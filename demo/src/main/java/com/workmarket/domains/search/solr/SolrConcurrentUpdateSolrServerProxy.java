package com.workmarket.domains.search.solr;

import com.codahale.metrics.MetricRegistry;
import com.workmarket.common.kafka.KafkaClient;
import com.workmarket.service.featuretoggle.FeatureEntitlementService;
import com.workmarket.service.web.WebRequestContextProvider;
import org.apache.http.client.HttpClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrRequest;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.StreamingResponseCallback;
import org.apache.solr.client.solrj.impl.ConcurrentUpdateSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrInputDocument;
import org.apache.solr.common.SolrInputField;
import org.apache.solr.common.params.SolrParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * Extension to Solrj's ConcurrentUpdateSolrServer with metrics added in.
 *
 * The impementation here and the implementation of SolrHttpSolrServerProxy should be exactly the same
 * minus the SERVER_TYPE and a little in the constructors.
 *
 */
public class SolrConcurrentUpdateSolrServerProxy extends ConcurrentUpdateSolrServer {
    private static final Logger logger = LoggerFactory.getLogger(SolrConcurrentUpdateSolrServerProxy.class);

    private static final String SERVER_TYPE = "solr-concurrent";
    private static final String UNKNOWN_DOCID = "unknown";

    @Autowired private MetricRegistry metricRegistry;
    @Autowired private WebRequestContextProvider webRequestContextProvider;
    @Autowired
    @Qualifier(value = "SolrKafkaClient")
    private KafkaClient kafkaClient;
    @Autowired private FeatureEntitlementService featureEntitlementService;

    private final boolean publishToKafka;
    private final String core;

    private SolrTracking solrTracking;

    public SolrConcurrentUpdateSolrServerProxy(String solrServerUrl, int queueSize, int threadCount, String core, boolean publishToKafka) {
        super(solrServerUrl, queueSize, threadCount);
        this.publishToKafka = publishToKafka;
        this.core = core;
    }

    public SolrConcurrentUpdateSolrServerProxy(String solrServerUrl, HttpClient client, int queueSize, int threadCount, String core, boolean publishToKafka) {
        super(solrServerUrl, client, queueSize, threadCount);
        this.publishToKafka = publishToKafka;
        this.core = core;
    }


    @PostConstruct
    public void init() {
        solrTracking = new SolrTracking(core, SERVER_TYPE, publishToKafka, metricRegistry, kafkaClient);
    }


    //
    // While there are other add methods they eventually call these two so we just track our metric on them
    // so we don't double count.
    //
    @Override
    public UpdateResponse add(final Collection<SolrInputDocument> docs, int commitWithinMs) throws SolrServerException, IOException {
        solrTracking.add(docs);
        if (indexDirectlyToSolr()) {
            logger.info("Indexing docs to solr " + core + ". Num of docs: " + docs.size());
            return super.add(docs, commitWithinMs);
        } else {
            logger.info("Documents being directed over Kafka and will not be indexed in to " + core + ". Num of docs: " + docs.size());
            return new UpdateResponse();
        }
    }

    @Override
    public UpdateResponse add(final SolrInputDocument doc, int commitWithinMs) throws SolrServerException, IOException {
        solrTracking.add(doc);
        String docId = UNKNOWN_DOCID;
        SolrInputField idField = doc.get("id");
        if (idField != null) {
            docId = idField.getValue().toString();
        }
        if (indexDirectlyToSolr()) {
            logger.info("Indexing doc to solr " + core + ". Doc Id: " + docId);
            return super.add(doc, commitWithinMs);
        } else {
            logger.info("Document being directed over Kafka and will not be indexed in to " + core + ". Doc id: " + docId);
            return new UpdateResponse();
        }
    }

    /**
     * Returns a flag indicating if the incoming doc should be indexed in to solr
     * or just send over kafka.
     * @return boolean Returns true if the doc should be indexed, false if it should
     * just be sent over solr.
     */
    private boolean indexDirectlyToSolr() {
        return !SolrThreadLocal.isDirected() || SolrThreadLocal.isDirectedTowards(core);
    }

    //
    // While there are other delete methods they eventually call these two so we just track our metric on them
    // so we don't double count.
    //
    @Override
    public UpdateResponse deleteById(final String id, int commitWithinMs) throws SolrServerException, IOException {
        solrTracking.deleteById(id);
        if (indexDirectlyToSolr()) {
            return super.deleteById(id, commitWithinMs);
        } else {
            return new UpdateResponse();
        }

    }

    @Override
    public UpdateResponse deleteById(final List<String> ids, int commitWithinMs) throws SolrServerException, IOException {
        solrTracking.deleteById(ids);
        if (indexDirectlyToSolr()) {
            return super.deleteById(ids, commitWithinMs);
        } else {
            return new UpdateResponse();
        }
    }

    @Override
    public UpdateResponse deleteByQuery(String query, int commitWithinMs) throws SolrServerException, IOException {
        solrTracking.deleteByQuery(query);
        if (indexDirectlyToSolr()) {
            logger.warn("Delete by query used - we might not support this in the future");
            return super.deleteByQuery(query, commitWithinMs);
        } else {
            return new UpdateResponse();
        }
    }

    @Override
    public QueryResponse query(SolrParams params) throws SolrServerException {
        //recordQueryMetric(params);
        return query(params, SolrRequest.METHOD.POST);
    }

    @Override
    public QueryResponse query(SolrParams params, SolrRequest.METHOD method) throws SolrServerException {
        solrTracking.query(params);
        addRequestContext(params);
        return super.query(params, method);
    }

    @Override
    public QueryResponse queryAndStreamResponse(SolrParams params, StreamingResponseCallback callback) throws SolrServerException, IOException {
        solrTracking.query(params);
        addRequestContext(params);
        return super.queryAndStreamResponse(params, callback);
    }

    private void addRequestContext(SolrParams params) {
        if (params instanceof SolrQuery) {
            if (webRequestContextProvider.getWebRequestContext().getRequestId() != null) {
                ((SolrQuery)params).add("mRequestId", webRequestContextProvider.getWebRequestContext().getRequestId());
            }
        }
    }
}

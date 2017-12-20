package com.workmarket.service.business;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.workmarket.domains.model.MimeType;
import com.workmarket.domains.model.User;
import com.workmarket.domains.model.asset.WorkAssetAssociation;
import com.workmarket.domains.work.model.Work;
import com.workmarket.domains.work.service.WorkService;
import com.workmarket.domains.work.service.route.WorkRoutingService;
import com.workmarket.service.business.dto.AssetDTO;
import com.workmarket.service.business.wrapper.AcceptWorkResponse;
import com.workmarket.service.infra.business.AuthenticationService;
import com.workmarket.service.infra.business.UploadService;
import com.workmarket.service.infra.file.RemoteFile;
import com.workmarket.service.infra.file.RemoteFileAdapter;
import com.workmarket.service.infra.file.RemoteFileType;
import com.workmarket.test.IntegrationTest;
import com.workmarket.utility.RandomUtilities;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@Category(IntegrationTest.class)
public class SignatureServiceIT extends BaseServiceIT {
	@Autowired AuthenticationService authenticationService;
	@Autowired AssetManagementService assetManagementService;
	@Autowired UploadService uploadService;
	@Autowired ResourceLoader resourceLoader;
	@Autowired SignatureService signatureService;
	@Autowired WorkRoutingService workRoutingService;
	@Autowired WorkService workService;
	@Autowired RemoteFileAdapter remoteFileAdapter;

	User employee;
	User contractor;
	Work work;
	String base64Image = "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";
	RemoteFile remoteFile;
	String fileName;
	File localFile;

	@Before
	public void before() throws Exception {
		employee = newFirstEmployeeWithCashBalance();
		contractor = newContractor();
		work = newWork(employee.getId());
		assertNotNull(work);
		laneService.addUserToCompanyLane2(contractor.getId(), employee.getCompany().getId());

		workRoutingService.openWork(work.getWorkNumber());
		routingStrategyService.addUserNumbersRoutingStrategy(work.getId(), Sets.newHashSet(contractor.getUserNumber()), 0, false);

		remoteFile = null;
		fileName = null;
		localFile = null;

		AcceptWorkResponse response = workService.acceptWork(contractor.getId(), work.getId());
		assertTrue(response.isSuccessful());
	}

	@After
	public void after() throws Exception {
		if (fileName != null) {
			remoteFileAdapter.delete(RemoteFileType.PUBLIC, fileName);
		}

		if (localFile != null) {
			FileUtils.deleteQuietly(localFile);
		}
	}

	@Test
	public void test_signatureUpload_createsFile() throws Exception {
		fileName = UUID.randomUUID().toString();
		remoteFile = signatureService.uploadSignatureImage(work.getId(), base64Image, fileName);
		assertNotNull(remoteFile);
	}

	@Test
	public void test_pdfAttach_configuredCorrectly() throws Exception {
		String uniqueId = RandomUtilities.generateAlphaNumericString(10);
		initializeTestFile(uniqueId);
		localFile = new File(STORAGE_TEST_FILE + uniqueId);

		AssetDTO signatureAssetDto = signatureService.attachSignaturePdfToWork(work.getId(), 1L, 1, localFile.getPath());
		assertNotNull(signatureAssetDto);

		/* Check basic configuration */
		assertEquals(MimeType.PDF.getMimeType(), signatureAssetDto.getMimeType());
		assertTrue(signatureAssetDto.getFileByteSize() > 0);
		assertNotNull(signatureAssetDto.getAssetId());

		/* Ensure it's actually associated with the assignment */
		List<WorkAssetAssociation> workAssociations = assetManagementService.findWorkAssetAssociationsByWork(Lists.newArrayList(work), signatureAssetDto.getAssetId());
		assertTrue(workAssociations.size() == 1);

		deleteTestFile(uniqueId);
	}
}

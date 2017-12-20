package com.workmarket.common.template;

import com.workmarket.configuration.Constants;
import com.workmarket.domains.model.WorkResource;
import com.workmarket.domains.model.notification.NotificationType;
import com.workmarket.domains.work.model.Work;
import com.workmarket.service.infra.communication.ReplyToType;

public class WorkUnassignedNotificationTemplate extends AbstractWorkNotificationTemplate {
	private static final long serialVersionUID = -1034214818464499255L;

	private WorkResource resource;
	private String message;

	public WorkUnassignedNotificationTemplate(Long toId, Work work, WorkResource resource, String message) {
		super(Constants.EMAIL_USER_ID_TRANSACTIONAL, toId, new NotificationType(NotificationType.RESOURCE_WORK_CANCELLED), ReplyToType.TRANSACTIONAL, work);
		this.resource = resource;
		this.message = message;
	}

	public WorkResource getResource() {
		return resource;
	}

	public String getMessage() {
		return message;
	}
}

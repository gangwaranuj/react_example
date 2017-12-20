package com.workmarket.common.template;

import com.workmarket.domains.model.User;
import com.workmarket.domains.groups.model.UserGroup;
import com.workmarket.domains.model.notification.NotificationType;
import com.workmarket.configuration.Constants;
import com.workmarket.service.infra.communication.ReplyToType;

public class UserGroupInvitationForUserProfileModificationOwnerNotificationTemplate extends AbstractUserGroupNotificationTemplate {
	/**
	 *
	 */
	private static final long serialVersionUID = 2128343088472195322L;

	private User resource;

	public UserGroupInvitationForUserProfileModificationOwnerNotificationTemplate(Long toId, UserGroup group, User resource) {
		super(Constants.EMAIL_USER_ID_TRANSACTIONAL, toId, new NotificationType(NotificationType.GROUP_INVITED_PROFILE_MODIFIED_GROUP_OWNER), ReplyToType.TRANSACTIONAL, group);
		this.resource = resource;
	}

	public User getResource() {
		return resource;
	}
}

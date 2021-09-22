package com.iexpress.spring.api.model;

import lombok.Data;

@Data
public class NotificationModel {

	private int notificationId;
	private int userId;
	private int senderId;
	private int notificationBadgeCount;
	private String notificationType;
	private UserModel sender;
	
	private String nameToBeReplaced;
	private String notificationMessage ;

	private boolean isRead;
	private String createdAt;
	
	private String userName;
	private String senderName;

	private int postId;
	private String postTitle;
	private boolean isDeleted;

}

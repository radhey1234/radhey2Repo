package com.iexpress.spring.api.model;

import java.util.List;

import com.iexpress.spring.api.model.builder.UserModelBuilder;

import lombok.Data;

@Data
public class UserModel {
	
	private final long userId;
	private final String email;
	private final String phoneNumber;
	private final String authenticationHeader;
	private final ProfileModel profile;
	private final boolean firstSignIn;
	private final boolean shouldUserBeShownStartUpViedo;
	private final Double lon;
	private final Double lat;
	private final int unreadNotificationCount;
	private final List<PostModel> posts;
	
	public UserModel (UserModelBuilder builder) {
		this.userId = builder.getUserId();
		this.email = builder.getEmail();
		this.phoneNumber = builder.getPhoneNumber();
		this.authenticationHeader = builder.getAuthenticationHeader();
		this.profile = builder.getProfile();
		this.firstSignIn = builder.isFirstSignIn();
		this.shouldUserBeShownStartUpViedo = builder.isShouldUserBeShownStartUpViedo();
		this.lon = builder.getLon();
		this.lat = builder.getLat();
		this.posts = builder.getPosts();
		this.unreadNotificationCount = builder.getUnreadNotificationCount();
	}
}

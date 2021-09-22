package com.iexpress.spring.api.model.builder;

import java.util.List;

import com.iexpress.spring.api.model.PostModel;
import com.iexpress.spring.api.model.ProfileModel;
import com.iexpress.spring.api.model.UserModel;

import lombok.Getter;

@Getter
public class UserModelBuilder {
	
	private long userId;
	private String email;
	private String phoneNumber;
	private String authenticationHeader;
	private ProfileModel profile;
	private boolean isFirstSignIn;
	private boolean shouldUserBeShownStartUpViedo;
	private Double lon;
	private Double lat;
	private List<PostModel> posts;
	private int unreadNotificationCount;
	
	private UserModelBuilder() {}
	
	public UserModelBuilder shouldUserBeShownStartUpViedo(boolean shouldUserBeShownStartUpViedo) {
		this.shouldUserBeShownStartUpViedo = shouldUserBeShownStartUpViedo;
		return this;
	}
	
	public UserModelBuilder firstSignIn(boolean isFirstSignIn) {
		this.isFirstSignIn = isFirstSignIn;
		return this;
	}
	
	public UserModelBuilder email(String email) {
		this.email = email;
		return this;
	}

	public UserModelBuilder phoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
		return this;
	}
	
	public UserModelBuilder authenticationHeader(String authenticationHeader) {
		this.authenticationHeader = authenticationHeader;
		return this;
	}	
	
	public UserModelBuilder userId(long l) {
		this.userId = l;
		return this;
	}
	
	public UserModelBuilder profile(ProfileModel profile) {
		this.profile = profile;
		return this;
	}

	public UserModelBuilder lat(Double lat) {
		this.lat = lat;
		return this;
	}
	
	public UserModelBuilder lon(Double lon) {
		this.lon = lon;
		return this;
	}

	public UserModelBuilder posts(List<PostModel> posts) {
		this.posts = posts;
		return this;
	}
	
	public static UserModelBuilder of() {
		return new UserModelBuilder();
	}
	
	public UserModel buildUser() {
		return new UserModel(this);
	}
}

package com.iexpress.spring.api.model;

import com.iexpress.spring.api.model.builder.ProfileModelBuilder;

import lombok.Data;

@Data
public class ProfileModel {
	
	private String firstName;
	private String lastName;
	private String city;
	private String state;
	private String profileUrl;
	private String profileBio;
	private String userName;
	private String dob;
	
	public ProfileModel(ProfileModelBuilder builder) {
		this.userName = builder.getUserName();
		this.firstName = builder.getFirstName();
		this.lastName = builder.getLastName();
		this.city = builder.getCity();
		this.state = builder.getState();
		this.profileUrl = builder.getProfileUrl();
		this.profileBio = builder.getProfileBio();
		this.dob = builder.getDob();
	}

}

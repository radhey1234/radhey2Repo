package com.iexpress.spring.api.model.builder;

import com.iexpress.spring.api.model.ProfileModel;
import com.iexpress.spring.domain.Profile;

import lombok.Getter;

@Getter
public class ProfileModelBuilder {

	private String firstName;
	private String lastName;
	private String city;
	private String state;
	private String profileUrl;
	private String profileBio;
	private String userName;
	private String dob;
	
	private ProfileModelBuilder() {}
	
	public static ProfileModelBuilder of() {
		return new ProfileModelBuilder();
	}
	
	public ProfileModelBuilder firstName(final String firstName) {
		this.firstName = firstName;
		return this;
	}
	
	public ProfileModelBuilder lastName(final String lastName) {
		this.lastName = lastName;
		return this;
	}
	
	public ProfileModelBuilder userName(final String userName) {
		this.userName = userName;
		return this;
	}
	
	public ProfileModelBuilder city(final String city) {
		this.city = city;
		return this;
	}
	
	public ProfileModelBuilder state(final String state) {
		this.state = state;
		return this;
	}
	
	public ProfileModelBuilder profileUrl(final String profileUrl) {
		this.profileUrl = profileUrl;
		if(this.profileUrl != null) {
			this.profileUrl = "https://dev-sandee-public-test.s3.ap-south-1.amazonaws.com/" + this.profileUrl;
		}
		return this;
	}
	
	public ProfileModelBuilder profileBio(final String profileBio) {
		this.profileBio = profileBio;
		return this;
	}
	
	public ProfileModelBuilder dob(final String dob) {
		this.dob = dob;
		return this;
	}
	
	public ProfileModelBuilder profile(Profile profile) {
		userName(profile.getUserName());
		firstName(profile.getFirstName());
		lastName(profile.getLastName());
		city(profile.getCity());
		state(profile.getState());
		profileUrl(profile.getProfilePic());
		profileBio(profile.getProfileBio());
		dob(profile.getDob());
		return this;
	}
	
	public ProfileModel build() {
		return new ProfileModel(this);
	}

}

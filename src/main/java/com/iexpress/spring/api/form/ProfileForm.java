package com.iexpress.spring.api.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class ProfileForm extends AddressForm {
	
	private String fullName;
	private String userName;
	private String firstName;
	private String lastName;
	private String profilePic;
	private String profileBio;
	private String password;
	private Double lat;
	private Double lon;

}

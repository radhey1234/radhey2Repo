package com.iexpress.spring.api.form;


import javax.validation.constraints.NotBlank;

import com.iexpress.spring.api.validator.ValidValues;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class SignUpForm extends ProfileForm{

	public interface phoneRelatedField {};
	public interface emailRelatedField {};
	public interface signInEmailRelatedField {};
	public interface signInPhoneRelatedField {};

	
	@NotBlank(message = "empty.countryCode" , groups = {SignUpForm.phoneRelatedField.class,signInPhoneRelatedField.class})
	private String countryCode;
	@NotBlank(message = "empty.phoneNumber" , groups = { SignUpForm.phoneRelatedField.class,signInPhoneRelatedField.class})
	private String phoneNumber;
	@NotBlank(message = "empty.email", groups =  {SignUpForm.emailRelatedField.class,SignUpForm.signInEmailRelatedField.class})
	private String email;
	@NotBlank(message = "empty.password",groups =  {SignUpForm.phoneRelatedField.class,
			SignUpForm.emailRelatedField.class, SignUpForm.signInEmailRelatedField.class,signInPhoneRelatedField.class})
	private String password;
	@NotBlank(message = "empty.deviceToken")
	private String deviceToken;
	
	@NotBlank(message = "empty.deviceType")
	@ValidValues(acceptedValues = { "IOS,ANDROID, WEB" }, message = "invalid.deviceType")
	private String deviceType; // IOS, Android, Web
	@NotBlank(message = "empty.inputType")
	@ValidValues(acceptedValues = { "Phone, Email, FB, Twitter" }, message = "invalid.inputType")
	private String inputType;
	private String verifiedMobileOrEmailToken;
	private boolean isVerificationTokenVerified;
}

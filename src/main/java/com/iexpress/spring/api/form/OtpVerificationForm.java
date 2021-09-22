package com.iexpress.spring.api.form;

import lombok.Data;

@Data
public class OtpVerificationForm {
	
	private String email;
	private String userId;
	private String phoneNumber;
	private String otp;
	private String verificationKey;
}

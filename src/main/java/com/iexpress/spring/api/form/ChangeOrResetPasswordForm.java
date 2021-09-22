package com.iexpress.spring.api.form;

import lombok.Data;

@Data
public class ChangeOrResetPasswordForm {
	
	private String currentPassword;
	private String newPassword;
	private String confirmPassword;
	private String email;
	private String phoneNumber;
	private String verificationToken;
	private String verificationOtp;
	private String inputType;
}

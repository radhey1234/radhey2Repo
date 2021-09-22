package com.iexpress.spring.api.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ResentEmailOrOTPForm {

	private String reasonOfResend; // possible values RESET_PASSWORD, ACCOUNT_VERIFICATION
	@NotBlank(message = "empty.inputType")
	private String inputType; //Phone, Email, FB, Twitter
	private String countryCode;
	private String phoneNumber;
	private String email;

}

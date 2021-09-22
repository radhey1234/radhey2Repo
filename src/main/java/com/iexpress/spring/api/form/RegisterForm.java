package com.iexpress.spring.api.form;

import lombok.Data;

@Data
public class RegisterForm  {

	private String email;
	private String phoneNumber;
	private String countryCode;
}

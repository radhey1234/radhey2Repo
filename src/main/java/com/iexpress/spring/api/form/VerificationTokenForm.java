package com.iexpress.spring.api.form;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VerificationTokenForm {

	private String verificationKey;
}

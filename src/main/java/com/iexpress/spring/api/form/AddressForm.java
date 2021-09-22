package com.iexpress.spring.api.form;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class AddressForm extends EducationForm {

	private String dob;
	private String city;
	private String state;
	private String country;
	private int cityId;
	
}

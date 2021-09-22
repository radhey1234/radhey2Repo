package com.iexpress.spring.api.form;

import lombok.Data;

@Data
public class EducationForm {

	private String typeOfEducation;
	private String degree;
	private String institueName;
	private String startDate;
	private String endDate;
	private String cityOfStudy;
	private String stateOfStudy;
	private String countryOfStudy;
	
}

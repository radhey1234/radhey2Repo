package com.iexpress.spring.api.response;


public class ErrorResponse {
	

	private String errorField;
	private String errorMessage;
	
	public ErrorResponse(String errorField, String errorMessage) {
		super();
		this.errorField = errorField;
		this.errorMessage = errorMessage;
	}

	public String getErrorField() {
		return errorField;
	}

	public void setErrorField(String errorField) {
		this.errorField = errorField;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}

package com.iexpress.spring.api.exception;

import java.util.List;

import com.iexpress.spring.api.response.ErrorResponse;

public class GenericException extends RuntimeException {

	private static final long serialVersionUID = -1423713963594503855L;
	private String message;
	private int errorCode;
	private String field;
	private String fieldMessage;
	private List<ErrorResponse> errors;
	
	public GenericException(String message, int errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}

	public GenericException(String message, int errorCode, List<ErrorResponse> errors) {
		this.message = message;
		this.errorCode = errorCode;
		this.errors = errors;
	}

	public GenericException(String field, String fieldMessage , String message, int errorCode) {
		this.field = field;
		this.fieldMessage = fieldMessage;
		this.message = message;
		this.errorCode = errorCode;
	}
	public GenericException(String message) {
		this.message = message;
	}

	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}
	
	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getFieldMessage() {
		return fieldMessage;
	}

	public void setFieldMessage(String fieldMessage) {
		this.fieldMessage = fieldMessage;
	}

	public List<ErrorResponse> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorResponse> errors) {
		this.errors = errors;
	}

}

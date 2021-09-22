package com.iexpress.spring.api.response;

import java.util.Collections;
import java.util.Date;
import java.util.List;

public class ResponseEnvelop<T> {
	
	private T data;
	private String message;
	private Date timeStamp;
	private int code;
	private List<ErrorResponse> errors = Collections.emptyList();
	
	public ResponseEnvelop(T data, String message, int code, List<ErrorResponse> errors) {
		this.data = data;
		this.message = message;
		this.timeStamp = new Date();
		this.code = code;
		this.errors = errors;
	}
	
	public ResponseEnvelop(T data, String message, int code) {
		this.data = data;
		this.message = message;
		this.timeStamp = new Date();
		this.code = code;
	}
	
	public ResponseEnvelop(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public List<ErrorResponse> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorResponse> errors) {
		this.errors = errors;
	}
	
}

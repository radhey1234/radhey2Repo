package com.iexpress.spring.api.validator;

import java.util.Arrays;

import com.iexpress.spring.api.exception.GenericException;
import com.iexpress.spring.api.form.SignUpForm;
import com.iexpress.spring.api.response.ErrorResponse;
import com.iexpress.spring.api.util.RestConstant;

public final class ValidateUtil {

	private static ValidateUtil util = new ValidateUtil();
	
	private ValidateUtil() {}

	public static ValidateUtil of() {
		return util;
	}
	
	public void validateForm(SignUpForm signUpForm, FormType formType) {
		
		switch (formType) {
		case SIGNUP_EMAIL:
			validateEmailAndPassword(signUpForm);
			break;
		case SIGNUP_PHONE:
			validatePhoneAndPassword(signUpForm);
			break;
		case SIGNIN_EMAIL:
			validateEmailAndPassword(signUpForm);
			break;
		case SIGNIN_PHONE:
			validatePhoneAndPassword(signUpForm);
			break;
		default:
			break;
		}
	}

	private void validateEmailAndPassword(SignUpForm signUpForm) {
		if(null == signUpForm.getVerifiedMobileOrEmailToken()){
			validateEmail(signUpForm.getEmail());
		}
		validatePassword(signUpForm.getPassword());
	}

	private void validatePhoneAndPassword(SignUpForm signUpForm) {
		if(null == signUpForm.getVerifiedMobileOrEmailToken()){
			validatePhone(signUpForm.getPhoneNumber());
		}
		validatePassword(signUpForm.getPassword());
	}

	private void validatePassword(String password) {
		if(null == password || password.isEmpty()) {
			ErrorResponse error = new ErrorResponse(RestConstant.PASSWORD, RestConstant.EMPTY_PASSWORD);
			throw new GenericException(RestConstant.EMPTY_PASSWORD, RestConstant.BAD_REQUEST, Arrays.asList(error));			
		}		
	}

	private void validateEmail(String email) {
		if(null == email || email.isEmpty()) {
			ErrorResponse error = new ErrorResponse(RestConstant.EMAIL, RestConstant.EMPTY_EMAIL);
			throw new GenericException(RestConstant.EMPTY_EMAIL, RestConstant.BAD_REQUEST, Arrays.asList(error));
		}
	}
	
	private void validatePhone(String phone) {
		if(null == phone || phone.isEmpty()) {
			ErrorResponse error = new ErrorResponse(RestConstant.PHONE, RestConstant.EMPTY_EMAIL);
			throw new GenericException(RestConstant.EMPTY_PHONE, RestConstant.BAD_REQUEST, Arrays.asList(error));
		}
	}
}

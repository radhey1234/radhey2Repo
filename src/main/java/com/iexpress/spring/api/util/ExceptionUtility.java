package com.iexpress.spring.api.util;

import static com.iexpress.spring.api.util.RestConstant.BAD_REQUEST;
import static com.iexpress.spring.api.util.RestConstant.EMAIL_NOT_VERIFIED;
import static com.iexpress.spring.api.util.RestConstant.EXISTING_EMAIL;
import static com.iexpress.spring.api.util.RestConstant.EXISTING_PHONE_NUMBER;
import static com.iexpress.spring.api.util.RestConstant.FIELD_EMAIL;
import static com.iexpress.spring.api.util.RestConstant.FIELD_PHONE_NUMBER;
import static com.iexpress.spring.api.util.RestConstant.INCORRECT_PASSWORD;
import static com.iexpress.spring.api.util.RestConstant.LOGIN_FAILURE;
import static com.iexpress.spring.api.util.RestConstant.NOT_EXISTING_EMAIL;
import static com.iexpress.spring.api.util.RestConstant.NOT_EXISTING_PHONE_NUMBER;
import static com.iexpress.spring.api.util.RestConstant.PHONE_NUMBER_NOT_VERIFIED;
import static com.iexpress.spring.api.util.RestConstant.UNAUTHENTICATED;
import static com.iexpress.spring.api.util.RestConstant.VALIDATION_FAILURE;
import com.iexpress.spring.api.exception.GenericException;

public class ExceptionUtility {

	public static void throwExistingPhoneNumberError() {
		throw new GenericException(FIELD_PHONE_NUMBER,EXISTING_PHONE_NUMBER , VALIDATION_FAILURE ,BAD_REQUEST);
	}

	public static GenericException phoneNumberAlreadExist() {
		return new GenericException(FIELD_PHONE_NUMBER,EXISTING_PHONE_NUMBER , VALIDATION_FAILURE ,BAD_REQUEST);
	}


	public static GenericException emailAlreadExist() {
		return new GenericException(FIELD_EMAIL,EXISTING_EMAIL , VALIDATION_FAILURE ,BAD_REQUEST);
	}

	public static GenericException invalidVerificationToken() {
		return new GenericException(FIELD_PHONE_NUMBER,EXISTING_PHONE_NUMBER , VALIDATION_FAILURE ,BAD_REQUEST);
	}
	
	public static void throwExistingEmailError() {
		throw new GenericException(FIELD_EMAIL, EXISTING_EMAIL, VALIDATION_FAILURE , BAD_REQUEST);		
	}
	
	public static void throwPasswordIsIncorrect() {
		throw new GenericException(FIELD_PHONE_NUMBER, INCORRECT_PASSWORD , 
				LOGIN_FAILURE , 	UNAUTHENTICATED);		
	}
	
	public static GenericException authenticationFailed() {
		return new GenericException(FIELD_PHONE_NUMBER, INCORRECT_PASSWORD , 
				LOGIN_FAILURE , 	UNAUTHENTICATED);		
	}

	public static void throwPhoneNumberNotVerified() {
		throw new GenericException(FIELD_PHONE_NUMBER, PHONE_NUMBER_NOT_VERIFIED , 
				PHONE_NUMBER_NOT_VERIFIED ,  UNAUTHENTICATED);		
	}

	public static GenericException phoneNumberNotVerified() {
		return new GenericException(FIELD_PHONE_NUMBER, PHONE_NUMBER_NOT_VERIFIED , 
				PHONE_NUMBER_NOT_VERIFIED ,  UNAUTHENTICATED);		
	}
	
	public static void throwPhoneNumberDoesNotExist() {
		throw new GenericException(FIELD_PHONE_NUMBER, NOT_EXISTING_PHONE_NUMBER , 
				NOT_EXISTING_PHONE_NUMBER , 	UNAUTHENTICATED);
	}

	public static GenericException phoneNumberDoesNotExist() {
		return new GenericException(FIELD_PHONE_NUMBER, NOT_EXISTING_PHONE_NUMBER , 
				NOT_EXISTING_PHONE_NUMBER , 	UNAUTHENTICATED);
	}
	
	public static void throwEmailNotVerified() {
		throw new GenericException(FIELD_EMAIL, EMAIL_NOT_VERIFIED , 
				EMAIL_NOT_VERIFIED ,  UNAUTHENTICATED);
	}

	public static GenericException emailNotVerified() {
		return new GenericException(FIELD_EMAIL, EMAIL_NOT_VERIFIED , 
				EMAIL_NOT_VERIFIED ,  UNAUTHENTICATED);
	}

	public static GenericException emailDoesNotExist() {
		return new GenericException(FIELD_EMAIL, NOT_EXISTING_EMAIL , 
				NOT_EXISTING_EMAIL , 	UNAUTHENTICATED);		
	}

	
	public static void throwErrorEmailDoesNotExist() {
		throw new GenericException(FIELD_EMAIL, NOT_EXISTING_EMAIL , 
				NOT_EXISTING_EMAIL , 	UNAUTHENTICATED);		
	}

}

package com.iexpress.spring.api.verification.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.iexpress.spring.api.exception.GenericException;
import com.iexpress.spring.api.form.OtpVerificationForm;
import com.iexpress.spring.api.form.ResentEmailOrOTPForm;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.otp.service.VerificationService;
import static com.iexpress.spring.api.util.AppUtil.*;
import com.iexpress.spring.api.util.RestConstant;

@Component
public class VerificationFacade {
	
	@Autowired private VerificationService verificationService;
       
	public UserModel verifyOtp(@Valid OtpVerificationForm otpVerificationForm) {
		
		if(!isNullOrEmpty(otpVerificationForm.getEmail())) {
			return verificationService.verifyEmailOtp(otpVerificationForm.getEmail(),
					otpVerificationForm.getOtp(), otpVerificationForm.getUserId());			
		}
		if(!isNullOrEmpty(otpVerificationForm.getPhoneNumber())) {
			return verificationService.verifyPhoneOtp(otpVerificationForm.getPhoneNumber(),
					otpVerificationForm.getOtp(), otpVerificationForm.getUserId());			
		}
		throw new GenericException(RestConstant.EMAIL_OR_PHONE, RestConstant.EMPTY_EMAIL_OR_PHONE, RestConstant.EMPTY_EMAIL_OR_PHONE, RestConstant.BAD_REQUEST);
	}

	public boolean resendOtpOrEmailLink(final ResentEmailOrOTPForm resentOtpForm) {
		boolean isVerified = false;
		if(resentOtpForm.getInputType().equalsIgnoreCase(RestConstant.PHONE)) {
			isVerified = verificationService.resentOtp(resentOtpForm.getCountryCode(), resentOtpForm.getPhoneNumber(),  resentOtpForm.getReasonOfResend());
		}
		
		if(resentOtpForm.getInputType().equalsIgnoreCase(RestConstant.EMAIL)) {
			isVerified = verificationService.resentEmailLink( resentOtpForm.getEmail(),  resentOtpForm.getReasonOfResend());			
		}
		return isVerified;
	}

	public boolean verifyEmailLink(final String randomeToken,final int id) {
		return verificationService.verifyEmailLink(randomeToken, id);
	}

	public String verifyIntialOtp(@Valid OtpVerificationForm otpVerificationForm) {
		return verificationService.verifyEmailOrPhoneNumberByOtp(otpVerificationForm);
	}

}

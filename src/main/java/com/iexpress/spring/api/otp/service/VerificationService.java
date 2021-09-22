package com.iexpress.spring.api.otp.service;

import org.springframework.stereotype.Service;

import com.iexpress.spring.api.form.OtpVerificationForm;
import com.iexpress.spring.api.model.UserModel;

@Service
public interface VerificationService {
	
	public boolean resentOtp(final String countryCode, final String phoneNumber , final String reasonOfResend);
	public UserModel verifyPhoneOtp(final String phoneNumber, final String otp, final String userId);
	public UserModel verifyEmailOtp(final String email, final String otp, final String userId);
	public boolean resentEmailLink(final String email, final String reasonOfSending);
	public boolean verifyEmailLink(String randomeToken, int userId);
	String verifyEmailOrPhoneNumberByOtp(OtpVerificationForm otpVerificationForm);
}

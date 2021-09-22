package com.iexpress.spring.api.otp.service;

import org.springframework.stereotype.Service;

import com.iexpress.spring.domain.OtpVerification;
import com.iexpress.spring.domain.User;

@Service
public interface OtpService {

	public OtpVerification saveOtp(String email , String phoneNumber, String countryCode, String generatedOtp,
			String verificationReason, User user);
}

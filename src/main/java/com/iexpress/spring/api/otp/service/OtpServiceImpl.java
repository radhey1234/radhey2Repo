package com.iexpress.spring.api.otp.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iexpress.spring.api.repo.OtpVerificationRepo;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.domain.OtpVerification;
import com.iexpress.spring.domain.User;
import com.iexpress.spring.domain.VerificationReason;
import com.iexpress.spring.domain.VerificationStatus;

@Service
public class OtpServiceImpl implements OtpService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(OtpServiceImpl.class);
	@Autowired
	private OtpVerificationRepo	otpVerificationRepo;
	
	
	@Override
	@Transactional
	public OtpVerification saveOtp(String email, String phoneNumber, String countryCode, String generatedOtp,
			String verificationReason, User user) {
		LOGGER.info("Inside saveOtp");
		String randomToken = AppUtil.getRandomString();
		OtpVerification otp = new OtpVerification();
		otp.setOtp(generatedOtp);
		otp.setCountryCode(countryCode);
		otp.setEmail(email);
		otp.setMobile(phoneNumber);
		otp.setStatus(VerificationStatus.UNVERIFIED);
		otp.setReason(VerificationReason.valueOf(verificationReason));
		otp.setExpiredAt(AppUtil.getExpiredAt());
		otp.setRandomeToken(randomToken);
		otp.setUser(user);
		otpVerificationRepo.save(otp);
		return otp;
	}

}

package com.iexpress.spring.api.otp.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iexpress.spring.api.form.OtpVerificationForm;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.otp.service.OtpService;
import com.iexpress.spring.api.otp.service.VerificationService;
import com.iexpress.spring.api.repo.OtpVerificationRepo;
import com.iexpress.spring.api.user.service.UserService;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.api.util.MailService;
import com.iexpress.spring.api.util.SmsServiceUtil;
import static com.iexpress.spring.api.util.AppUtil.*;
import com.iexpress.spring.domain.EmailStatus;
import com.iexpress.spring.domain.MobileEmailVerification;
import com.iexpress.spring.domain.MobileStatus;
import com.iexpress.spring.domain.OtpVerification;
import com.iexpress.spring.domain.User;
import com.iexpress.spring.domain.VerificationStatus;
import static com.iexpress.spring.api.util.RestConstant.*;

@Service
public class VerificationServiceImpl implements VerificationService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VerificationServiceImpl.class);

	@Autowired private OtpVerificationRepo otpVerificationRepo;
	@Autowired private UserService userService;
	@Autowired private OtpService otpService;
	@Autowired private MailService mailService;
	@Autowired private Environment environment;
	@Autowired private SmsServiceUtil smsService;
	@Autowired private MobileOrEmailVerificationService mobileOrEmailVerificationService;
	/**
	 * Returns false if email is sent again 
	 * true if it is already verified
	 * Exception if it invalid phone
	 */
	@Override
	public boolean resentOtp(final String countryCode,final String phoneNumber,final String reasonOfResend) {
		LOGGER.info("Inside resent Otp phone number otp user id", phoneNumber);
		final User user = checkIfPhoneNumberExist(phoneNumber);

		if(isUserVerified(user.getMobileStatus().name())) {
			LOGGER.info("Already verified ! Please login");
			return true;
		}
		
		OtpVerification verification = otpVerificationRepo.findFirst1ByMobileOrderByCreatedAtDesc(phoneNumber);

		if(null == verification || isExpired(verification.getExpiredAt())) {
			LOGGER.info("Sent new OTP");
			final String randomOtp = AppUtil.generateRandomNumericCode(AppUtil.OTP_LENGTH);
			sendOtp(countryCode, phoneNumber, randomOtp);
			otpService.saveOtp(null, phoneNumber, countryCode, randomOtp, reasonOfResend , user);
			return false;
		}
		
		sendOtp(countryCode, phoneNumber, verification.getOtp());
		LOGGER.info("Sent you the same OTP again !");
		return false;
	}

	@Override
	@Transactional
	public UserModel verifyPhoneOtp(String phoneNumber, String otp, String userId) {
		LOGGER.info("Inside verify Otp phone number otp user id", phoneNumber, otp, userId);
		OtpVerification verification = otpVerificationRepo.findFirst1ByMobileOrderByCreatedAtDesc(phoneNumber);

		if(isNull(verification)) {
			throwNotExistingPhone();
		}

		if(isVerified(verification.getStatus().name())) {
			throwAlreadyVerified();
		}

		if(isExpired(verification.getExpiredAt())) {
			throwExpiredOTP();
		}
		
		if(!isEqual(verification.getOtp() ,otp)) {
			throwInvalidOtp();
		}

		verification.setStatus(VerificationStatus.VERIFIED);
		otpVerificationRepo.save(verification);
		User user = verification.getUser();
		user.setMobileStatus(MobileStatus.ACTIVE);
		userService.save(user);
		LOGGER.info("Phone number is verified "+ phoneNumber);
		return userService.buildUserModel(user, true);
	}
	

	@Override
	@Transactional
	public UserModel verifyEmailOtp(String email, String otp, String userId) {
		LOGGER.info("Inside verify Otp email otp user id" + email, otp, userId);
		OtpVerification verification = otpVerificationRepo.findFirst1ByEmailOrderByCreatedAtDesc(email);

		if(isNull(verification)) {
			throwNotExistingEmail();
		}

		if(isVerified(verification.getStatus().name())) {
			throwAlreadyVerified();
		}

		if(isExpired(verification.getExpiredAt())) {
			throwExpiredOTP();
		}
		
		if(!isEqual(verification.getOtp() ,otp)) {
			throwInvalidOtp();
		}
		
		verification.setStatus(VerificationStatus.VERIFIED);
		otpVerificationRepo.save(verification);
		User user = verification.getUser();
		user.setEmailStatus(EmailStatus.ACTIVE);
		userService.save(user);
		LOGGER.info("Email is verified "+ email);
		return userService.buildUserModel(user, true);
	}
	
	@Override
	@Transactional
	public String verifyEmailOrPhoneNumberByOtp(OtpVerificationForm otpVerificationForm) {
		LOGGER.info("Inside verifyEmailOrPhoneNumberByOtp Otp phone number otp user id", otpVerificationForm.getPhoneNumber(), otpVerificationForm.getOtp());
		MobileEmailVerification verification = null;
		
		if(!StringUtils.isEmpty(otpVerificationForm.getVerificationKey())) {
			verification = mobileOrEmailVerificationService.findVerificationTokenDetail(otpVerificationForm.getVerificationKey());
		}
		else if(!StringUtils.isEmpty(otpVerificationForm.getPhoneNumber())) {
			verification = mobileOrEmailVerificationService.findFirst1ByMobileOrderByCreatedAtDesc(otpVerificationForm.getPhoneNumber());
		}else {
		   verification = mobileOrEmailVerificationService.findFirst1ByEmailOrderByCreatedAtDesc(otpVerificationForm.getEmail());
		}

		if(isNull(verification) || !verification.getOtp().equals(otpVerificationForm.getOtp())) {
			throwInvalidOtp();
		}
		
		verification.setStatus(ACTIVE);
		mobileOrEmailVerificationService.save(verification);
		return verification.getRandomeToken();
	}

	/**
	 * Returns false if email is sent again 
	 * true if it is already verified
	 * Exception if it invalid email
	 */
	@Override
	public boolean resentEmailLink(String email, String reasonOfSending) {
		LOGGER.info("Inside resentEmailLink user id", email);
		final User user = checkIfEmailExist(email);
		
		if(ACTIVE.equalsIgnoreCase(user.getEmailStatus().name())) {
			LOGGER.info("Already verified ! Please login");
			return true;
		}
		
		OtpVerification verification = otpVerificationRepo.findFirst1ByEmailOrderByCreatedAtDesc(email);

		if(isNull(verification) || isExpired(verification.getExpiredAt())) {
			LOGGER.info("Sent new email link");
			final String randomOtp = AppUtil.generateRandomNumericCode(AppUtil.OTP_LENGTH);
			final OtpVerification otp	= otpService.saveOtp(email, null, null, randomOtp, reasonOfSending, user);
			sendLinkOnEmail(email, otp.getRandomeToken(), user.getId());
			return false;
		}

		sendLinkOnEmail(email, verification.getRandomeToken(), user.getId());
		LOGGER.info("Sent you the same email link again !");
		return false;
	}

	@Override
	public boolean verifyEmailLink(final String randomeToken,final int userId) {
		LOGGER.info("Inside verifyLink userId randome token", userId, randomeToken);
		
		OtpVerification verification = otpVerificationRepo.findByUserIdAndRadomeToken(randomeToken, userId);
		
		if(isNull(verification) || isExpired(verification.getExpiredAt())) {
			LOGGER.info("Invalid link or Expired time , randome token", userId,randomeToken  );
			return false;
		}
		
		if(isVerified(verification.getStatus().name())) {
			LOGGER.info("The link already verifed it is user ID , randome token", userId,randomeToken  );
			return true;
		}
		
		verification.setStatus(VerificationStatus.VERIFIED);
		otpVerificationRepo.save(verification);
		verification.getUser().setEmailStatus(EmailStatus.ACTIVE);
		userService.save(verification.getUser());
		LOGGER.info("Link is verified");
		return true;
	}
	
	public User checkIfPhoneNumberExist(String phoneNumber) {
		return userService.checkIfPhoneNumberExist(phoneNumber);
	}

	public User checkIfEmailExist(String email) {
		return userService.checkIfEmailExist(email);
	}

	private void sendLinkOnEmail(String email, String randomeToken, int userId) {
		mailService.sendMail(email, "Please click on this link to verify"," Link is "+environment.getProperty("based.address")+"/api/v1/verify_email?randomeToken="+randomeToken+"&id="+userId);
	}

	private void sendOtp(String countryCode, String phoneNumber, String randomOtp) {
		try {
			smsService.sendMessage(environment.getProperty(SMS_ACCOUNT_SID), environment.getProperty(SMS_AUTH_TOKEN), environment.getProperty(SMS_FROM_NUMBER),"+"+countryCode+phoneNumber,"This is test otp - "+randomOtp);
		}catch(Exception e) {
			LOGGER.error("Error while sending OTP ", e);
		}
	}
	
}


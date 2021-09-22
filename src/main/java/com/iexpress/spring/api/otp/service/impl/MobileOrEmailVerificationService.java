package com.iexpress.spring.api.otp.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iexpress.spring.api.exception.GenericException;
import com.iexpress.spring.api.repo.MobileEmailVerificationRepo;
import com.iexpress.spring.api.util.ExceptionUtility;
import com.iexpress.spring.domain.MobileEmailVerification;

@Service
public class MobileOrEmailVerificationService {
	
	@Autowired private MobileEmailVerificationRepo mobileEmailVerificationRepo;

	@Transactional(readOnly = true)
	public MobileEmailVerification findVerifiedEmailOrMobileTokenDetail(String verifiedEmailOrMobileToken) throws GenericException {
		MobileEmailVerification mobileOrEmail = mobileEmailVerificationRepo.
				findFirst1ByRandomeTokenOrderByCreatedAtDesc(verifiedEmailOrMobileToken);
		 
		 if(null == mobileOrEmail) {
			throw ExceptionUtility.invalidVerificationToken();
		 }
		 
		 if(!mobileOrEmail.isVerificatioinTokenVerified()) {
			 if(mobileOrEmail.getMobile() != null)
				 throw ExceptionUtility.phoneNumberNotVerified();
			 else
				 throw ExceptionUtility.emailNotVerified();
		 }
		 return mobileOrEmail;
	}

	@Transactional(readOnly = true)
	public MobileEmailVerification findVerificationTokenDetail(String verifiedEmailOrMobileToken) throws GenericException {
		return mobileEmailVerificationRepo.findFirst1ByRandomeTokenOrderByCreatedAtDesc(verifiedEmailOrMobileToken);
	}

	@Transactional(readOnly = true)
	public MobileEmailVerification findFirst1ByMobileOrderByCreatedAtDesc(String phoneNumber) {
		return mobileEmailVerificationRepo.findFirst1ByMobileOrderByCreatedAtDesc(phoneNumber);
	}

	@Transactional(readOnly = true)
	public MobileEmailVerification findFirst1ByEmailOrderByCreatedAtDesc(String email) {
		return mobileEmailVerificationRepo.findFirst1ByEmailOrderByCreatedAtDesc(email);
	}

	@Transactional
	public void save(MobileEmailVerification verification) {
		mobileEmailVerificationRepo.save(verification);
	}
	
}

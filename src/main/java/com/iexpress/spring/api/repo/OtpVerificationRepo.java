package com.iexpress.spring.api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iexpress.spring.domain.OtpVerification;

@Repository
public interface OtpVerificationRepo extends JpaRepository<OtpVerification, Integer>{

	@Query("Select o from OtpVerification o where o.user.id = ?1 and o.otp = ?2")
	OtpVerification findByUserIdAndOtp(Integer userId, String otp);

	@Query("Select o from OtpVerification o where o.user.id = ?1")
	List<OtpVerification> findByUserId(int id);

	OtpVerification findFirst1ByMobileOrderByCreatedAtDesc(String mobile);

	@Query("Select o from OtpVerification o where o.randomeToken = ?1 and o.user.id = ?2")
	OtpVerification findByUserIdAndRadomeToken(String randomeToken, int userId);

	OtpVerification findFirst1ByEmailOrderByCreatedAtDesc(String email);
	
}

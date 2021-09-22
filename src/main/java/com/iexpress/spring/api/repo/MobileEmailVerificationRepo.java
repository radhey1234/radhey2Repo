package com.iexpress.spring.api.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.iexpress.spring.domain.MobileEmailVerification;

@Repository
public interface MobileEmailVerificationRepo extends JpaRepository<MobileEmailVerification, Integer>{

	MobileEmailVerification findFirst1ByMobileOrderByCreatedAtDesc(String phoneNumber);

	MobileEmailVerification findFirst1ByRandomeTokenOrderByCreatedAtDesc(String verifiedEmailOrMobileToken);

	MobileEmailVerification findFirst1ByEmailOrderByCreatedAtDesc(String email);

}

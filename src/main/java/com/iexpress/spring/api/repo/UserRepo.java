package com.iexpress.spring.api.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iexpress.spring.domain.User;

@Repository
public interface UserRepo extends JpaRepository<User, Integer>{

	@Query("Select u from User u where u.mobile = ?1")
	User findByPhoneNumber(String phoneNumber);

	@Query("Select u from User u where u.mobile = ?1")
	Optional<User> findUserByPhoneNumber(String phoneNumber);
	
	@Query("Select u from User u where u.email = ?1")
	User findByEmail(String email);

	@Query("Select u from User u where u.email = ?1")
	Optional<User> findUserByEmail(String email);

	
}

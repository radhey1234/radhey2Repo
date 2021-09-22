package com.iexpress.spring.api.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iexpress.spring.domain.Profile;

@Repository
public interface ProfileRepo extends JpaRepository<Profile, Integer> {

	@Query("Select p from Profile p where p.firstName like ?1 or "
			+ "p.lastName like ?1 or p.userName like ?1 ")
	Page<Profile> findUserByName(String key, Pageable pageable);

}

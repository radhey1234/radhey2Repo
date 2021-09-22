package com.iexpress.spring.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.iexpress.spring.domain.LoginDetail;

@Repository
public interface LoginDetailServiceRepo extends JpaRepository<LoginDetail, Integer> {

}

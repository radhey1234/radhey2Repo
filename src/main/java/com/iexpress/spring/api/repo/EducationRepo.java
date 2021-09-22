package com.iexpress.spring.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iexpress.spring.domain.Education;

@Repository
public interface EducationRepo extends JpaRepository<Education, Integer>{

}

package com.iexpress.spring.api.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.iexpress.spring.domain.MasterRole;

@Repository
public interface MasterRoleRepo extends JpaRepository<MasterRole, Integer>{

}

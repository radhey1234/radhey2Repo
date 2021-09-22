package com.iexpress.spring.api.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iexpress.spring.domain.State;

@Repository
public interface StateRepo extends JpaRepository<State, Integer> {

	@Query("Select s from State s where s.name like ?1 group by s.name")
	List<State> findAll(String stateChar);

	@Query(value= "Select * from state s  where s.name like ?1 ", nativeQuery =  true)
	List<State> findAllByState(String stateChar);
}

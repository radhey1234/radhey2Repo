package com.iexpress.spring.api.repo;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.iexpress.spring.domain.City;

@Repository
public interface CityRepo extends JpaRepository<City, Integer>{

	@Query("Select s from City s join fetch s.state where s.cityName like ?1 ")
	List<City> findAll(String cityChar);
}

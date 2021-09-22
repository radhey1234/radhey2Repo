package com.iexpress.spring.api.city.service;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.iexpress.spring.api.model.builder.CityModelBuilder;
import com.iexpress.spring.api.model.builder.StateModelBuilder;
import com.iexpress.spring.api.repo.CityRepo;
import com.iexpress.spring.api.repo.StateRepo;
import com.iexpress.spring.domain.City;
import com.iexpress.spring.domain.CityModel;
import com.iexpress.spring.domain.State;
import com.iexpress.spring.domain.StateModel;

@Service
public class CityAndStateService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CityAndStateService.class);
	
	@Autowired private StateRepo stateRepo;
	@Autowired private CityRepo listCity;
	
	@Transactional(readOnly = true)
	public List<State> listStates(){
		LOGGER.info("Inside listStates");
		return stateRepo.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<State> listStates(String stateChar){
		LOGGER.info("Inside listStates");
		return stateRepo.findAllByState("%"+stateChar+"%");
	}

	@Transactional(readOnly = true)
	public List<City> listCities(){
		LOGGER.info("Inside listCities");
		return listCity.findAll();
	}
	
	@Transactional(readOnly = true)
	public List<City> listCities(String cityChar){
		LOGGER.info("Inside listCities");
		return listCity.findAll("%"+cityChar+"%");
	}
	
	
	@Transactional(readOnly = true)
	public List<StateModel> getStates(final String stateChar){
		List<State> states = null;
		
		if(null == stateChar || stateChar.isEmpty()) {
			states = listStates();
		}
		else {
			states = listStates(stateChar);
		}
		return  states.stream().map(StateModelBuilder.of() :: state).map(StateModelBuilder :: build).
			collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<CityModel> getCities(final String cityChar){
		List<City> cities = null;
		
		if(null == cityChar || cityChar.isEmpty())
			cities = listCities();
		else 
			cities = listCities(cityChar);
		
		return CityModelBuilder.of().cities(cities).build();
	}

}

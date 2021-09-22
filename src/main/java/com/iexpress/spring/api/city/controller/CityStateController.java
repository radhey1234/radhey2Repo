package com.iexpress.spring.api.city.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.iexpress.spring.api.city.service.CityAndStateService;
import com.iexpress.spring.api.response.ResponseEnvelop;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.api.util.RestConstant;
import com.iexpress.spring.domain.CityModel;
import com.iexpress.spring.domain.StateModel;

@RestController
@RequestMapping("/api/v1/")
public class CityStateController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CityStateController.class);
	
	@Autowired private CityAndStateService cityAndStateService;
	
	@GetMapping(value= "state")
	public ResponseEnvelop<List<StateModel>> listState( @RequestParam("key") String key){
		LOGGER.info("Inside listState");
		return new ResponseEnvelop<List<StateModel>>(cityAndStateService.getStates(key), 
				AppUtil.of().getEnvProperty(RestConstant.RESPONSE_SUCCESSFULL),RestConstant.OK);
	}

	@GetMapping(value= "city")
	public ResponseEnvelop<List<CityModel>> listCity(@RequestParam("key") String key){
		LOGGER.info("Inside listCity");
		return new ResponseEnvelop<List<CityModel>>(cityAndStateService.getCities(key), 
				AppUtil.of().getEnvProperty(RestConstant.RESPONSE_SUCCESSFULL),RestConstant.OK);
	}

}

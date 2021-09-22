package com.iexpress.spring.api.model.builder;

import java.util.List;
import java.util.stream.Collectors;

import com.iexpress.spring.domain.City;
import com.iexpress.spring.domain.CityModel;

public class CityModelBuilder {
		
	private List<City> cities;
	
	public static CityModelBuilder of() {
		return new CityModelBuilder();
	}

	public CityModelBuilder cities(List<City> cities) {
		this.cities = cities;
		return this;
	}

	public List<CityModel> build() {
		return cities.stream().map(c -> new CityModel(c.getId(), c.getCityName(), c.getState().getName())).collect(Collectors.toList());
	}
}

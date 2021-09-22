package com.iexpress.spring.domain;

import lombok.Getter;

@Getter
public class CityModel {
	
	private final String state;
	private final String city;
	private final int id;

	public CityModel(int id, String city, String state) {
		this.city = city;
		this.id = id;
		this.state = state;
	}
	
}

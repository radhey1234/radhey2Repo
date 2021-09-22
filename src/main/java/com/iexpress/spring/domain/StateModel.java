package com.iexpress.spring.domain;

import lombok.Getter;

@Getter
public class StateModel {
	
	private final String name;
	private final int id;
		
	public StateModel(int id, String name) {
		this.name = name;
		this.id = id;
	}
	
}

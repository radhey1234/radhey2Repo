package com.iexpress.spring.api.model.builder;

import com.iexpress.spring.domain.State;
import com.iexpress.spring.domain.StateModel;

public class StateModelBuilder {

	private String name;
	private int id ;
	
	private StateModelBuilder() {}
	
	public StateModelBuilder state(State state) {
		this.name = state.getName();
		this.id = state.getId();
		return this;
	}
	
	public StateModelBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	public static StateModelBuilder of() {
		return new StateModelBuilder();
	}
	
	public StateModel build() {
		return new StateModel(id, name);
	}
}

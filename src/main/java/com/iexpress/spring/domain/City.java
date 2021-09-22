package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the city database table.
 * 
 */
@Entity
@Table(name = "city")
public class City extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="city_name")
	private String cityName;

	//bi-directional many-to-one association to State
	@ManyToOne
	private State state;

	public City() {
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public State getState() {
		return this.state;
	}

	public void setState(State state) {
		this.state = state;
	}

}
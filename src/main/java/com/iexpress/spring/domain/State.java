package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the state database table.
 * 
 */
@Entity
@Table(name = "state")
public class State extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	private String name;

	//bi-directional many-to-one association to City
	@OneToMany(mappedBy="state", fetch = FetchType.EAGER)
	private List<City> cities;

	public State() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<City> getCities() {
		return this.cities;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public City addCity(City city) {
		getCities().add(city);
		city.setState(this);

		return city;
	}

	public City removeCity(City city) {
		getCities().remove(city);
		city.setState(null);

		return city;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof State)) return false;
        
        State that = (State) o;
        return name.equals(that.name);
    }

	@Override
	public int hashCode() {
		return id;
	}
	
    @Override
    public String toString() {
        return
                "id=" + id;
    }

}
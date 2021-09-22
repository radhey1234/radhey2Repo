package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the tracking database table.
 * 
 */
@Entity
@Table(name="tracking")
@NamedQuery(name="Tracking.findAll", query="SELECT t FROM Tracking t")
public class Tracking extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public Tracking() {
	}

}
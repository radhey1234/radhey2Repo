package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ads database table.
 * 
 */
@Entity
@Table(name="ads")
@NamedQuery(name="Ad.findAll", query="SELECT a FROM Ad a")
public class Ad  extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public Ad() {
	}

}
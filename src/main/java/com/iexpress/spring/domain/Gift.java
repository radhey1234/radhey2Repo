package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the gift database table.
 * 
 */
@Entity
@Table(name="gift")
@NamedQuery(name="Gift.findAll", query="SELECT g FROM Gift g")
public class Gift extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	public Gift() {
	}

}
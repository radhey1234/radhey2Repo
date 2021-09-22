package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the user_stat database table.
 * 
 */
@Entity
@Table(name="user_stat")
@NamedQuery(name="UserStat.findAll", query="SELECT u FROM UserStat u")
public class UserStat extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	public UserStat() {
	}

}
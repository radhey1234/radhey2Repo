package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the master_role database table.
 * 
 */
@Entity
@Table(name="master_role")
@NamedQuery(name="MasterRole.findAll", query="SELECT m FROM MasterRole m")
public class MasterRole extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	@Column(nullable=false, length=45)
	private String name;

	//bi-directional many-to-one association to User
	@OneToMany(mappedBy="masterRole")
	private List<User> users;

	public MasterRole() {
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public User addUser(User user) {
		getUsers().add(user);
		user.setMasterRole(this);
		return user;
	}

	public User removeUser(User user) {
		getUsers().remove(user);
		user.setMasterRole(null);
		return user;
	}

}
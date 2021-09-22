package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the login_detail database table.
 * 
 */
@Entity
@Table(name="login_detail")
@NamedQuery(name="LoginDetail.findAll", query="SELECT l FROM LoginDetail l")
public class LoginDetail extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="login_at", nullable=false)
	private Date loginAt;

	@Column(name="login_by")
	@Enumerated(EnumType.STRING)
	private LogInBy loginBy;

	@Column(name="login_ip", length=95)
	private String loginIp;

	@Column(name="login_plateform", nullable=false, length=100)
	private String loginPlateform;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="logout_at")
	private Date logoutAt;

	@Column(length=95)
	private String timezone;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;

	public LoginDetail() {
	}

	public Date getLoginAt() {
		return this.loginAt;
	}

	public void setLoginAt(Date loginAt) {
		this.loginAt = loginAt;
	}

	public LogInBy getLoginBy() {
		return this.loginBy;
	}

	public void setLoginBy(LogInBy loginBy) {
		this.loginBy = loginBy;
	}

	public String getLoginIp() {
		return this.loginIp;
	}

	public void setLoginIp(String loginIp) {
		this.loginIp = loginIp;
	}

	public String getLoginPlateform() {
		return this.loginPlateform;
	}

	public void setLoginPlateform(String loginPlateform) {
		this.loginPlateform = loginPlateform;
	}

	public Date getLogoutAt() {
		return this.logoutAt;
	}

	public void setLogoutAt(Date logoutAt) {
		this.logoutAt = logoutAt;
	}

	public String getTimezone() {
		return this.timezone;
	}

	public void setTimezone(String timezone) {
		this.timezone = timezone;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
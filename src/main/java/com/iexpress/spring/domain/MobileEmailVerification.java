package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;

import com.iexpress.spring.api.util.RestConstant;

/**
 * The persistent class for the mobile_email_verification database table.
 * 
 */
@Entity
@Table(name="mobile_email_verification")
@NamedQuery(name="MobileEmailVerification.findAll", query="SELECT m FROM MobileEmailVerification m")
public class MobileEmailVerification extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private String email;
	private String mobile;
	private String otp;
	private String randomeToken;
	private String status;

	public MobileEmailVerification() {
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOtp() {
		return this.otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getRandomeToken() {
		return this.randomeToken;
	}

	public void setRandomeToken(String randomeToken) {
		this.randomeToken = randomeToken;
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isVerificatioinTokenVerified() {
		return getStatus().equals(RestConstant.ACTIVE);
	}
}
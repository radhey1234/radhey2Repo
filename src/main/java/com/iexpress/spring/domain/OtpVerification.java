package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the otp_verification database table.
 * 
 */
@Entity
@Table(name="otp_verification")
@NamedQuery(name="OtpVerification.findAll", query="SELECT o FROM OtpVerification o")
public class OtpVerification extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(length=95)
	private String email;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="expired_at")
	private Date expiredAt;

	@Column(name="is_used")
	private boolean isUsed;

	@Column(length=95)
	private String mobile;

	@Column(length=45)
	private String otp;

	@Column(name="randome_token", length=95)
	private String randomeToken;

	@Column(name="country_code")
	private String countryCode;


	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public void setUsed(boolean isUsed) {
		this.isUsed = isUsed;
	}

	@Basic
    @Enumerated(EnumType.STRING)
	private VerificationStatus status;
	
	@Basic
	@Enumerated(EnumType.STRING)
	private VerificationReason reason;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;

	public OtpVerification() {
	}

	public VerificationStatus getStatus() {
		return this.status;
	}

	public void setStatus(VerificationStatus status) {
		this.status = status;
	}
	
	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getExpiredAt() {
		return this.expiredAt;
	}

	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}

	public boolean getIsUsed() {
		return this.isUsed;
	}

	public void setIsUsed(boolean isUsed) {
		this.isUsed = isUsed;
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

	public VerificationReason getReason() {
		return this.reason;
	}

	public void setReason(VerificationReason reason) {
		this.reason = reason;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
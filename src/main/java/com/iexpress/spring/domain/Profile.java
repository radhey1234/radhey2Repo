package com.iexpress.spring.domain;


import static com.iexpress.spring.api.util.RestConstant.DEFAULT_PROFILE_BIO;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;


/**
 * The persistent class for the profile database table.
 * 
 */
@Entity
@Table(name="profile")
@NamedQuery(name="Profile.findAll", query="SELECT p FROM Profile p")
public class Profile extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(length=95)
	private String city;

	@Column(length=95)
	private String country;

	@Lob
	private String dob;

	@Column(name="first_name", length=98)
	private String firstName;

	@Column(name="is_deleted")
	private boolean isDeleted;

	@Column(name="last_name", length=98)
	private String lastName;

	@Lob
	@Column(name="profile_bio")
	private String profileBio;

	@Column(name="profile_pic", length=95)
	private String profilePic;

	@Column(length=95)
	private String state;

	@Column(name="user_name", length=98)
	private String userName;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;

	public Profile() {
	}

	public String getCity() {
		return this.city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return this.country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Date getCreatedAt() {
		return this.createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public String getDob() {
		return this.dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getProfileBio() {
		return this.profileBio;
	}

	public void setProfileBio(String profileBio) {
		this.profileBio = profileBio;
	}

	public String getProfilePic() {
		return this.profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	
	public static Profile of(String fullName, String city, String state, String dob, String userName, String profileBio) {
		
		Profile profile = new Profile();
		String firstName = null;
		String lastName= null;
		
		if(!StringUtils.isEmpty(fullName)){
			int idx = fullName.lastIndexOf(' ');
			if (idx == -1) {
			}else {
			 firstName = fullName.substring(0, idx);
			 lastName  = fullName.substring(idx + 1);
			}
		}
		profile.setFirstName(firstName);
		profile.setLastName(lastName);
		profile.setCity(city);
		profile.setState(state);
		profile.setDob(dob);
		profile.setUserName(userName);

		if(StringUtils.isEmpty(profileBio)) {
			profileBio = DEFAULT_PROFILE_BIO;
		}
		profile.setProfileBio(profileBio);
		return profile;
	}

}
package com.iexpress.spring.domain;

import static com.iexpress.spring.api.util.RestConstant.ACTIVE;

import java.io.Serializable;
import javax.persistence.*;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the user database table.
 * 
 */
@Entity
@Table(name="user")
@NamedQuery(name="User.findAll", query="SELECT u FROM User u")
public class User extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="country_code", length=45)
	private String countryCode;

	@Column(length=95)
	private String email;

	@Basic
	@Enumerated(EnumType.STRING)
	@Column(name = "email_status")
	private EmailStatus emailStatus;

	@Column(name="fb_id", length=98)
	private String fbId;

	@Column(name="gmail_id", length=98)
	private String gmailId;

	@Column(name="is_active")
	private boolean isActive;

	@Column(name="is_deleted")
	private boolean isDeleted;

	@Column(name="is_social")
	private boolean isSocial;

	@Column(name="is_tc_accepted")
	private boolean isTcAccepted;

	@Column(precision=10, scale=8)
	private Double lat;

	@Column(length=98)
	private String location;

	@Column(precision=10, scale=8)
	private Double lon;

	private String mobile;

	@Basic
    @Enumerated(EnumType.STRING)
	@Column(name = "mobile_status")
	private MobileStatus mobileStatus;

	@Column
	@Lob
	private String password;

	@Column(name="twitter_id", length=98)
	private String twitterId;

	@Column(name="user_name", length=95)
	private String userName;

	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="user")
	private List<Comment> comments;

	//bi-directional many-to-one association to DeviceDetail
	@OneToMany(mappedBy="user",cascade = CascadeType.ALL)
	private List<DeviceDetail> deviceDetails;

	//bi-directional many-to-one association to Education
	@OneToMany(mappedBy="user")
	private List<Education> educations;

	//bi-directional many-to-one association to LoginDetail
	@OneToMany(mappedBy="user")
	private List<LoginDetail> loginDetails;

	//bi-directional many-to-one association to Notification
	@OneToMany(mappedBy="user1")
	private List<Notification> notifications1;

	//bi-directional many-to-one association to Notification
	@OneToMany(mappedBy="user2")
	private List<Notification> notifications2;

	//bi-directional many-to-one association to OtpVerification
	@OneToMany(mappedBy="user")
	private List<OtpVerification> otpVerifications;

	//bi-directional many-to-one association to Post
	@OneToMany(mappedBy="creator")
	private List<Post> posts1;

	//bi-directional many-to-one association to Post
	@OneToMany(mappedBy="taggedUser")
	private List<Post> posts2;

	//bi-directional many-to-one association to PostSeen
	@OneToMany(mappedBy="user")
	private List<PostSeen> postSeens;

	//bi-directional many-to-one association to Profile
	@OneToMany(mappedBy="user", cascade = CascadeType.ALL)
	private List<Profile> profiles;

	//bi-directional many-to-one association to Question
	@OneToMany(mappedBy="user")
	private List<Question> questions;

	//bi-directional many-to-one association to Resource
	@OneToMany(mappedBy="user")
	private List<Resource> resources;

	//bi-directional many-to-one association to MasterRole
	@ManyToOne
	@JoinColumn(name="role", nullable=false)
	private MasterRole masterRole;

	public User() {
	}

	public String getCountryCode() {
		return this.countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public EmailStatus getEmailStatus() {
		return this.emailStatus;
	}

	public void setEmailStatus(EmailStatus emailStatus) {
		this.emailStatus = emailStatus;
	}

	public String getFbId() {
		return this.fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	public String getGmailId() {
		return this.gmailId;
	}

	public void setGmailId(String gmailId) {
		this.gmailId = gmailId;
	}

	public boolean getIsActive() {
		return this.isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean getIsSocial() {
		return this.isSocial;
	}

	public void setIsSocial(boolean isSocial) {
		this.isSocial = isSocial;
	}

	public boolean getIsTcAccepted() {
		return this.isTcAccepted;
	}

	public void setIsTcAccepted(boolean isTcAccepted) {
		this.isTcAccepted = isTcAccepted;
	}

	public Double getLat() {
		return this.lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public String getLocation() {
		return this.location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Double getLon() {
		return this.lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public MobileStatus getMobileStatus() {
		return this.mobileStatus;
	}

	public void setMobileStatus(MobileStatus mobileStatus) {
		this.mobileStatus = mobileStatus;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTwitterId() {
		return this.twitterId;
	}

	public void setTwitterId(String twitterId) {
		this.twitterId = twitterId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setUser(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setUser(null);

		return comment;
	}

	public List<DeviceDetail> getDeviceDetails() {
		return this.deviceDetails;
	}

	public void setDeviceDetails(List<DeviceDetail> deviceDetails) {
		this.deviceDetails = deviceDetails;
	}

	public DeviceDetail addDeviceDetail(DeviceDetail deviceDetail) {
		if(getDeviceDetails() == null) {
			setDeviceDetails(new ArrayList<DeviceDetail>());
		}
		getDeviceDetails().add(deviceDetail);
		deviceDetail.setUser(this);
		return deviceDetail;
	}

	public DeviceDetail removeDeviceDetail(DeviceDetail deviceDetail) {
		getDeviceDetails().remove(deviceDetail);
		deviceDetail.setUser(null);

		return deviceDetail;
	}

	public List<Education> getEducations() {
		return this.educations;
	}

	public void setEducations(List<Education> educations) {
		this.educations = educations;
	}

	public Education addEducation(Education education) {
		getEducations().add(education);
		education.setUser(this);

		return education;
	}

	public Education removeEducation(Education education) {
		getEducations().remove(education);
		education.setUser(null);

		return education;
	}

	public List<LoginDetail> getLoginDetails() {
		return this.loginDetails;
	}

	public void setLoginDetails(List<LoginDetail> loginDetails) {
		this.loginDetails = loginDetails;
	}

	public LoginDetail addLoginDetail(LoginDetail loginDetail) {
		getLoginDetails().add(loginDetail);
		loginDetail.setUser(this);

		return loginDetail;
	}

	public LoginDetail removeLoginDetail(LoginDetail loginDetail) {
		getLoginDetails().remove(loginDetail);
		loginDetail.setUser(null);

		return loginDetail;
	}

	public List<Notification> getNotifications1() {
		return this.notifications1;
	}

	public void setNotifications1(List<Notification> notifications1) {
		this.notifications1 = notifications1;
	}

	public Notification addNotifications1(Notification notifications1) {
		getNotifications1().add(notifications1);
		notifications1.setUser1(this);

		return notifications1;
	}

	public Notification removeNotifications1(Notification notifications1) {
		getNotifications1().remove(notifications1);
		notifications1.setUser1(null);

		return notifications1;
	}

	public List<Notification> getNotifications2() {
		return this.notifications2;
	}

	public void setNotifications2(List<Notification> notifications2) {
		this.notifications2 = notifications2;
	}

	public Notification addNotifications2(Notification notifications2) {
		getNotifications2().add(notifications2);
		notifications2.setUser2(this);

		return notifications2;
	}

	public Notification removeNotifications2(Notification notifications2) {
		getNotifications2().remove(notifications2);
		notifications2.setUser2(null);

		return notifications2;
	}

	public List<OtpVerification> getOtpVerifications() {
		return this.otpVerifications;
	}

	public void setOtpVerifications(List<OtpVerification> otpVerifications) {
		this.otpVerifications = otpVerifications;
	}

	public OtpVerification addOtpVerification(OtpVerification otpVerification) {
		getOtpVerifications().add(otpVerification);
		otpVerification.setUser(this);

		return otpVerification;
	}

	public OtpVerification removeOtpVerification(OtpVerification otpVerification) {
		getOtpVerifications().remove(otpVerification);
		otpVerification.setUser(null);

		return otpVerification;
	}

	public List<Post> getPosts1() {
		return this.posts1;
	}

	public void setPosts1(List<Post> posts1) {
		this.posts1 = posts1;
	}

	public Post addPosts1(Post posts1) {
		getPosts1().add(posts1);
		posts1.setCreator(this);

		return posts1;
	}

	public Post removePosts1(Post posts1) {
		getPosts1().remove(posts1);
		posts1.setCreator(null);

		return posts1;
	}

	public List<Post> getPosts2() {
		return this.posts2;
	}

	public void setPosts2(List<Post> posts2) {
		this.posts2 = posts2;
	}

	public Post addPosts2(Post posts2) {
		getPosts2().add(posts2);
		posts2.setTaggedUser(this);

		return posts2;
	}

	public Post removePosts2(Post posts2) {
		getPosts2().remove(posts2);
		posts2.setTaggedUser(null);

		return posts2;
	}

	public List<PostSeen> getPostSeens() {
		return this.postSeens;
	}

	public void setPostSeens(List<PostSeen> postSeens) {
		this.postSeens = postSeens;
	}

	public PostSeen addPostSeen(PostSeen postSeen) {
		getPostSeens().add(postSeen);
		postSeen.setUser(this);

		return postSeen;
	}

	public PostSeen removePostSeen(PostSeen postSeen) {
		getPostSeens().remove(postSeen);
		postSeen.setUser(null);

		return postSeen;
	}

	public List<Profile> getProfiles() {
		return this.profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

	public Profile addProfile(Profile profile) {
		if(getProfiles() == null) {
			setProfiles(new ArrayList<>());
		}
		getProfiles().add(profile);
		profile.setUser(this);

		return profile;
	}

	public Profile removeProfile(Profile profile) {
		getProfiles().remove(profile);
		profile.setUser(null);

		return profile;
	}

	public List<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Question addQuestion(Question question) {
		getQuestions().add(question);
		question.setUser(this);

		return question;
	}

	public Question removeQuestion(Question question) {
		getQuestions().remove(question);
		question.setUser(null);

		return question;
	}

	public List<Resource> getResources() {
		return this.resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public Resource addResource(Resource resource) {
		getResources().add(resource);
		resource.setUser(this);

		return resource;
	}

	public Resource removeResource(Resource resource) {
		getResources().remove(resource);
		resource.setUser(null);

		return resource;
	}

	public MasterRole getMasterRole() {
		return this.masterRole;
	}

	public void setMasterRole(MasterRole masterRole) {
		this.masterRole = masterRole;
	}

	public boolean isPhoneNumberVerified() {
		if(this.getMobileStatus() == null) {
			return false;
		}
		return ACTIVE.equalsIgnoreCase(this.getMobileStatus().name());
	}

	public boolean isEmailVerified() {
		
		if(this.getEmailStatus() == null) {
			return false;
		}
		return ACTIVE.equalsIgnoreCase(this.getEmailStatus().name());
	}

	public static User of(String countryCode, String phoneNumber, String email, String userName, 
			String password, MasterRole userRole, DeviceDetail device, Profile profile, boolean isVerificationDone) {
		User user = new User();
		user.setEmail(email);
		user.setCreatedAt(new Date());
		user.setModifiedAt(new Date());
		user.setPassword(password);
		user.setCountryCode(countryCode);
		user.setMobile(phoneNumber);
		user.setUserName(userName);
		
		if(!StringUtils.isEmpty(phoneNumber)) {
			if(isVerificationDone) 
				user.activateMobile();
			else
				user.setMobileStatus(MobileStatus.PENDING);
		}
		if(!StringUtils.isEmpty(email)) {
			if(isVerificationDone)
				user.activateEmail();
			else
				user.setEmailStatus(EmailStatus.PENDING);
		}
		
		user.setMasterRole(userRole);
		user.addDeviceDetail(device);
		user.addProfile(profile);
		return user;
	}

	public void activateMobile() {
		setMobileStatus(MobileStatus.ACTIVE);		
	}

	public void activateEmail() {
		setEmailStatus(EmailStatus.ACTIVE);
	}
	
	public DeviceDetail getActiveDeviceDetail() {
		return this.getDeviceDetails().stream().findFirst().get();
	}

	public DeviceDetail addDeviceDetail(String deviceToken, String deviceType) {
		return addDeviceDetail(DeviceDetail.of(deviceToken, deviceType));
	}
}
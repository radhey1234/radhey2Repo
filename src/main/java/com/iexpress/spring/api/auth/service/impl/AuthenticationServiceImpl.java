package com.iexpress.spring.api.auth.service.impl;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iexpress.spring.api.auth.service.AuthenticationService;
import com.iexpress.spring.api.auth.service.LoginDetailService;
import com.iexpress.spring.api.device.service.DeviceDetailService;
import com.iexpress.spring.api.exception.GenericException;
import com.iexpress.spring.api.form.ChangeOrResetPasswordForm;
import com.iexpress.spring.api.form.RegisterForm;
import com.iexpress.spring.api.form.SignUpForm;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.otp.service.OtpService;
import com.iexpress.spring.api.otp.service.impl.MobileOrEmailVerificationService;
import com.iexpress.spring.api.profile.service.ProfileService;
import com.iexpress.spring.api.repo.CityRepo;
import com.iexpress.spring.api.repo.MasterRoleRepo;
import com.iexpress.spring.api.repo.MobileEmailVerificationRepo;
import com.iexpress.spring.api.user.service.UserService;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.api.util.MailService;

import static com.iexpress.spring.api.util.RestConstant.*;
import com.iexpress.spring.api.util.SmsServiceUtil;
import com.iexpress.spring.domain.City;
import com.iexpress.spring.domain.DeviceDetail;
import com.iexpress.spring.domain.LogInBy;
import com.iexpress.spring.domain.LoginDetail;
import com.iexpress.spring.domain.MasterRole;
import com.iexpress.spring.domain.MobileEmailVerification;
import com.iexpress.spring.domain.OtpVerification;
import com.iexpress.spring.domain.Profile;
import com.iexpress.spring.domain.User;
import com.iexpress.spring.jwt.JwtUser;
import static com.iexpress.spring.api.util.ExceptionUtility.*;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationServiceImpl.class);

	@Autowired private MasterRoleRepo masterRoleRepo;
	@Autowired private DeviceDetailService deviceDetailService;
	@Autowired private SmsServiceUtil smsService;
	@Autowired private MailService mailService;
	@Autowired @Qualifier("passwordEncoderBean")PasswordEncoder passwordEncoader;
	@Autowired private UserService userService;
	@Autowired private OtpService otpService;
	@Autowired private MobileEmailVerificationRepo mobileEmailVerificationRepo;
	@Autowired private MobileOrEmailVerificationService mobileOrEmailVerificationService;
	@Autowired private LoginDetailService loginDetailService;
	@Autowired CityRepo cityRepo;
	@Autowired ProfileService profileService;

	@Override
	@Transactional
	public UserModel signInByPhone(String countryCode, String phoneNumber, String password, String deviceType,String deviceToken) {
		LOGGER.info("Inside signUpWithPhone " + phoneNumber);
		final User userExist = userService.authenticateByPhoneNumber(phoneNumber, password);// to be changed to include country code
		deviceDetailService.saveDeviceDetail(userExist.addDeviceDetail(deviceToken, deviceType));
		addLoginDetailsForRecords(LogInBy.MOBILE, userExist);
		return userService.buildUserModel(userExist, true);
	}

	@Override
	@Transactional
	public UserModel signInByEmail(String email, String password, String deviceType, String deviceToken) {
		LOGGER.info("Inside signInWithEmail " + email);
		final User userExist = userService.authenticateByEmail(email, password);
		deviceDetailService.saveDeviceDetail(userExist.addDeviceDetail(deviceToken, deviceType));
		addLoginDetailsForRecords(LogInBy.EMAIL, userExist);
		return userService.buildUserModel(userExist, true);
	}

	@Override
	@Transactional
	public UserModel signUpByPhone(SignUpForm signUpForm) {
		LOGGER.info("Inside signoUpWithPhone " + signUpForm.getPhoneNumber());
			
		userService.throwExceptionIfPhoneExist(signUpForm.getPhoneNumber());
		
		User newUser = createNewUser(signUpForm);		
		
		if (isPhoneVerificationDone(newUser)) {
			addLoginDetailsForRecords(LogInBy.MOBILE, newUser);
			return  buildUserModelWithAuth(newUser);
		}
		
		sendAndSaveOTPToMobile(newUser);
		return buildUserModelWithoutAuth(newUser);
	}

	@Override
	@Transactional
	public UserModel signUpByEmail(SignUpForm signUpForm) {
		LOGGER.info("Inside signUpWithEmail ", signUpForm.getEmail());
		userService.throwExceptionIfEmailExist(signUpForm.getEmail());

		User newUser = createNewUser(signUpForm);
		
		if(isEmailVerificationDone(newUser)) {
			addLoginDetailsForRecords(LogInBy.EMAIL, newUser);
			return buildUserModelWithAuth(newUser);
		}
		
		sendAndSaveOTP(newUser);
		return buildUserModelWithoutAuth(newUser);
	}


	@Transactional(readOnly = true)
	public MasterRole getUserRole() {
		return masterRoleRepo.getOne(1);
	}

	@Override
	@Transactional
	public UserModel changePassword(int userId, String currentPassword, String newPassword, String confirmPassword) {
		LOGGER.info("Inside changed password");
		final User user = getUserById(userId);

		if (!passwordEncoader.matches(currentPassword, user.getPassword())) {
			LOGGER.info("Current Password is incorrect ");
			throw new GenericException("incorrect.current.password", UNAUTHENTICATED);
		}

		if (!newPassword.equalsIgnoreCase(confirmPassword)) {
			LOGGER.info("New and confirm password does not match");
			throw new GenericException("not.match.new.confirm.password", UNAUTHENTICATED);
		}

		user.setPassword(passwordEncoader.encode(newPassword));
		userService.save(user);
		LOGGER.info("Password changed successfully !");
		return userService.buildUserModel(user, true);
	}

	@Override
	public UserModel resetPassword(ChangeOrResetPasswordForm changeOrResetPasswordForm) {
		LOGGER.info(" Inside resetPassword Input Type ", changeOrResetPasswordForm.getInputType());

		if (!changeOrResetPasswordForm.getNewPassword()
				.equalsIgnoreCase(changeOrResetPasswordForm.getConfirmPassword())) {
			LOGGER.info("New and confirm password does not match");
			throw new GenericException("not.match.new.confirm.password", UNAUTHENTICATED);
		}

		User user = null;
		if (changeOrResetPasswordForm.getInputType().equalsIgnoreCase(EMAIL)) {
			user = userService.checkIfEmailExist(changeOrResetPasswordForm.getEmail());

			if (!isValidVerificationToken(changeOrResetPasswordForm.getVerificationToken(), user)) {
				throw new GenericException("invalid.verification.token", UNAUTHENTICATED);
			}
		}

		if (changeOrResetPasswordForm.getInputType().equalsIgnoreCase(PHONE)) {
			user = userService.checkIfEmailExist(changeOrResetPasswordForm.getPhoneNumber());

			if (!isValidVerificationOtp(changeOrResetPasswordForm.getVerificationOtp(), user)) {
				throw new GenericException("invalid.verification.otp", UNAUTHENTICATED);
			}
		}

		user.setPassword(passwordEncoader.encode(changeOrResetPasswordForm.getNewPassword()));
		LOGGER.info("Password is set");
		return buildUserModelWithoutAuth(user);
	}

	@Transactional
	public void logOut(JwtUser user) {
		LOGGER.info("Inside logout user " + user.getId() + " device token " + user.getDeviceToken());
		User existingUser = userService.findById(user.getId());
		addLogoutDetailsForRecords(LogInBy.MOBILE, existingUser, existingUser.getDeviceDetails().get(0).getDeviceType(),new Date());
		deviceDetailService.deleteByUserIdAndDeviceToken(user.getId(), user.getDeviceToken());
		LOGGER.info("Logout successfully !");
	}

	@Override
	@Transactional
	public String register(@Valid RegisterForm registrationForm) {
		User userExist = null;

		if (!StringUtils.isEmpty(registrationForm.getEmail()))
			userExist = userService.loadEmail(registrationForm.getEmail());
		else
			userExist = userService.loadPhoneNumber(registrationForm.getPhoneNumber());

		if (null != userExist) {
			if (registrationForm.getEmail() == null)
				throwExistingPhoneNumberError();
			throwExistingEmailError();
		}

		String randomOtp = AppUtil.generateRandomNumericCode(AppUtil.OTP_LENGTH);
		String randomeToken = UUID.randomUUID().toString();

		MobileEmailVerification mobileEmailVerification = new MobileEmailVerification();

		if (!StringUtils.isEmpty(registrationForm.getPhoneNumber())) {
			
			if (isDemoNumber(registrationForm.getPhoneNumber())) {
				randomOtp = "4444";
			}
			
			sendOtp(registrationForm.getCountryCode(), registrationForm.getPhoneNumber(), randomOtp);
			mobileEmailVerification.setMobile(registrationForm.getPhoneNumber());
		} else {
			sendLinkOnEmail(registrationForm.getEmail(), randomOtp, 1);
			mobileEmailVerification.setEmail(registrationForm.getEmail());
		}

		mobileEmailVerification.setOtp(randomOtp);
		mobileEmailVerification.setRandomeToken(randomeToken);
		mobileEmailVerification.setStatus("Pending");
		mobileEmailVerificationRepo.save(mobileEmailVerification);
		return randomeToken;
	}


	@Override
	@Transactional(readOnly = true)
	public void overrideInputValuesIfRequiredFromVerificationToken(SignUpForm signUpForm) {
		signUpForm.setVerificationTokenVerified(false);
		MobileEmailVerification mobileEmailVerification = findVerifiedEmailOrMobileTokenDetail(signUpForm.getVerifiedMobileOrEmailToken());
		if (mobileEmailVerification.isVerificatioinTokenVerified()) {
		
			if(!StringUtils.isEmpty(mobileEmailVerification.getEmail())) {
				signUpForm.setEmail(mobileEmailVerification.getEmail());
				signUpForm.setVerificationTokenVerified(true);
			}else {
				signUpForm.setPhoneNumber(mobileEmailVerification.getMobile());
				signUpForm.setCountryCode("91");// will change , load if from db
				signUpForm.setVerificationTokenVerified(true);
			}
		}


		if(signUpForm.getCityId() !=0) {
			Optional<City> oCity = cityRepo.findById(signUpForm.getCityId());
			if(oCity.isPresent()) {
				signUpForm.setCity(oCity.get().getCityName());
				signUpForm.setState(oCity.get().getState().getName());
			}
		}
		
	}

	@Async
	public LoginDetail addLoginDetailsForRecords(LogInBy loginBy, User userExist) {
		return loginDetailService.addLoginDetailsForRecords(loginBy, userExist, userExist.getActiveDeviceDetail().getDeviceType(), new Date());
	}

	public LoginDetail addLogoutDetailsForRecords(LogInBy loginBy, User userExist, String deviceType, Date date) {
		return loginDetailService.addLogoutDetailsForRecords(loginBy, userExist, deviceType, date);
	}

	private boolean isEmailVerificationDone(User newUser) {
		return newUser.isEmailVerified();
	}
	
	private boolean isPhoneVerificationDone(User newUser) {
		return newUser.isPhoneNumberVerified();
	}
	
	private User createNewUser(SignUpForm signUpForm) {
		Profile newProfile = createProfile(signUpForm.getFullName(), signUpForm.getCity(), signUpForm.getState(), 
				signUpForm.getDob(), signUpForm.getProfileBio());
		
		DeviceDetail device = createDeviceDetail(signUpForm.getDeviceToken(), signUpForm.getDeviceType());
	 	User user = userService.save(createUser(signUpForm, getUserRole(),device, newProfile));
	 	return user;
	}

	private boolean isValidVerificationOtp(final String verificationOtp, final User user) {

		return user.getOtpVerifications().stream()
								  .filter( e-> e.getOtp().equalsIgnoreCase(verificationOtp))
								  .findAny().isPresent();
		/*
		 * List<OtpVerification> emailVerifications = user.getOtpVerifications(); for
		 * (OtpVerification emailVerification : emailVerifications) { if
		 * (verificationOtp.equalsIgnoreCase(emailVerification.getOtp())) { return true;
		 * } } return false;
		 */
	}

	private boolean isValidVerificationToken(String verificationToken, User user) {

		return user.getOtpVerifications().stream()
										 .filter( e-> e.getRandomeToken().equalsIgnoreCase(verificationToken))
										 .findAny().isPresent();
		/*
		 * List<OtpVerification> emailVerifications = user.getOtpVerifications(); for
		 * (OtpVerification emailVerification : emailVerifications) { if
		 * (verificationToken.equalsIgnoreCase(emailVerification.getRandomeToken())) {
		 * return true; } } return false;
		 */
	}

	private void sendLinkOnEmail(String email, String randomeToken, int userId) {
		mailService.sendMail(email, "Please click on this link to verify",
				" Link is " + AppUtil.of().getEnvProperty("based.address") + "/api/v1/verify_email?randomeToken="
						+ randomeToken + "&id=" + userId);
	}

	private void sendOtp(String countryCode, String phoneNumber, String randomOtp) {
		try {
			smsService.sendMessage(AppUtil.of().getEnvProperty(SMS_ACCOUNT_SID), AppUtil.of().getEnvProperty(SMS_AUTH_TOKEN),
					AppUtil.of().getEnvProperty(SMS_FROM_NUMBER), "+" + countryCode + phoneNumber,
					"This is test otp - " + randomOtp);
		} catch (Exception e) {
			LOGGER.error("Error while sending OTP ", e);
		}
	}

	private User getUserById(int userId) {
		return userService.findUserById(userId).get();
	}

	private UserModel buildUserModelWithoutAuth(User user) {
		return userService.buildUserModel(user, false);
	}

	private UserModel buildUserModelWithAuth(User user) {
		return userService.buildUserModel(user, true);
	}


	private MobileEmailVerification findVerifiedEmailOrMobileTokenDetail(String verifiedEmailOrMobileToken) {
		return mobileOrEmailVerificationService.findVerifiedEmailOrMobileTokenDetail(verifiedEmailOrMobileToken);
	}

	private DeviceDetail createDeviceDetail(String deviceToken, String deviceType) {
		return DeviceDetail.of(deviceToken, deviceType);
	}

	private User createUser(SignUpForm signUpForm,MasterRole userRole, DeviceDetail deviceDetail, Profile newProfile) {
		return User.of(signUpForm.getCountryCode(), signUpForm.getPhoneNumber(), signUpForm.getEmail(), signUpForm.getUserName()
				, passwordEncoader.encode(signUpForm.getPassword()), userRole,	deviceDetail, newProfile , signUpForm.isVerificationTokenVerified());
	}

	private Profile createProfile(String fullName, String city, String state, String dob, String defaultProfileBio) {
		return Profile.of(fullName, city, state, dob, fullName, defaultProfileBio);
	}

	private void sendAndSaveOTPToMobile(User newUser) {
		String randomOtp = getRandomeOTP();
		if (isDemoNumber(newUser.getMobile())) {
			randomOtp = "4444";
		}

		sendOtp(newUser.getCountryCode(), newUser.getMobile(), randomOtp);
		otpService.saveOtp(null, newUser.getMobile(), newUser.getCountryCode(), randomOtp, ACCOUNT_VERIFICATION, newUser);
	}

	private void sendAndSaveOTP(User newUser) {
		String randomOtp = getRandomeOTP(); 
		if (isDemoEmail(newUser.getEmail())) {
			randomOtp = "4444";
		}
		final  OtpVerification otp = otpService.saveOtp(newUser.getEmail(), null, null, randomOtp,  ACCOUNT_VERIFICATION, newUser);
		sendLinkOnEmail(newUser.getEmail(),  otp.getRandomeToken(), newUser.getId());
	}

	private String getRandomeOTP() {
		return  AppUtil.generateRandomNumericCode(AppUtil.OTP_LENGTH);
	}

	private boolean isDemoNumber(String phoneNumber) {
		return !StringUtils.isEmpty(phoneNumber) && phoneNumber.startsWith("44");
	}

	private boolean isDemoEmail(String email) {
		return !StringUtils.isEmpty(email) && email.startsWith("44");
	}

}

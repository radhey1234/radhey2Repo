package com.iexpress.spring.api.user.service;

import static com.iexpress.spring.api.util.AppUtil.isNull;

import static java.util.stream.Collectors.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iexpress.spring.api.exception.GenericException;
import com.iexpress.spring.api.model.ProfileModel;
import com.iexpress.spring.api.model.ResourceModel;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.model.builder.ProfileModelBuilder;
import com.iexpress.spring.api.model.builder.UserModelBuilder;
import com.iexpress.spring.api.profile.service.ProfileService;
import com.iexpress.spring.api.repo.DeviceDetailRepo;
import com.iexpress.spring.api.repo.ProfileRepo;
import com.iexpress.spring.api.repo.UserRepo;
import com.iexpress.spring.api.util.ExceptionUtility;
import com.iexpress.spring.api.util.RestConstant;
import com.iexpress.spring.domain.Profile;
import com.iexpress.spring.domain.User;
import com.iexpress.spring.jwt.JwtTokenUtil;
import com.iexpress.spring.jwt.JwtUser;

@Service
public class UserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
	private UserRepo userRepo;
	@Autowired	private JwtTokenUtil jwtTokenUtil;
	@Autowired @Qualifier("passwordEncoderBean") PasswordEncoder passwordEncoader;
	@Autowired ProfileRepo profileRepo;
	@Autowired DeviceDetailRepo deviceDetailRepo;
	@Autowired ProfileService profileService;
	
	@Autowired
	public UserService(UserRepo userRepo, ProfileRepo profileRepo) {
		this.userRepo = userRepo;
	}
	
	@Transactional(readOnly = true)
	public Optional<User> findUserById(int userId) {
		return userRepo.findById(userId);
	}

	
	public User authenticateByPhoneNumber(String phoneNumber, String password){
       User user = Optional.of(findUserByPhoneNumber(phoneNumber))
    		   			   .filter( u -> passwordEncoader.matches(password, u.getPassword()))
    		   			   .orElseThrow(ExceptionUtility :: authenticationFailed);
       
       return Optional.of(user).filter( User :: isPhoneNumberVerified).orElseThrow(ExceptionUtility :: phoneNumberNotVerified);
	}
	
	public User authenticateByEmail(String email, String password){
	       User user = Optional.of(findUserByEmail(email)).filter( u -> passwordEncoader.matches(password, u.getPassword()))
	    		   				.orElseThrow(ExceptionUtility :: authenticationFailed);
	       
	       return Optional.of(user).filter( User :: isEmailVerified).orElseThrow(ExceptionUtility :: emailNotVerified);
	}
		
	@Transactional(readOnly = true)
	public User findById(int userId) {
		Optional<User> user = userRepo.findById(userId);
		
		if(!user.isPresent()) {
			LOGGER.error("Bad Request Invalid user id", userId);
			throw new GenericException("Invalid.user.id", RestConstant.BAD_REQUEST);
		}
		return user.get();
	}
	
	@Transactional(readOnly = true)
	public User checkIfEmailExist(String email) {
		User user = userRepo.findByEmail(email);
		if(isNull(user) ) {
			 LOGGER.error("email does not exist "+ email);
			 throw new GenericException("not.existing.email", RestConstant.UNAUTHENTICATED);
			}
		return user;
	}

	@Transactional(readOnly = true)
	public User loadEmail(String email) {
		return userRepo.findByEmail(email);
	}

	@Transactional(readOnly = true)
	public User loadPhoneNumber(String phoneNumber) {
		return userRepo.findByPhoneNumber(phoneNumber);
	}

	@Transactional(readOnly = true)
	public User findUserByPhoneNumber(String phoneNumber) {
		return userRepo.findUserByPhoneNumber(phoneNumber).orElseThrow(ExceptionUtility :: phoneNumberDoesNotExist) ;
	}
	
	@Transactional(readOnly = true)
	public User findUserByEmail(String email) {
		return userRepo.findUserByEmail(email).orElseThrow(ExceptionUtility :: emailDoesNotExist) ;
	}
	
	@Transactional
	public User save(final User user) {
		userRepo.save(user);
//		profileRepo.save(user.getProfiles().get(0));
//		deviceDetailRepo.save(user.getDeviceDetails().get(0));
	    return user;
	}
	
	@Transactional(readOnly = true)
	public User checkIfPhoneNumberExist(String phoneNumber) {
		final User user = userRepo.findByPhoneNumber(phoneNumber);
		if(null == user ) {
		  LOGGER.info("Phone Number does not exist "+ phoneNumber);
		  throw new GenericException("not.existing.phoneNumber", RestConstant.UNAUTHENTICATED);
		}
		return user;
	}
	
	@Transactional
	public UserModel buildUserModel(final User user, boolean withAuthHeader) {
		boolean isFirstSignIn	= false;
		if(null == user.getLoginDetails() || user.getLoginDetails().isEmpty()) {
			isFirstSignIn = true;
		}
		
		final ProfileModel profileModel = ProfileModelBuilder.of().profile(user.getProfiles().get(0)).build();
		
		return UserModelBuilder.of()
				.authenticationHeader(withAuthHeader ? jwtTokenUtil.generateToken(new JwtUser(user.getId(),user.getPassword(), 
						
				user.getDeviceDetails().stream().findFirst().get().getId(), user.getDeviceDetails().stream().findFirst().get().getDeviceToken())) : null)
				
				.userId(user.getId()).email(user.getEmail())
				.phoneNumber(user.getMobile()).profile(profileModel)
				.firstSignIn(isFirstSignIn)
				.shouldUserBeShownStartUpViedo(isFirstSignIn)
				.lat(user.getLat())
				.lon(user.getLon())
				.buildUser();
	}
	
	public UserModel getUserById(int userId) {
		return buildUserModel(findById(userId), true);
	}

	@Transactional(readOnly = true)
	public User loadUserById(int userId) {
		return userRepo.getOne(userId) ;
	}

	@Transactional
	public UserModelBuilder buildPublicUserModel(User targettedUser) {
		final ProfileModel profileModel = ProfileModelBuilder.of().profile(targettedUser
				.getProfiles().get(0)).build();
		return UserModelBuilder.of()
		.userId(targettedUser.getId())
		.profile(profileModel);
	}

	@Transactional
	public List<UserModel> buildUserModel(List<Profile> profiles) {
		if(null != profiles) {
			return profiles.stream().map(Profile :: getUser)
					.map(this :: buildPublicUserModel)
					.map(UserModelBuilder :: buildUser)
					.collect(toList());
		}
		return Collections.emptyList();
	}

	public List<ResourceModel> getAlbumOfUser(int userId, int page, long size) {
		
		
		return null;
	}

	public void throwExceptionIfPhoneExist(String phoneNumber) {
		userRepo.findUserByPhoneNumber(phoneNumber)
				.ifPresent( u ->{ throw ExceptionUtility.phoneNumberAlreadExist();});
	}

	public void throwExceptionIfEmailExist(String email) {
		userRepo.findUserByEmail(email)
				.ifPresent( u ->{ throw ExceptionUtility.emailAlreadExist();});
	}

}

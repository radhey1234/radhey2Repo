package com.iexpress.spring.api.profile.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iexpress.spring.api.form.ProfileForm;
import com.iexpress.spring.api.model.ResourceModel;
import com.iexpress.spring.api.model.SearchUserForm;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.profile.service.ProfileService;
import com.iexpress.spring.api.profile.service.ResourceService;
import com.iexpress.spring.api.repo.CityRepo;
import com.iexpress.spring.api.repo.ProfileRepo;
import com.iexpress.spring.api.response.PaginatedResponse;
import com.iexpress.spring.api.user.service.UserService;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.domain.City;
import com.iexpress.spring.domain.Profile;
import com.iexpress.spring.domain.Resource;
import com.iexpress.spring.domain.User;
import static com.iexpress.spring.api.util.AppUtil.*;
@Service
public class ProfileServiceImpl implements ProfileService{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileServiceImpl.class);
	private UserService userService;
	private ProfileRepo profileRepo;
	
	@Autowired 
	private CityRepo cityRepo;
	@Autowired
	private ResourceService resourceService;
	
	@Autowired
	public ProfileServiceImpl(ProfileRepo profileRepo, UserService userService) {
		this.profileRepo = profileRepo;
		this.userService = userService;
	}
	
	@Override
	@Transactional
	public UserModel createOrUpdateProfile(int userId, ProfileForm form) {
		LOGGER.info(" Inside createOrUpdateProfile User id "+ userId);
		User user =  userService.findById(userId);
		
		if(isValid(form.getPassword())) {
			user.setPassword(form.getPassword());
		}
		
		if(isValid(form.getLat())  && isValid(form.getLat())) {
			user.setLat(form.getLat());
			user.setLon(form.getLon());
		}
		
		createOrUpdateProfile( user, form.getCity(), form.getState(), 
				form.getDob(), form.getCityId(),form.getFirstName(), form.getLastName(), form.getUserName(), form.getProfileBio());
		userService.save(user);
		return userService.buildUserModel(user, false);
	}

	

	@Override
	@Transactional
	public Profile createOrUpdateProfile(User user, String city, String state,
			String dob, int cityId, String firstName, String lastName, String 	userName, String profileBio) {
		LOGGER.info("Inside createOrUpdateProfile user id : "+ user.getId());
		
		
		String fullName = user.getUserName();
		
		if(((!isValid(firstName) && !isValid(lastName))&&(isValid(fullName)))){
			int idx = fullName.lastIndexOf(' ');
			if (idx == -1) {
				
			}else {
			 firstName = fullName.substring(0, idx);
			 lastName  = fullName.substring(idx + 1);
			}

		}

		if(cityId !=0) {
			Optional<City> oCity = cityRepo.findById(cityId);
			if(oCity.isPresent()) {
				city = oCity.get().getCityName();
				state = oCity.get().getState().getName();
			}
		}
		
		Profile profile = null;
		
		if(user.getProfiles() != null && user.getProfiles().size() != 0) {
			profile = user.getProfiles().get(0);
		} else {
			profile = new Profile();
		}
		
		if(isValid(firstName))
			profile.setFirstName(firstName);
		
		if(isValid(lastName))
			profile.setLastName(lastName);
		
		if(isValid(city))
			profile.setCity(city);
		
		if(isValid(state))
			profile.setState(state);
		
		if(isValid(dob))
			profile.setDob(dob);
		
		if(isValid(userName))
			profile.setUserName(userName);
		
		if(isValid(profileBio))
			profile.setProfileBio(profileBio);
		
		profile.setUser(user);
		profileRepo.save(profile);
		return profile;
	}

	@Override
	@Transactional
	public ResourceModel addImageToProfilePic(int userId, int resourceId) {
		LOGGER.info("Inside  addImageToProfilePic user Id , resource ID"+ userId +" - "+ resourceId);
		User user =	userService.findById(userId);
		Resource resource = resourceService.findResourceById(resourceId);
		user.getProfiles().get(0).setProfilePic(resource.getUrl());
		return resourceService.buildResourceModel(resource);
	}
	
	@Override
	@Transactional
	public PaginatedResponse<List<UserModel>> getAllUser(int id, SearchUserForm form) {
		LOGGER.info("Inside getAllUser key : "+ form.getKey());
		final Pageable pageable =	buildPageable(form.getPage(), form.getSize());
		final Page<Profile> profiles = findAllUserByName(form.getKey(), pageable);
		final List<UserModel> users =	userService.buildUserModel(null != profiles ? profiles.getContent() : Collections.emptyList());
		return new PaginatedResponse<List<UserModel>>(users, profiles.getTotalPages(), profiles.getTotalElements());
	}

	public Page<Profile> findAllUserByName(String key, Pageable pageable) {
		return AppUtil.isNullOrEmpty(key) ? profileRepo.findAll(pageable) : profileRepo.findUserByName(key,  pageable);
	}

	private Pageable buildPageable(int page, int size) {
		return
				page == 0 ? Pageable.unpaged() :PageRequest.of(page-1, size);
	}

	@Override
	public Profile createOrUpdateProfile(Profile profile) {
		return null;
	}

	@Override
	@Transactional
	public void save(Profile newProfile) {
		LOGGER.info("Inside ProfileService save");
		profileRepo.save(newProfile);
	}

}

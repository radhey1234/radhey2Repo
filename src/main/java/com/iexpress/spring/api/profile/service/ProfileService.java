package com.iexpress.spring.api.profile.service;


import java.util.List;

import org.springframework.stereotype.Service;
import com.iexpress.spring.api.form.ProfileForm;
import com.iexpress.spring.api.model.ResourceModel;
import com.iexpress.spring.api.model.SearchUserForm;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.response.PaginatedResponse;
import com.iexpress.spring.domain.Profile;
import com.iexpress.spring.domain.User;

@Service
public interface ProfileService {

	public UserModel createOrUpdateProfile(int userId, ProfileForm form) ;	

	public Profile createOrUpdateProfile( 
			User user, String city, String state,String dob, int cityId,String firstName, String lastName, String userName
			,String profileBio);
	
	
	public Profile createOrUpdateProfile( Profile profile);
	public ResourceModel addImageToProfilePic(int userId, int resourceId);
	public PaginatedResponse<List<UserModel>> getAllUser(int id, SearchUserForm form);

	public void save(Profile newProfile);
}

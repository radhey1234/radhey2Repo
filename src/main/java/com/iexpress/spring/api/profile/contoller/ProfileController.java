package com.iexpress.spring.api.profile.contoller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iexpress.spring.api.form.ProfileForm;
import com.iexpress.spring.api.form.ResourceForm;
import com.iexpress.spring.api.form.ResourceIdForm;
import com.iexpress.spring.api.model.ResourceModel;
import com.iexpress.spring.api.model.SearchUserForm;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.profile.service.ProfileService;
import com.iexpress.spring.api.profile.service.ResourceService;
import com.iexpress.spring.api.response.PaginatedResponse;
import com.iexpress.spring.api.response.ResponseEnvelop;
import com.iexpress.spring.api.user.service.UserService;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.api.util.RestConstant;
import com.iexpress.spring.domain.User;
import com.iexpress.spring.jwt.JwtUser;

@RestController
@RequestMapping("/api/v1/")
public class ProfileController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);
	@Autowired private ResourceService resourceService;
	@Autowired private ProfileService profileService;
	@Autowired private UserService userService;
	
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping( value = "resource")
	public ResponseEnvelop<List<ResourceModel>> uploadResources(JwtUser jwtUser , @ModelAttribute  ResourceForm form){
		LOGGER.info("Inside Upload resource "+ jwtUser.getId());
		return buildResponseResourceModel(resourceService.uploadResources(jwtUser.getId(),form), 
				RestConstant.RESOURCE_UPLOADED_SUCCESSFULLY);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping( value = "profile")
	public ResponseEnvelop<UserModel> createOrUpdateProfile(JwtUser jwtUser ,@RequestBody ProfileForm form){
		return buildResponseUserModel(profileService.createOrUpdateProfile(jwtUser.getId(),form),
				RestConstant.SUCCESSFULL_RESPONSE);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping( value = "profile/image")
	public ResponseEnvelop<ResourceModel> addImageToProfilePic(JwtUser jwtUser, @RequestBody ResourceIdForm resourceIdForm){
		return buildResponseResourceModel(profileService.addImageToProfilePic(jwtUser.getId(), resourceIdForm.getId()), 
				RestConstant.RESOURCE_UPLOADED_SUCCESSFULLY);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@PostMapping( value = "location")
	public ResponseEnvelop<UserModel> updateLocation(JwtUser jwtUser ,@RequestBody ProfileForm form){
		return buildResponseUserModel(profileService.createOrUpdateProfile(jwtUser.getId(),form),
				RestConstant.SUCCESSFULL_RESPONSE);
	}
	
	@RequestMapping(value = "users", method = RequestMethod.GET, produces =  { "application/json" })
	public ResponseEnvelop<PaginatedResponse<List<UserModel>>> getAllUser(JwtUser jwtUser, 
			@ModelAttribute SearchUserForm form){
		LOGGER.info("inside UserController : getAllUser {}");
		PaginatedResponse<List<UserModel>> paginatedResponse = null;
		if(null != jwtUser){
			paginatedResponse = profileService.getAllUser(jwtUser.getId(), form);
		}
		return new ResponseEnvelop<>(paginatedResponse, AppUtil.of().getEnv().getProperty(RestConstant.LIST_OF_USERS), RestConstant.OK);
	}

	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping( value = "user/{userId}/album")
	public ResponseEnvelop<PaginatedResponse<List<ResourceModel>>> album(@PathVariable("userId") int userId, @RequestParam("page") int page,
			@RequestParam("size") long size ){
		return buildPaginatedResponseResourceModel(userService.getAlbumOfUser(userId, page , size), page, size, RestConstant.SUCCESSFULL_RESPONSE);
	}
	
	@ResponseStatus(code = HttpStatus.OK)
	@GetMapping( value = "user/{userId}")
	public ResponseEnvelop<UserModel> profile(@PathVariable("userId") int userId){
		return buildResponseUserModel(userService.getUserById(userId), RestConstant.SUCCESSFULL_RESPONSE);
	}
	
	@GetMapping("email")
	public <T> ResponseEnvelop<ObjectNode> checkIfEmailAlreadyExist(@RequestParam("email") String email){
		LOGGER.info("Inside checkIfEmailAlreadyExist "+ email);
		User user = userService.loadEmail(email);
		return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), 
				AppUtil.of().getEnv().getProperty(user != null ? RestConstant.EXISTING_EMAIL : RestConstant.NOT_EXISTING_EMAIL),
				user != null ? RestConstant.OK : RestConstant.NOT_FOUND);
	}
	
	@GetMapping("phoneNumber")
	public <T> ResponseEnvelop<ObjectNode> checkIfPhoneNumberAlreadyExist(@RequestParam("phoneNumber") String phoneNumber){
		LOGGER.info("Inside checkIfPhoneNumberAlreadyExist "+ phoneNumber);
		User user = userService.loadPhoneNumber(phoneNumber);
		return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(),  
				AppUtil.of().getEnv().getProperty(user != null ? RestConstant.EXISTING_PHONE_NUMBER : RestConstant.NOT_EXISTING_PHONE_NUMBER), 
				user != null ? RestConstant.OK : RestConstant.NOT_FOUND);
	}

	private ResponseEnvelop<PaginatedResponse<List<ResourceModel>>> buildPaginatedResponseResourceModel(final List<ResourceModel> resourceModel
			,int totalPages, long numberOfResults, String message) {
		return new ResponseEnvelop<PaginatedResponse<List<ResourceModel>>>(new PaginatedResponse<List<ResourceModel>>(resourceModel, 
				totalPages, numberOfResults) , AppUtil.of().getEnv().getProperty(message) ,RestConstant.OK);
	}

	private ResponseEnvelop<List<ResourceModel>> buildResponseResourceModel(final List<ResourceModel> resourceModel, final String message) {
		return new ResponseEnvelop<List<ResourceModel>>(resourceModel, AppUtil.of().getEnv().getProperty(message) ,RestConstant.OK);
	}

	private ResponseEnvelop<ResourceModel> buildResponseResourceModel(ResourceModel resourceModel, final String message) {
		return new ResponseEnvelop<ResourceModel>(resourceModel, AppUtil.of().getEnv().getProperty(message) ,RestConstant.OK);
	}

	private ResponseEnvelop<UserModel> buildResponseUserModel(final UserModel resourceModel, final String message) {
		return new ResponseEnvelop<UserModel>(resourceModel, AppUtil.of().getEnv().getProperty(message) ,RestConstant.OK);
	}

}

package com.iexpress.spring.api.profile.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.iexpress.spring.api.exception.GenericException;
import com.iexpress.spring.api.form.ResourceForm;
import com.iexpress.spring.api.model.ResourceModel;
import com.iexpress.spring.api.model.builder.ResourceModelBuilder;
import com.iexpress.spring.api.repo.ProfileRepo;
import com.iexpress.spring.api.repo.ResourceRepo;
import com.iexpress.spring.api.user.service.UserService;
import com.iexpress.spring.api.util.AWSImageUpload;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.api.util.RestConstant;
import com.iexpress.spring.domain.Post;
import com.iexpress.spring.domain.ProfilePlace;
import com.iexpress.spring.domain.Resource;
import com.iexpress.spring.domain.ResourceType;
import com.iexpress.spring.domain.User;

@Service
public class ResourceService {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ResourceService.class);
	private static final String RESOURCE = "resource";
	private static final String THUMBNAIL = "thumbnail";
	
	@Autowired private AWSImageUpload awsUtil;
	@Autowired private ResourceRepo resourceRepo;
	@Autowired private ProfileRepo profileRepo;
	@Autowired private AppUtil appUtil;
	
	private UserService userService;
	@Autowired
	public ResourceService(UserService userService) {
		this.userService = userService;
	}
	

	public List<ResourceModel> uploadResources(int userId, ResourceForm form) {
		LOGGER.info("Uploading Resources with User Id {}", userId);
		
		List<Resource> resources = new ArrayList<Resource>();
		for(MultipartFile file : form.getResource()) {
		LOGGER.info("Uploading Resources file content type: {}", file.getContentType());
		LOGGER.info("Uploading Resources file name: {}", file.getOriginalFilename());
		LOGGER.info("Uploading Resources file size: {}", file.getSize());
		Optional<User> user =	userService.findUserById(userId);
		if(user == null) {
			LOGGER.error("Bad Request Invalid user id", userId);
			throw new GenericException("Invalid.user.id", RestConstant.BAD_REQUEST);
		}
		final String randomeFileName = UUID.randomUUID().toString();
		final String uri = awsUtil.saveResourceOfAnEvent(file, randomeFileName);
		Resource resource = new Resource();
		resource.setBelongsTo(ProfilePlace.textOf(form.getBelongsTo()));
		resource.setUser(user.get());
		resource.setResourceType(ResourceType.IMAGE);
		resource.setUrl(uri);
		resourceRepo.save(resource);
		
		if(AppUtil.isNullOrEmpty(user.get().getProfiles().get(0).getProfilePic())) {
			user.get().getProfiles().get(0).setProfilePic(uri);
		}
		
		profileRepo.save(user.get().getProfiles().get(0));
		resources.add(resource);
		}
		return buildResourceModel(resources);
	}

	private List<ResourceModel> buildResourceModel(List<Resource> resources) {
		return resources.stream().map(this :: buildResourceModel).collect(Collectors.toList());
	}
	
	public ResourceModel buildResourceModel(Resource resource) {
	  return  ResourceModelBuilder.of()
				.resourceId(resource.getId())
				.resourceUrl(appUtil.getResourePrefixAws()+resource.getUrl())
				.resourceUrlTiny(appUtil.getResourePrefixAws() + resource.getUrl().replaceFirst(RESOURCE, THUMBNAIL))
				.resourceType(resource.getResourceType().name())
				.belongsTo(resource.getBelongsTo().name())
				.build();
	}


	@Transactional(readOnly = true)
	public Resource loadResourceById(int id) {
		return resourceRepo.getOne(id);
	}


	@Transactional
	public void addToPost(Post post, List<Integer> resourceIds) {
		List<Resource> resources = resourceIds
				.stream().map(this :: loadResourceById)
				.collect(Collectors.toList());	

		for (Resource resource : resources) {
			resource.setPost(post);
		}
		
		resourceRepo.saveAll(resources);
	}

	@Transactional(readOnly = true)
	public Resource findResourceById(int resourceId) {
		return resourceRepo.findById(resourceId).orElseThrow(AppUtil :: throwResourceNotFound);
	}

}

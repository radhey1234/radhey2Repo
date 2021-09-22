package com.iexpress.spring.api.iread.controller;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.iexpress.spring.api.iread.service.IReadService;
import com.iexpress.spring.api.model.PostModel;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.response.PaginatedResponse;
import com.iexpress.spring.api.response.ResponseEnvelop;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.api.util.RestConstant;
import com.iexpress.spring.jwt.JwtUser;

@RestController
@RequestMapping("/api/v1/")
public class IReadController {

	private static final Logger LOGGER = LoggerFactory.getLogger(IReadController.class);
	
	@Autowired private IReadService iReadService;
	
	/**
	 * This will return all existing seen/read post with detail and 
	 * @param jwtUser
	 * @param userId
	 */
	@GetMapping("user/{targetUserId}/read")
	public ResponseEnvelop<UserModel> accessToProfile(JwtUser jwtUser, @PathVariable("targetUserId") int targetUserId) {
		LOGGER.info("Inside accessToProfile  logged In user "+ jwtUser.getId() + " user looking for "+ targetUserId);
		UserModel userAndPostDetail = iReadService.getAllReadablePost(jwtUser.getId(), targetUserId);
		return buildResponseResourceModel(userAndPostDetail, RestConstant.RESPONSE_SUCCESSFULL);
	}

	
	@GetMapping("user/{userId}/post")
	public ResponseEnvelop<PaginatedResponse<List<PostModel>>> listingUsersAllPost(JwtUser jwtUser, @PathVariable("userId") int userId,
			@RequestParam("page") int page, @RequestParam("size") int size, @RequestParam("key") String key, @RequestParam("status") String status) {
		LOGGER.info("Inside accessToProfile  logged In user "+ jwtUser.getId() + " user looking for "+ userId);
	    PaginatedResponse<List<PostModel>> posts = iReadService.getAllUsersPost( userId, page, size , "%"+key+"%", status);
		return buildPagainatedPostModel(posts, RestConstant.RESPONSE_SUCCESSFULL);
	}

	@GetMapping("user/{userId}/post/{postId}")
	public ResponseEnvelop<PostModel> findPostById(JwtUser jwtUser, @PathVariable("userId") int userId,
			@PathVariable("postId") int postId) {
		LOGGER.info("Inside accessToProfile  logged In user "+ jwtUser.getId() + " user looking for "+ userId);
		PostModel posts = iReadService.getPostById(postId);
		return buildResponsePostModel(posts, RestConstant.RESPONSE_SUCCESSFULL);
	}

	private ResponseEnvelop<UserModel> buildResponseResourceModel(final UserModel userModel, final String message) {
		return new ResponseEnvelop<UserModel>(userModel, AppUtil.of().getEnv().getProperty(message) ,RestConstant.OK);
	}

	private ResponseEnvelop<PostModel> buildResponsePostModel(final PostModel userModel, final String message) {
		return new ResponseEnvelop<PostModel>(userModel, AppUtil.of().getEnv().getProperty(message) ,RestConstant.OK);
	}

	private ResponseEnvelop<PaginatedResponse<List<PostModel>>> buildPagainatedPostModel(final PaginatedResponse<List<PostModel>> posts, final String message) {
		return new ResponseEnvelop<PaginatedResponse<List<PostModel>>>(posts, AppUtil.of().getEnv().getProperty(message) ,RestConstant.OK);
	}

}

package com.iexpress.spring.api.iread.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.iexpress.spring.api.model.PostModel;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.post.service.PostService;
import com.iexpress.spring.api.response.PaginatedResponse;
import com.iexpress.spring.domain.Post;

@Service
public class IReadServiceImp implements IReadService {

	private static final Logger LOGGER = LoggerFactory.getLogger(IReadServiceImp.class);
	private final PostService postService;
	
	@Autowired
	public IReadServiceImp( PostService postService) {
		this.postService = postService;
	}
	
	@Override
	public UserModel getAllReadablePost(int loggedInUserId, int targetUserId) {
		LOGGER.info(" Inside getAllReadablePost loggedInUserId "+ loggedInUserId + " target user id "+ targetUserId);
		return	postService.postsWrittenForLoggedInUserBy(loggedInUserId, targetUserId);
	}

	@Override
	public PaginatedResponse<List<PostModel>> getAllUsersPost(int userId, int page, int size, String key, String status) {
		LOGGER.info("Inside getAllUsersPost user id "+ userId);
		Page<Post> posts = postService.listAllUsersPostByUserId(userId, PageRequest.of(page-1, size),key, status);
		return new PaginatedResponse<List<PostModel>>(postService.buildPostModels(posts), posts.getTotalPages(), posts.getTotalElements());
	}
	

	@Transactional
	@Override
	public PostModel getPostById(int postId) {
		return postService.buildPostModelWithContentDetail(postService.findPostById(postId));
	}

}

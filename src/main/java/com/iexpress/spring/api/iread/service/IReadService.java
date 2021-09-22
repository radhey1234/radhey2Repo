package com.iexpress.spring.api.iread.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.iexpress.spring.api.model.PostModel;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.response.PaginatedResponse;

@Service
public interface IReadService {

	UserModel getAllReadablePost(int loggedInUserId, int targetUserId);

	PaginatedResponse<List<PostModel>> getAllUsersPost(int userId, int page, int size, String key, String status);

	PostModel getPostById(int postId);

}

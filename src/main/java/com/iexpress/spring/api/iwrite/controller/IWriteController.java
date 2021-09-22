package com.iexpress.spring.api.iwrite.controller;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iexpress.spring.api.form.PostForm;
import com.iexpress.spring.api.form.QuestionForm;
import com.iexpress.spring.api.model.PostModel;
import com.iexpress.spring.api.model.QuestionModel;
import com.iexpress.spring.api.post.service.PostService;
import com.iexpress.spring.api.response.ResponseEnvelop;
import com.iexpress.spring.api.util.RestConstant;
import com.iexpress.spring.jwt.JwtUser;

@RestController
@RequestMapping("/api/v1/")
public class IWriteController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(IWriteController.class);
	
	@Autowired private Environment env;
	@Autowired private PostService postService;

	
	@PostMapping("post")
	@ResponseStatus(code = HttpStatus.CREATED)
	public <T> ResponseEnvelop<PostModel> createPost(JwtUser jwtUser, @RequestBody PostForm postForm ){
		LOGGER.info("Inside createPost - user id - "+ jwtUser.getId());
		PostModel postModel = postService.createPost(jwtUser, postForm);
		return new ResponseEnvelop<PostModel>(postModel, resolveToString(RestConstant.SUCCESSFULL_RESPONSE), RestConstant.OK);
	}

	@PatchMapping("post")
	public <T> ResponseEnvelop<PostModel> updatePost(JwtUser jwtUser, @RequestBody PostForm postForm ){
		LOGGER.info("Inside createPost - user id - "+ jwtUser.getId());
		PostModel postModel = postService.updatePost(jwtUser, postForm);
		return new ResponseEnvelop<PostModel>(postModel, resolveToString(RestConstant.SUCCESSFULL_RESPONSE), RestConstant.OK);
	}

	@PostMapping("post/{postId}/answer")
	@ResponseStatus(code = HttpStatus.OK)
	public <T> ResponseEnvelop<PostModel> answerQuestionOfPost(JwtUser jwtUser, @RequestBody QuestionForm questionAnswerForm ){
		LOGGER.info("Inside addQuestion - user id - "+ jwtUser.getId() + "  post id"+ questionAnswerForm.getPostId());
		PostModel postModel = postService.answerQuestion(jwtUser, questionAnswerForm);
		return new ResponseEnvelop<PostModel>(postModel, resolveToString(RestConstant.SUCCESSFULL_RESPONSE), RestConstant.OK);
	}
	

	@PostMapping("question")
	@ResponseStatus(code = HttpStatus.CREATED)
	public <T> ResponseEnvelop<List<QuestionModel>> addQuestion(JwtUser jwtUser, @RequestBody QuestionForm questionForm ){
		LOGGER.info("Inside addQuestion - user id - "+ jwtUser.getId() + "  post id"+ questionForm.getPostId());
		List<QuestionModel> questionModels = postService.addQuestion(jwtUser, questionForm);
		return new ResponseEnvelop<List<QuestionModel>>(questionModels, resolveToString(RestConstant.SUCCESSFULL_RESPONSE), RestConstant.OK);
	}
	

	@PatchMapping("question")
	public <T> ResponseEnvelop<List<QuestionModel>> updateQuestion(JwtUser jwtUser, @RequestBody QuestionForm questionForm ){
		LOGGER.info("Inside updateQuestion - user id - "+ jwtUser.getId() + "  post id"+ questionForm.getPostId());
		List<QuestionModel> questionModels = postService.updateQuestion(jwtUser, questionForm);
		return new ResponseEnvelop<List<QuestionModel>>(questionModels, resolveToString(RestConstant.SUCCESSFULL_RESPONSE), RestConstant.OK);
	}

	@DeleteMapping("question")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public <T> ResponseEnvelop<ObjectNode> deleteQuestions(JwtUser jwtUser, @RequestBody QuestionForm questionForm ){
		LOGGER.info("Inside addQuestion - user id - "+ jwtUser.getId() + "  post id"+ questionForm.getPostId());
		postService.deleteQuestion(jwtUser, questionForm);
		return new ResponseEnvelop<ObjectNode>( new ObjectMapper().createObjectNode() , resolveToString(RestConstant.SUCCESSFULL_RESPONSE), RestConstant.OK);
	}

	@DeleteMapping("question/{qId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public <T> ResponseEnvelop<ObjectNode> deleteQuestionById(JwtUser jwtUser, @PathVariable ("qId") int qId){
		LOGGER.info("Inside addQuestion - user id - "+ jwtUser.getId() + "  post id"+ qId);
		QuestionForm questionForm = new QuestionForm();
		questionForm.setQuestionIds(Arrays.asList(qId));
		postService.deleteQuestion(jwtUser, questionForm);
		return new ResponseEnvelop<ObjectNode>( new ObjectMapper().createObjectNode() , resolveToString(RestConstant.SUCCESSFULL_RESPONSE), RestConstant.OK);
	}

	@DeleteMapping("post/{postId}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public <T> ResponseEnvelop<ObjectNode> deletePostById(JwtUser jwtUser, @PathVariable ("postId") int postId){
		LOGGER.info("Inside deletePostById - user id - "+ jwtUser.getId() + "  post id"+ postId);
		postService.deletePostByIds(jwtUser, Arrays.asList(postId));
		return new ResponseEnvelop<ObjectNode>( new ObjectMapper().createObjectNode() , resolveToString(RestConstant.SUCCESSFULL_RESPONSE), RestConstant.OK);
	}

	@DeleteMapping("question/delink")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public <T> ResponseEnvelop<ObjectNode> deLinkQuestion(JwtUser jwtUser, @RequestBody QuestionForm questionForm ){
		LOGGER.info("Inside addQuestion - user id - "+ jwtUser.getId() + "  post id"+ questionForm.getPostId());
		postService.delinkQuestionFromPost(jwtUser, questionForm);
		return new ResponseEnvelop<ObjectNode>( new ObjectMapper().createObjectNode() , resolveToString(RestConstant.SUCCESSFULL_RESPONSE), RestConstant.OK);
	}
	
	@DeleteMapping("question/{qId}/delink")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	public <T> ResponseEnvelop<ObjectNode> deLinkQuestionById(JwtUser jwtUser, @PathVariable ("qId") int qId ){
		LOGGER.info("Inside addQuestion - user id - "+ jwtUser.getId() + "  post id"+ qId);
		QuestionForm questionForm = new QuestionForm();
		questionForm.setQuestionIds(Arrays.asList(qId));
		postService.delinkQuestionFromPost(jwtUser, questionForm);
		return new ResponseEnvelop<ObjectNode>( new ObjectMapper().createObjectNode() , resolveToString(RestConstant.SUCCESSFULL_RESPONSE), RestConstant.OK);
	}
	
	private String resolveToString(String input) {
		return env.getProperty(input);
	}
}

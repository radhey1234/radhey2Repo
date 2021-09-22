package com.iexpress.spring.api.post.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iexpress.spring.api.exception.GenericException;
import com.iexpress.spring.api.form.PostForm;
import com.iexpress.spring.api.form.QuestionForm;
import com.iexpress.spring.api.model.PostModel;
import com.iexpress.spring.api.model.QuestionModel;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.model.builder.PostModelBuilder;
import com.iexpress.spring.api.model.builder.QuestionModelBuilder;
import com.iexpress.spring.api.notification.NotificationService;
import com.iexpress.spring.api.profile.service.ResourceService;
import com.iexpress.spring.api.repo.PostRepo;
import com.iexpress.spring.api.repo.QuestionRepo;
import com.iexpress.spring.api.repo.UserRepo;
import com.iexpress.spring.api.response.ErrorResponse;
import com.iexpress.spring.api.user.service.UserService;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.domain.Post;
import com.iexpress.spring.domain.PostSeen;
import com.iexpress.spring.domain.PostStatus;
import com.iexpress.spring.domain.Question;
import com.iexpress.spring.domain.Resource;
import com.iexpress.spring.domain.User;
import com.iexpress.spring.jwt.JwtUser;
import static com.iexpress.spring.api.util.AppUtil.*;
import static com.iexpress.spring.api.util.RestConstant.*;
import static java.util.stream.Collectors.*;
@Service
public class PostService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PostService.class);
	private static final String PUBLISH = "PUBLISH";
	@Autowired private PostRepo postRepo;
	@Autowired private UserRepo userRepo;
	@Autowired private UserService userService;
	@Autowired private ResourceService resourceService;
	@Autowired private AppUtil appUtil;
	@Autowired private QuestionRepo questionRepo;
	@Autowired private NotificationService notificationService;
	/**
	 * Create Post
	 * @param jwtUser
	 * @param postForm
	 * @return
	 */
	@Transactional
	public PostModel createPost(JwtUser jwtUser, PostForm postForm) {
		LOGGER.info("Inside createPost User ID --"+jwtUser.getId());

		Optional<User> user = userRepo.findById(jwtUser.getId());

		Post newPost = new Post();
		newPost.setContent(postForm.getContent());
		newPost.setCreator(user.get());
		newPost.setIsSeen(false);
		newPost.setIsDeleted(false);
		newPost.setIsProtected(false);
		newPost.setStatus(PostStatus.DRAFT);
		
		if(!isNullOrEmpty(postForm.getStatus()) && postForm.getStatus().equalsIgnoreCase(PUBLISH) ) {
			newPost.setStatus(PostStatus.PUBLISHED);
		}
		
		if(!isNullOrEmpty(postForm.getReaderUserId())) {
			User userBeingTagged = userService.findById(postForm.getReaderUserId());
			newPost.setTaggedUser(userBeingTagged);
		}
		

		if(!isNullOrEmpty(postForm.getReaderUserFirstName())) {
			newPost.setTaggedUserFirstName(postForm.getReaderUserFirstName());
		}

		if(!isNullOrEmpty(postForm.getReaderUserLastName())) {
			newPost.setTaggedUserLastName(postForm.getReaderUserLastName());
		}

		postRepo.save(newPost);

		
		if(null !=  postForm.getResourceIds() || ! postForm.getResourceIds().isEmpty()) {

			List<Resource> resources = postForm.getResourceIds()
					.stream().map(this :: getResourceById).collect(Collectors.toList());
			newPost.setResources(resources);
			addPostToResource(newPost, postForm.getResourceIds());
		}

		return buildPostModel(newPost, true, false);
	}
	
	@Transactional(readOnly = true)
	public UserModel postsWrittenForLoggedInUserBy(int loggedInUserId, int targetUserId) {
	LOGGER.info("Inside postsWrittenForLoggedInUserBy loggedInUser "+loggedInUserId+ " target user id" + targetUserId);
	
		final User loggedInUser = userService.loadUserById(loggedInUserId);
	
		List<PostModel> postModels = new ArrayList<PostModel>();
		
		List<Post> posts = postRepo
				.findPostWrittenForLoggedInUser(loggedInUser.getId(), 
						loggedInUser.getProfiles().get(0).getFirstName(), 
						loggedInUser.getProfiles().get(0).getLastName(),
						targetUserId);
		
		if(null != posts && !posts.isEmpty()) {
			for(Post post : posts) {
				postModels.add(buildPostModelWithOutContentDetail(post, loggedInUserId));
			 }
		}
		
		User targettedUser =	userService.findById(targetUserId);
	    return userService.buildPublicUserModel(targettedUser).posts(postModels).buildUser();
	}

	/**
	 * It can be used to update post content, image,  read by , Q&A 
	 * @param jwtUser
	 * @param postForm
	 * @return
	 */
	@Transactional
	public PostModel updatePost(JwtUser jwtUser, PostForm postForm) {
		LOGGER.info("Inside updatePost -"+ jwtUser.getId());
		
		Post post = findPostById(postForm.getPostId());
		
		if(!isNullOrEmpty(postForm.getContent())){
			post.setContent(postForm.getContent());
		}
		
		if(!isNullOrEmpty(postForm.getStatus()) && postForm.getStatus().equalsIgnoreCase("PUBLISH") ) {
			post.setStatus(PostStatus.PUBLISHED);
		}
		
		if(!isNullOrEmpty(postForm.getReaderUserId())) {
			User userBeingTagged = userService.findById(postForm.getReaderUserId());
			post.setTaggedUser(userBeingTagged);
		}
		
		if(null !=  postForm.getResourceIds() || ! postForm.getResourceIds().isEmpty()) {
			List<Resource> resources = postForm.getResourceIds()
					.stream().map(this :: getResourceById).collect(Collectors.toList());
			post.setResources(resources);
			addPostToResource(post, postForm.getResourceIds());
		}

		if(!isNullOrEmpty(postForm.getReaderUserFirstName())) {
			post.setTaggedUserFirstName(postForm.getReaderUserFirstName());
		}

		if(!isNullOrEmpty(postForm.getReaderUserLastName())) {
			post.setTaggedUserLastName(postForm.getReaderUserLastName());
		}

		if(!isNull(postForm.getQuestionIds()) && !postForm.getQuestionIds().isEmpty()){
			List<Question> questions = postForm.getQuestionIds().stream().map(this :: loadQuestion).collect(Collectors.toList());
			post.setQuestions(questions);
			addQuestonToPost(post, postForm);
  		}
		post =	postRepo.saveAndFlush(post);
		return buildPostModel(post, true, false);
	}

	private void addQuestonToPost(Post post, PostForm postForm) {
		List<Question> questions = postForm.getQuestionIds().stream().map(this :: loadQuestion)
				.collect(Collectors.toList());
		questions.forEach(q -> q.setPost(post));
		questionRepo.saveAll(questions);
	}

	public void addPostToResource(Post post, List<Integer> resourceIds) {
		resourceService.addToPost(post, resourceIds);
	}

	public Resource getResourceById(int id) {
		return resourceService.loadResourceById(id);
	}

	@Transactional(readOnly = true)
	public Post findPostById(int postId) {
		LOGGER.info("Inside findPostById -"+ postId);
		return postRepo.findById(postId).orElseThrow(AppUtil :: throwPostNotFound);
	}

	@Transactional(readOnly = true)
	public Page<Post> listAllUsersPostByUserId(int userId, Pageable pageable, String key, String status) {
		LOGGER.info("Inside listAllUsersPostByUserId user id "+ userId);

		PostStatus postStatus = findValidStatus(status);
		
		if (StringUtils.isEmpty(key)) {
			if(null == postStatus)
			 return postRepo.listAllUsersPostByUserId(userId, postStatus, pageable);
			else
			 return postRepo.listAllUsersPostByUserId(userId, pageable);
		}
		
		if(null == postStatus)
			return postRepo.listAllUsersPostByUserId(userId, key, pageable);
		
		return postRepo.listAllUsersPostByUserId(userId, key, postStatus, pageable);
	}
	

	@Transactional(readOnly = true)
	public PostModel buildPostModelWithOutContentDetail(Post post, int loggedInUser) {
		Optional<User> isAlreadySeen =   post.getPostSeens().stream()
		 	.map(PostSeen :: getUser)
		 	.filter(p -> (p.getId() == loggedInUser))
		 	.findFirst();
		
		return buildPostModel(post, isAlreadySeen.isPresent(), isAlreadySeen.isPresent());
	}
	
	
	@Transactional(readOnly = true)
	public PostModel buildPostModelWithContentDetail(Post post) {
		return buildPostModel(post, true, false);
	}
	
	private PostModel buildPostModel(Post newPost, boolean includeContentDetail, boolean isSeen) {
		return PostModelBuilder.of().id(newPost.getId())
				.content(includeContentDetail ? newPost.getContent() : "")
				.creator(newPost.getCreator().getId())
				.createdAt(convertDateToString(DATE_TIME_PATTERN, newPost.getCreatedAt()))
				.updatedAt(convertDateToString(DATE_TIME_PATTERN, newPost.getModifiedAt()))
				.urls(getResourceUrlFromResources(includeContentDetail ? newPost.getResources() : null))
				.status(includeContentDetail ? newPost.getStatus().name() : "")
				.readerFirstName(includeContentDetail ? newPost.getTaggedUserFirstName() : "")
				.readerLastName(includeContentDetail ? newPost.getTaggedUserLastName() : "")
				.readerUserId(includeContentDetail && null != newPost.getTaggedUser() ?  newPost.getTaggedUser().getId() : 0)
				.question(includeContentDetail && null != newPost.getQuestions() && !newPost.getQuestions().isEmpty() ? buildQuestionModel(newPost.getQuestions() , true) : buildQuestionModel(newPost.getQuestions() , false) )
				.seen(isSeen)
				.build();
	}

	private List<String> getResourceUrlFromResources(List<Resource> resources) {
		if(null != resources) {
			return resources.stream().map(Resource :: getUrl)
					.map(s -> appUtil.getResourePrefixAws()+s)
					.collect(Collectors.toList());
		}
		return null;
	}
	
	@Transactional
	public List<QuestionModel> addQuestion(JwtUser jwtUser, QuestionForm questionForm) {
		LOGGER.info("Inside addQuestion ");
		User loggedInUser = userService.findById(jwtUser.getId());
		List<Question> questions = null;
		
		if(questionForm.getPostId() != 0) 
			questions = saveQuestionWithPost(questionForm, loggedInUser);
		else
			questions = saveQuestionWithoutPost(questionForm, loggedInUser);
		
		return buildQuestionModel(questions, true);
	}

	private List<Question> saveQuestionWithPost(QuestionForm questionForm, User loggedInUser) {
		return saveQuestion(questionForm.getQuestions(), loggedInUser, findPostById(questionForm.getPostId()));
	}

	private List<Question> saveQuestionWithoutPost(QuestionForm questionForm, User loggedInUser) {
		return saveQuestion(questionForm.getQuestions(), loggedInUser, null);
	}
	
	
	@Transactional
	public List<Question> saveQuestion(List<QuestionModel> inputQuetionModels, User loggedInUser, Post post){
		if(null != inputQuetionModels && !inputQuetionModels.isEmpty()) {
			List<Question> questions  = inputQuetionModels
			.stream().map( questionModel -> buildQuestionEntity( questionModel, post, loggedInUser)).collect(Collectors.toList());
			 return questionRepo.saveAll(questions);	
		}
		return null;
	}

	@Transactional
	public List<QuestionModel> updateQuestion(JwtUser jwtUser, QuestionForm questionForm) {
		LOGGER.info("Inside updateQuestion ");

		List<Question> questions = null;
		if(null != questionForm.getQuestions() && !questionForm.getQuestions().isEmpty()) {
			
			questions = questionForm.getQuestions()
					 .stream()
					 .map(QuestionModel :: getQuestionId)
					 .map(this :: loadQuestion)
					 .filter(q -> questionForm.getQuestions().stream().filter( qm -> qm.getQuestionId() == q.getId()).findAny().isPresent())
					 .map( q -> updateQuestion(q, 
							 questionForm.getQuestions().stream().filter( qm -> qm.getQuestionId() == q.getId() ).findFirst().orElseGet(null)))
					 .collect(Collectors.toList());
			
			 questions = questionRepo.saveAll(questions);	 
		}
		return buildQuestionModel(questions, true);
	}

	@Transactional
	public boolean delinkQuestionFromPost(JwtUser jwtUser, QuestionForm questionForm) {
		LOGGER.info("Inside delinkQuestionFromPost ");

		if(null != questionForm.getQuestionIds() && !questionForm.getQuestionIds().isEmpty()) {
			questionForm.getQuestionIds()
					 .stream()
					 .map(this :: loadQuestion)
					 .filter(q -> questionForm.getQuestionIds().contains(q.getId()))
					 .forEach(q -> delinkQuestion(q));
			 return true;
		}
		return false;
	}
	
	@Transactional
	public Question delinkQuestion(Question question) {
		LOGGER.info("Inside  delinkQuestion "+ question.getId());
		question.setPost(null);
		questionRepo.save(question);
		return question;
	}
	
	@Transactional
	public boolean deleteQuestion(JwtUser jwtUser, QuestionForm questionForm) {
		LOGGER.info("Inside deleteQuestion qids "+ questionForm.getQuestionIds());

		if(null != questionForm.getQuestionIds() && !questionForm.getQuestionIds().isEmpty()) {
			questionForm.getQuestionIds().stream().forEach(this :: deleteQuestionById);
			return true;
		}
		return false;
	}

	
	@Transactional(readOnly = true)
	public Question loadQuestion(int questionId) {
		return questionRepo.getOne(questionId);
	}
	
	@Transactional
	public void deleteQuestionById(int questionId) {
		questionRepo.deleteById(Arrays.asList(questionId));
	}

	@Transactional
	public void deleteQuestionById(List<Integer> questionIds) {
		questionRepo.deleteById(questionIds);
	}

	private Question updateQuestion(Question question, QuestionModel questionModelInput) {
		LOGGER.info("Inside updateQuestion qId "+ question.getId());	
		if(null != questionModelInput.getAnswers() && !questionModelInput.getAnswers().isEmpty()) {
			question.setAnswer(questionModelInput.getAnswers().stream().map(String :: toUpperCase ).collect(Collectors.joining(",")));
		}
		
		if(null != questionModelInput.getQuestion() && !questionModelInput.getQuestion().isEmpty()) {
			question.setQuestion(question.getQuestion());
		}
		return question;
	}

	private List<QuestionModel> buildQuestionModel(List<Question> questions, boolean shouldAnswerBeIncluded) {
		if(null != questions)
			return questions.stream().map(q -> buildQuestionModel(q, shouldAnswerBeIncluded)).collect(toList());
		else 
			return null;
	}
	
	private QuestionModel buildQuestionModel(Question question, boolean shouldAnswerBeIncluded) {
		return QuestionModelBuilder.of()
				.question(question.getQuestion())
				.answers(shouldAnswerBeIncluded ? Arrays.asList(question.getAnswer().split(",")) : null)
				.questionId(question.getId())
				.postId(null != question.getPost() ? question.getPost().getId() : 0)
				.createdAt(convertDateToString(DATE_TIME_PATTERN, question.getCreatedAt()))
				.updatedAt(convertDateToString(DATE_TIME_PATTERN, question.getModifiedAt()))
				.build();
	}

	private Question buildQuestionEntity(QuestionModel questionFormModel, Post post, User user) {
		Question question = new Question();
		question.setQuestion(questionFormModel.getQuestion());
		question.setAnswer(questionFormModel.getAnswers().stream().map(String :: toUpperCase).collect(joining(",")));
		question.setPost(post);
		question.setUser(user);
		return question;
	}
	
	@Transactional
	public PostModel answerQuestion(JwtUser jwtUser, QuestionForm questionAnswerForm) {
		LOGGER.info("Inside answerQuestion logged user id - "+ jwtUser.getId());
		final User loggedInUser = userService.loadUserById(jwtUser.getId());
		final Post post = findPostById(questionAnswerForm.getPostId());
		
		List<Question>	qs = questionAnswerForm.getQuestions()
										   .stream().map(QuestionModel ::  getQuestionId).map(this :: loadQuestion)
				                           .filter( p -> Arrays.asList(p.getAnswer().split(","))
						                   .contains(questionAnswerForm.getQuestions().stream()
						        		   .filter( qf -> qf.getQuestionId() == p.getId())
						        		   .findFirst().get().getAnswer()))
				                           .collect(toList());
		
		if(null != qs && qs.size() == questionAnswerForm.getQuestions().size()) {
			LOGGER.info("Answered Correctly ");
			return buildPostModel(post, true, false);
		}
		
		if(null == qs) {
			throwIncorrectAnswer(questionAnswerForm.getQuestions().stream().map(QuestionModel :: getQuestionId).collect(toList()));
		}

		List<ErrorResponse> errors = questionAnswerForm.getQuestions().stream().map(QuestionModel :: getQuestionId)
				.filter(ps ->  ! qs.stream().map(Question :: getId).collect(toList()).contains(ps)).collect(toList()).stream().map( q -> (new ErrorResponse(String.valueOf(q), INCORRECT_ANSWER))).collect(Collectors.toList());

		throw new GenericException(INCORRECT_ANSWER, BAD_REQUEST, errors);		

	}

	public  void throwIncorrectAnswer(List<Integer> answerIds) {
		List<ErrorResponse> errors = answerIds.stream().map( q -> (new ErrorResponse(String.valueOf(q), INCORRECT_ANSWER))).collect(Collectors.toList());
		throw new GenericException(INCORRECT_ANSWER, BAD_REQUEST, errors);		
	}

	@Transactional
	public boolean deletePostByIds(JwtUser jwtUser, List<Integer> postIds) {
		if(isNull(postIds) || postIds.isEmpty()) {
			return false;
		}
		LOGGER.info("Inside deletePostByIds postIds - "+ postIds.toString());
		postRepo.deleteByIds(postIds);
		return true;
	}

	private PostStatus findValidStatus(String status) {
		if("DRAFT".equalsIgnoreCase(status) ) {
			return PostStatus.DRAFT;
		}
		
		if("PUBLISHED".equalsIgnoreCase(status)) {
			return PostStatus.PUBLISHED;
		}
	  return null;			
	}

	public List<PostModel> buildPostModels(Page<Post> posts) {
		return posts.stream()
		            .map(this :: buildPostModelWithContentDetail)
                    .collect(Collectors.toList());
	}

	
}

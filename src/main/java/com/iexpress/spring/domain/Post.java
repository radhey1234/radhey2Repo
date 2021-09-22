package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the post database table.
 * 
 */
@Entity
@Table(name="post")
@NamedQuery(name="Post.findAll", query="SELECT p FROM Post p")
public class Post extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;


	@Column
	private String content;

	@Column(name="is_deleted")
	private boolean isDeleted;

	@Column(name="is_protected")
	private boolean isProtected;

	@Column(name="is_seen")
	private boolean isSeen;

	@Column(name="protected_password", length=255)
	private String protectedPassword;

	@Column(name="tagged_user_first_name", length=95)
	private String taggedUserFirstName;

	@Column(name="tagged_user_last_name", length=95)
	private String taggedUserLastName;

	@Column(name="total_comments")
	private int totalComments;

	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="post")
	private List<Comment> comments;

	//bi-directional many-to-one association to Notification
	@OneToMany(mappedBy="post")
	private List<Notification> notifications;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="creator", nullable=false)
	private User creator;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="tagged_user")
	private User taggedUser;

	//bi-directional many-to-one association to PostSeen
	@OneToMany(mappedBy="post")
	private List<PostSeen> postSeens;

	//bi-directional many-to-one association to Question
	@OneToMany(mappedBy="post")
	private List<Question> questions;

	//bi-directional many-to-one association to Resource
	@OneToMany(mappedBy="post")
	private List<Resource> resources;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private PostStatus status;

	public PostStatus getStatus() {
		return status;
	}

	public void setStatus(PostStatus status) {
		this.status = status;
	}

	public Post() {
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public boolean getIsProtected() {
		return this.isProtected;
	}

	public void setIsProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}

	public boolean getIsSeen() {
		return this.isSeen;
	}

	public void setIsSeen(boolean isSeen) {
		this.isSeen = isSeen;
	}

	public String getProtectedPassword() {
		return this.protectedPassword;
	}

	public void setProtectedPassword(String protectedPassword) {
		this.protectedPassword = protectedPassword;
	}

	public String getTaggedUserFirstName() {
		return this.taggedUserFirstName;
	}

	public void setTaggedUserFirstName(String taggedUserFirstName) {
		this.taggedUserFirstName = taggedUserFirstName;
	}

	public String getTaggedUserLastName() {
		return this.taggedUserLastName;
	}

	public void setTaggedUserLastName(String taggedUserLastName) {
		this.taggedUserLastName = taggedUserLastName;
	}

	public int getTotalComments() {
		return this.totalComments;
	}

	public void setTotalComments(int totalComments) {
		this.totalComments = totalComments;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setPost(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setPost(null);

		return comment;
	}

	public List<Notification> getNotifications() {
		return this.notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public Notification addNotification(Notification notification) {
		getNotifications().add(notification);
		notification.setPost(this);

		return notification;
	}

	public Notification removeNotification(Notification notification) {
		getNotifications().remove(notification);
		notification.setPost(null);

		return notification;
	}

	public User getCreator() {
		return this.creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public User getTaggedUser() {
		return this.taggedUser;
	}

	public void setTaggedUser(User taggedUser) {
		this.taggedUser = taggedUser;
	}

	public List<PostSeen> getPostSeens() {
		return this.postSeens;
	}

	public void setPostSeens(List<PostSeen> postSeens) {
		this.postSeens = postSeens;
	}

	public PostSeen addPostSeen(PostSeen postSeen) {
		getPostSeens().add(postSeen);
		postSeen.setPost(this);

		return postSeen;
	}

	public PostSeen removePostSeen(PostSeen postSeen) {
		getPostSeens().remove(postSeen);
		postSeen.setPost(null);

		return postSeen;
	}

	public List<Question> getQuestions() {
		return this.questions;
	}

	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}

	public Question addQuestion(Question question) {
		getQuestions().add(question);
		question.setPost(this);

		return question;
	}

	public Question removeQuestion(Question question) {
		getQuestions().remove(question);
		question.setPost(null);

		return question;
	}

	public List<Resource> getResources() {
		return this.resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
	}

	public Resource addResource(Resource resource) {
		getResources().add(resource);
		resource.setPost(this);

		return resource;
	}

	public Resource removeResource(Resource resource) {
		getResources().remove(resource);
		resource.setPost(null);

		return resource;
	}

}
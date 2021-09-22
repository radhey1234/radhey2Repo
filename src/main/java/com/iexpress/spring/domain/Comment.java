package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the comment database table.
 * 
 */
@Entity
@Table(name="comment")
@NamedQuery(name="Comment.findAll", query="SELECT c FROM Comment c")
public class Comment extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name="is_deleted")
	private boolean isDeleted;

	@Lob
	@Column(nullable=false)
	private String text;

	//bi-directional many-to-one association to Comment
	@ManyToOne
	@JoinColumn(name="parent_comment_id")
	private Comment comment;

	//bi-directional many-to-one association to Comment
	@OneToMany(mappedBy="comment")
	private List<Comment> comments;

	//bi-directional many-to-one association to Post
	@ManyToOne
	@JoinColumn(name="post_id", nullable=false)
	private Post post;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="commenter", nullable=false)
	private User user;

	//bi-directional many-to-one association to Notification
	@OneToMany(mappedBy="comment")
	private List<Notification> notifications;

	public Comment() {
	}

	public boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Date getModifiedAt() {
		return this.modifiedAt;
	}

	public void setModifiedAt(Date modifiedAt) {
		this.modifiedAt = modifiedAt;
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Comment getComment() {
		return this.comment;
	}

	public void setComment(Comment comment) {
		this.comment = comment;
	}

	public List<Comment> getComments() {
		return this.comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public Comment addComment(Comment comment) {
		getComments().add(comment);
		comment.setComment(this);

		return comment;
	}

	public Comment removeComment(Comment comment) {
		getComments().remove(comment);
		comment.setComment(null);

		return comment;
	}

	public Post getPost() {
		return this.post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Notification> getNotifications() {
		return this.notifications;
	}

	public void setNotifications(List<Notification> notifications) {
		this.notifications = notifications;
	}

	public Notification addNotification(Notification notification) {
		getNotifications().add(notification);
		notification.setComment(this);

		return notification;
	}

	public Notification removeNotification(Notification notification) {
		getNotifications().remove(notification);
		notification.setComment(null);

		return notification;
	}

}
package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the post_seen database table.
 * 
 */
@Entity
@Table(name="post_seen")
@NamedQuery(name="PostSeen.findAll", query="SELECT p FROM PostSeen p")
public class PostSeen extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="seen_at", nullable=false)
	private Date seenAt;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="seen_by", nullable=false)
	private User user;

	//bi-directional many-to-one association to Post
	@ManyToOne
	@JoinColumn(name="post_id", nullable=false)
	private Post post;

	public PostSeen() {
	}

	public Date getSeenAt() {
		return this.seenAt;
	}

	public void setSeenAt(Date seenAt) {
		this.seenAt = seenAt;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Post getPost() {
		return this.post;
	}

	public void setPost(Post post) {
		this.post = post;
	}

}
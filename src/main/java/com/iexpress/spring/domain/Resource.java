package com.iexpress.spring.domain;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the resource database table.
 * 
 */
@Entity
@Table(name="resource")
@NamedQuery(name="Resource.findAll", query="SELECT r FROM Resource r")
public class Resource extends BaseEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Basic
	@Enumerated(EnumType.STRING)
	@Column(name="belongs_to")
	private ProfilePlace belongsTo;

	@Column(name="is_deleted")
	private boolean isDeleted;

	@Basic
	@Enumerated(EnumType.STRING)
	@Column(name="resource_type")
	private ResourceType resourceType;
	
	@Column(name="url")
	@Lob
	private String url;

	//bi-directional many-to-one association to Post
	@ManyToOne
	@JoinColumn(name="post_id")
	private Post post;

	//bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name="uploader", nullable=false)
	private User user;
	
	public Resource() {
	}

	public ProfilePlace getBelongsTo() {
		return this.belongsTo;
	}

	public void setBelongsTo(ProfilePlace belongsTo) {
		this.belongsTo = belongsTo;
	}

	public boolean getIsDeleted() {
		return this.isDeleted;
	}

	public void setIsDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}


	public ResourceType getResourceType() {
		return this.resourceType;
	}

	public void setResourceType(ResourceType resourceType) {
		this.resourceType = resourceType;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}
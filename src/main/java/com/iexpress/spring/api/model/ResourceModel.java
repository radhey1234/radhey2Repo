package com.iexpress.spring.api.model;

import com.iexpress.spring.api.model.builder.ResourceModelBuilder;

import lombok.Getter;


@Getter
public class ResourceModel {
	

	private final int resourceId;
	private final String resourceUrl;
	private final String resourceType;
	private final String createdAt;
	private final String modifiedAt;
	private final String resourceUrlSmall;
	private final String resourceUrlLarge;
	private final String resourceUrlTiny;
	private final String resourceUrlThumbnail;
	private final String belongsTo;

	public ResourceModel(ResourceModelBuilder builder) {
		this.resourceId = builder.getResourceId();
		this.resourceUrl = builder.getResourceUrl();
		this.resourceType = builder.getResourceType();
		this.resourceUrlSmall = builder.getResourceUrlSmall();
		this.resourceUrlLarge = builder.getResourceUrlLarge();
		this.resourceUrlTiny = builder.getResourceUrlTiny();
		this.createdAt = builder.getCreatedAt();
		this.modifiedAt = builder.getModifiedAt();
		this.resourceUrlThumbnail = builder.getResourceUrlTiny();
		this.belongsTo = builder.getBelongsTo();
	}
	
}

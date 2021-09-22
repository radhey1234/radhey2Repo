package com.iexpress.spring.api.model.builder;

import com.iexpress.spring.api.model.ResourceModel;

import lombok.Getter;

@Getter
public class ResourceModelBuilder {

	private String resourceUrl;
	private String resourceType;
	private int resourceId;
	private String createdAt;
	private String modifiedAt;
	private String resourceUrlLarge ;
	private String resourceUrlTiny;
	private String resourceUrlSmall;
	private String belongsTo;

	private ResourceModelBuilder() {
	}
	
	public static ResourceModelBuilder of() {
		return new ResourceModelBuilder();
	}

	public ResourceModelBuilder resourceUrlTiny(String resourceUrlTiny) {
		this.resourceUrlTiny = resourceUrlTiny;
		return this;
	}
	
	public ResourceModelBuilder resourceUrl(String resourceUrl) {
		this.resourceUrl = resourceUrl;
		return this;
	}
	
	public ResourceModelBuilder resourceType(String resourceType) {
		this.resourceType = resourceType;
		return this;
	}
	
	public ResourceModelBuilder resourceId(int resourceId) {
		this.resourceId = resourceId;
		return this;
	}
	
	public ResourceModelBuilder createdAt(String createdAt) {
		this.createdAt = createdAt;
		return this;
	}
	
	public ResourceModelBuilder modifiedAt(String modifiedAt) {
		this.modifiedAt = modifiedAt;
		return this;
	}
	
	public ResourceModel build() {
		return new ResourceModel(this);
	}

	public ResourceModelBuilder belongsTo(String belongsTo) {
		this.belongsTo = belongsTo;
		return this;
	}
}

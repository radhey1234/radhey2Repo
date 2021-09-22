package com.iexpress.spring.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PaginatedResponse<T> {

    @JsonProperty("contentList")
    private T feedModelList;
    private int totalPages;
    private long numberOfResults;
    
	public PaginatedResponse(T feedModelList, int totalPages,
			long numberOfResults) {
		super();
		this.feedModelList = feedModelList;
		this.totalPages = totalPages;
		this.numberOfResults = numberOfResults;
	}

	public PaginatedResponse(T feedModelList) {
		super();
		this.feedModelList = feedModelList;
	}
	
	public PaginatedResponse() {}
}
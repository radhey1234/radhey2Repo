package com.iexpress.spring.api.model;

import lombok.Data;

@Data
public class SearchUserForm {
	private int page;
	private int size;
	private String key;
	private Boolean sortByFirstName;
	private String orderBy;
}

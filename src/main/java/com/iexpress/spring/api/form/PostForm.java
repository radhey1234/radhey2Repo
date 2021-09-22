package com.iexpress.spring.api.form;

import java.util.List;

import lombok.Data;

@Data
public class PostForm {
	
	private int postId;
	private int readerUserId;
	private String readerUserFirstName;
	private String readerUserLastName;
	private String content;
	private String status;
	private List<Integer> resourceIds;
	private List<Integer> questionIds;
	private int page;
	private int size;
}

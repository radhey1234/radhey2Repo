package com.iexpress.spring.api.model;

import java.sql.Date;
import java.util.List;
import com.iexpress.spring.api.model.builder.PostModelBuilder;
import lombok.Data;

@Data
public class PostModel {
	
	private final int id;
	private final int creator;
	private final String content;
	private final List<String> urls;
	private final List<QAModel> qas;
	private final boolean seen;
	private final Date seenAt;
	private final String status;
	private final String createdAt;
	private final String updatedAt;
	private final String readerFirstName;
	private final String readerLastName;
	private int readerUserId;
	List<QuestionModel> questions;

	
	public PostModel(final PostModelBuilder builder) {
		this.id = builder.getId();
		this.creator = builder.getCreator();
		this.content = builder.getContent();
		this.urls = builder.getUrls();
		this.qas = builder.getQas();
		this.seen = builder.isSeen();
		this.seenAt = builder.getSeenAt();
		this.status = builder.getStatus();
		this.createdAt = builder.getCreatedAt();
		this.readerFirstName = builder.getReaderFirstName();
		this.readerLastName = builder.getReaderLastName();
		this.readerUserId = builder.getReaderUserId();
		this.questions = builder.getQuestions();
		this.updatedAt = builder.getUpdatedAt();
	}

}

package com.iexpress.spring.api.model;

import java.util.List;

import com.iexpress.spring.api.model.builder.QuestionModelBuilder;

import lombok.Data;

@Data
public class QuestionModel {
	
	private String question;
	private List<String> answers;
	private String answer; //only for input from form
	private int questionId;
	private int postId;
	private String createdAt;
	private String updatedAt;
	
	public QuestionModel(QuestionModelBuilder qBuilder) {
		this.question = qBuilder.getQuestion();
		this.answers = qBuilder.getAnswers();
		this.questionId = qBuilder.getQuestionId();
		this.postId = qBuilder.getPostId();
		this.createdAt = qBuilder.getCreatedAt();
		this.updatedAt = qBuilder.getUpdatedAt();
	}
	
	public QuestionModel() {
		
	}

}

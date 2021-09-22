package com.iexpress.spring.api.model.builder;

import java.util.List;

import com.iexpress.spring.api.model.QuestionModel;

import lombok.Getter;

@Getter
public class QuestionModelBuilder {
	
	private String question;
	private List<String> answers;
	private int questionId;
	private int postId;
	private String createdAt;
	private String updatedAt;

	public static QuestionModelBuilder of() {
		return new QuestionModelBuilder();
	}

	public QuestionModelBuilder question(String question) {
		this.question = question;
		return this;
	}
	
	public QuestionModelBuilder answers(List<String> answers) {
		this.answers = answers;
		return this;
	}

	public QuestionModelBuilder postId(int postId) {
		this.postId = postId;
		return this;
	}
	
	public QuestionModelBuilder questionId(int questionId) {
		this.questionId = questionId;
		return this;
	}
	
	public QuestionModelBuilder createdAt(String createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public QuestionModelBuilder updatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
		return this;
	}

	public QuestionModel build() {
		return new QuestionModel(this);
	}
}

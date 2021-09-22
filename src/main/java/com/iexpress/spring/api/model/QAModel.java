package com.iexpress.spring.api.model;

import lombok.Getter;

@Getter
public class QAModel {
	
	private String question;
	private String answer;
	
	public QAModel(String question, String answer) {
		this.question = question;
		this.answer = answer;
	}
}

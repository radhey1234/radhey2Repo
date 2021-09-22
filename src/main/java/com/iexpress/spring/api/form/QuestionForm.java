package com.iexpress.spring.api.form;

import java.util.List;

import com.iexpress.spring.api.model.QuestionModel;

import lombok.Data;

@Data
public class QuestionForm {

	private List<QuestionModel> questions;
	private int postId;
	private List<Integer> questionIds;
	private int page;
	private int size;

}

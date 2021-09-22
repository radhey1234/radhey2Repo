package com.iexpress.spring.api.model.builder;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import com.iexpress.spring.api.model.PostModel;
import com.iexpress.spring.api.model.QAModel;
import com.iexpress.spring.api.model.QuestionModel;

import lombok.Getter;

@Getter
public class PostModelBuilder {
	
	private int id;
	private int creator;
	private String content;
	private List<String> urls;
	private List<QAModel> qas;
	private boolean seen;
	private Date seenAt;
	private String status;
	private String createdAt;
	private String readerFirstName;
	private String readerLastName;
	private int readerUserId;
	private String updatedAt;
	List<QuestionModel> questions;

	public PostModelBuilder seen(final boolean seen) {
		this.seen = seen;
		return this;
	}
	
	public PostModelBuilder id(final int id) {
		this.id = id;
		return this;
	}
	
	public PostModelBuilder creator(final int creator) {
		this.creator = creator;
		return this;
	}
	
	public PostModelBuilder content(final String content) {
		this.content = content;
		return this;
	}
	
	public PostModelBuilder urls(final List<String> urls) {
		this.urls = urls;
		return this;
	}
	
	public PostModelBuilder qas(QAModel qaModel) {
		if(null == qas) {
			qas = new ArrayList<QAModel>();
		}
		 qas.add(qaModel);
		 return this;
	}

	public static PostModelBuilder of() {
		return new PostModelBuilder();
	}
	

	public PostModel build() {
		return new PostModel(this);
	}

	public PostModelBuilder status(String status) {
		this.status = status;
		return this;
	}

	public PostModelBuilder createdAt(String dateAndTime) {
		this.createdAt = dateAndTime;
		return this;
	}

	public PostModelBuilder updatedAt(String dateAndTime) {
		this.updatedAt = dateAndTime;
		return this;
	}

	public PostModelBuilder readerFirstName(String  readerFirstName) {
		this.readerFirstName = readerFirstName;
		return this;
	}

	public PostModelBuilder readerLastName(String  readerLastName) {
		this.readerLastName = readerLastName;
		return this;
	}

	public PostModelBuilder readerUserId(int  readerUserId) {
		this.readerUserId = readerUserId;
		return this;
	}

	public PostModelBuilder question(List<QuestionModel> questions) {
		this.questions = questions;
		return this;
	}

}

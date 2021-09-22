package com.iexpress.spring.api.form;

import java.util.List;

import lombok.Data;

@Data
public class ResourceIdForm {
	
	int id;
	List<Integer> ids;
}

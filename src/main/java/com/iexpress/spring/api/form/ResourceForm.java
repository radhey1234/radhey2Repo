package com.iexpress.spring.api.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;
import lombok.Data;

@Data
public class ResourceForm {

	private List<MultipartFile> resource;
	private String belongsTo;
	
}

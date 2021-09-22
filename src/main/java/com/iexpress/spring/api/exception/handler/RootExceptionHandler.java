package com.iexpress.spring.api.exception.handler;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iexpress.spring.api.exception.GenericException;
import com.iexpress.spring.api.response.ErrorResponse;
import com.iexpress.spring.api.response.ResponseEnvelop;
import com.iexpress.spring.api.util.RestConstant;

@RestControllerAdvice
@PropertySource({"classpath:msg.properties"})
public class RootExceptionHandler   {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(RootExceptionHandler.class);
	@Autowired
	private Environment env;
	
	@ExceptionHandler(value = AuthenticationCredentialsNotFoundException.class)
	public ResponseEnvelop<ObjectNode>  handleAuthenticationCredentialsNotFoundException(Exception ex) {
		LOGGER.error("Handle Exception "+ ex);
		ex.printStackTrace();
	    return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), "Invalid Authentication ",
				HttpStatus.UNAUTHORIZED.value(), Collections.emptyList());
	}
	
	@ExceptionHandler(value = NoHandlerFoundException.class)
	public ResponseEnvelop<ObjectNode>  handleNotFoundException(NoHandlerFoundException ex) {
		LOGGER.error("Handle Exception "+ ex);
		ex.printStackTrace();
		return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), env.getProperty(RestConstant.PAGE_NOT_FOUND),
				HttpStatus.NOT_FOUND.value(), Collections.emptyList());
	}
	
	@ExceptionHandler(value= HttpRequestMethodNotSupportedException.class)
	public ResponseEnvelop<ObjectNode>  handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
		LOGGER.error("Handle Exception "+ ex);
		ex.printStackTrace();
		return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), env.getProperty(RestConstant.METHOD_NOT_SUPPORTED),
				HttpStatus.METHOD_NOT_ALLOWED.value(), Collections.emptyList());
	}

	@ExceptionHandler(value= ServletException.class )
	public ResponseEnvelop<ObjectNode> handleServletException(ServletException ex){
		LOGGER.error("Handle Exception "+ ex);
		ex.printStackTrace();
		return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), env.getProperty(RestConstant.PAGE_NOT_FOUND),
				HttpStatus.NOT_FOUND.value(), Collections.emptyList());
	}
	
	@ExceptionHandler(value = Exception.class)
	@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
	public ResponseEnvelop<ObjectNode>  handleException(Exception ex) {
		LOGGER.error("Inside handleException "+ ex.toString());
		ex.printStackTrace();
		return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), "Something went wrong", 
				HttpStatus.INTERNAL_SERVER_ERROR.value(), Collections.emptyList());
	}
	
	@ExceptionHandler(value = GenericException.class)
	@ResponseStatus(code =  HttpStatus.BAD_REQUEST)
	public ResponseEnvelop<ObjectNode>  handleGenericException(GenericException ex) {
		LOGGER.error("Handle GenericException "+ ex);
		List<ErrorResponse> errors =  Collections.emptyList();
		ex.printStackTrace();
		
		if(!StringUtils.isEmpty(ex.getField())) {
			errors = new ArrayList<>();
			errors.add(new ErrorResponse(env.getProperty(ex.getField()), env.getProperty(ex.getFieldMessage())));
		}
		
		if(null != ex.getErrors() && !ex.getErrors().isEmpty()) {
			errors = ex.getErrors();
			errors.stream().forEach(e -> e.setErrorMessage(env.getProperty(e.getErrorMessage())));
		}
		
		return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(),
				env.getProperty(ex.getMessage()), HttpStatus.BAD_REQUEST.value(), errors);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEnvelop<ObjectNode> handleValidationExceptions(MethodArgumentNotValidException ex) {
		
		LOGGER.error("Inside handleValidationExceptions",ex);
		ex.printStackTrace();

	    List<ErrorResponse> errors = new ArrayList<>();
	    
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        
	    	String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();

	        errors.add(new ErrorResponse(fieldName, env.getProperty(errorMessage)));
	    });
	    
	    return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), "Please correct input", 
	    		HttpStatus.BAD_REQUEST.value(), errors);
	}
}

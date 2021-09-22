package com.iexpress.spring.api.exception.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iexpress.spring.api.response.ResponseEnvelop;

@Component
public class FailedAuthenticationHandler implements AuthenticationEntryPoint {

	private static final Logger LOGGER = LoggerFactory.getLogger(FailedAuthenticationHandler.class);

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		LOGGER.debug("Failed Authentication is called");
		response.setContentType("application/json;charset=UTF-8");
		response.setStatus(401);
	
        ObjectMapper mapper = new ObjectMapper();

        ResponseEnvelop<ObjectNode> responseObject = new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), " You are not logged in ! Please login !!", HttpStatus.UNAUTHORIZED.value());
        final String responseMsg = mapper.writeValueAsString(responseObject);
        response.getWriter().write(responseMsg);

	}

}

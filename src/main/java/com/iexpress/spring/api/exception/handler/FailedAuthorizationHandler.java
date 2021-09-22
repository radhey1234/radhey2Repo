package com.iexpress.spring.api.exception.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iexpress.spring.api.response.ResponseEnvelop;

@Component
public class FailedAuthorizationHandler implements AccessDeniedHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(FailedAuthorizationHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        LOGGER.error("Exception", accessDeniedException);
        
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");        
        response.setStatus(HttpStatus.FORBIDDEN.value());
        ObjectMapper mapper = new ObjectMapper();

        ResponseEnvelop<ObjectNode> responseObject = new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), "You are not authrized to access this resource ! Please login", HttpStatus.FORBIDDEN.value());
        final String responseMsg = mapper.writeValueAsString(responseObject);
        response.getWriter().write(responseMsg);
    }

}
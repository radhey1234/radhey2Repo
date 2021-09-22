package com.iexpress.spring.api.auth.contoller;

import static com.iexpress.spring.api.util.RestConstant.LOG_OUT;
import static com.iexpress.spring.api.util.RestConstant.OK;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iexpress.spring.api.response.ResponseEnvelop;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.jwt.JwtUser;

@RestController
public class LogoutController {

	private static final Logger LOGGER = LoggerFactory.getLogger(LogoutController.class);
	@Autowired private AuthenticationControllerFacade facade;
	
	@PostMapping("/api/v1/logout")
	public ResponseEnvelop<ObjectNode> logOut(JwtUser user){
		LOGGER.info("Inside Log out user id "+ user.getId());
		facade.logOut(user);
		return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), 
				AppUtil.of().getEnv().getProperty(LOG_OUT), OK);
	}

}

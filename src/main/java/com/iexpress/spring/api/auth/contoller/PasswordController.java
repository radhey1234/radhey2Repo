package com.iexpress.spring.api.auth.contoller;

import static com.iexpress.spring.api.util.RestConstant.OK;
import static com.iexpress.spring.api.util.RestConstant.PASSWORD_IS_CHANGED;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.iexpress.spring.api.form.ChangeOrResetPasswordForm;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.response.ResponseEnvelop;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.jwt.JwtUser;

@RestController
public class PasswordController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PasswordController.class);

	@Autowired private AuthenticationControllerFacade facade;

	@PutMapping("/api/v1/password")
	public <T> ResponseEnvelop<UserModel> changePassword(JwtUser jwtUser, @RequestBody  ChangeOrResetPasswordForm changeOrResetPasswordForm){
		LOGGER.info("Inside changePassword "+ jwtUser.getId());
		final UserModel model = facade.changePassword(jwtUser.getId(), changeOrResetPasswordForm);
		return new ResponseEnvelop<UserModel>(model, resolveToString(PASSWORD_IS_CHANGED), OK);
	}
	
	@PostMapping("/api/v1/password")
	public <T> ResponseEnvelop<UserModel> resetPassword(@RequestBody ChangeOrResetPasswordForm changeOrResetPasswordForm){
		LOGGER.info("Inside resetPassword ");
		final UserModel model = facade.resetPassword( changeOrResetPasswordForm);
		return new ResponseEnvelop<UserModel>(model,  resolveToString(PASSWORD_IS_CHANGED), OK);
	}

	private String resolveToString(String input) {
		return AppUtil.of().getEnv().getProperty(input);
	}

}

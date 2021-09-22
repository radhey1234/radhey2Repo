package com.iexpress.spring.api.auth.contoller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.iexpress.spring.api.auth.service.AuthenticationService;
import com.iexpress.spring.api.form.ChangeOrResetPasswordForm;
import com.iexpress.spring.api.form.RegisterForm;
import com.iexpress.spring.api.form.SignInForm;
import com.iexpress.spring.api.form.SignUpForm;
import com.iexpress.spring.api.model.UserModel;
import static com.iexpress.spring.api.util.AppUtil.*;
import com.iexpress.spring.jwt.JwtUser;

@Component
public class AuthenticationControllerFacade {

	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationControllerFacade.class);
	private AuthenticationService authService;
	
	
	@Autowired
	public AuthenticationControllerFacade(AuthenticationService authService){
		this.authService = authService;
	}
	
	public UserModel signUp( SignUpForm signUpForm) {
		LOGGER.info("Inside sign up");
		
		UserModel userModel = null;
		
		if(signUpForm.getVerifiedMobileOrEmailToken()!=null) {
			authService.overrideInputValuesIfRequiredFromVerificationToken(signUpForm);
		}
		
		if(isPhone(signUpForm.getInputType())) {
			userModel = authService.signUpByPhone(signUpForm);
		}
		
		if(isEmail(signUpForm.getInputType())) {
			userModel = authService.signUpByEmail(signUpForm);			
		}
		
		return userModel;
	}


	public UserModel signIn(final SignInForm signInform) {
		UserModel userModel = null;
		
		if(isPhone(signInform.getInputType())) {
			userModel = authService.signInByPhone(signInform.getCountryCode(), signInform.getPhoneNumber(), 
					signInform.getPassword(), signInform.getDeviceType(), 
					signInform.getDeviceToken());
		}
		
		if(isEmail(signInform.getInputType())) {
			userModel = authService.signInByEmail( signInform.getEmail(), signInform.getPassword(), 
					signInform.getDeviceType(), signInform.getDeviceToken());			
		}
		return userModel;
	}
	

	public UserModel changePassword(int userId, ChangeOrResetPasswordForm changeOrResetPasswordForm) {
		return authService.changePassword(userId, changeOrResetPasswordForm.getCurrentPassword(), changeOrResetPasswordForm.getNewPassword()
				, changeOrResetPasswordForm.getConfirmPassword());
	}

	public UserModel resetPassword(ChangeOrResetPasswordForm changeOrResetPasswordForm) {
		return authService.resetPassword(changeOrResetPasswordForm);
	}

	public void logOut(JwtUser user) {
		authService.logOut(user);
	}


	public String register( RegisterForm registrationForm) {
		return authService.register(registrationForm);
	}
	
}

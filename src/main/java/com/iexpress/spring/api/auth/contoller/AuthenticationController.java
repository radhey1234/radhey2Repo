package com.iexpress.spring.api.auth.contoller;


import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.iexpress.spring.api.form.RegisterForm;
import com.iexpress.spring.api.form.SignInForm;
import com.iexpress.spring.api.form.SignUpForm;
import com.iexpress.spring.api.form.VerificationTokenForm;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.response.ResponseEnvelop;
import com.iexpress.spring.api.util.AppUtil;
import com.iexpress.spring.api.validator.FormType;
import com.iexpress.spring.api.validator.ValidateUtil;
import static com.iexpress.spring.api.util.RestConstant.*;

@RestController
public class AuthenticationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

	@Autowired private AuthenticationControllerFacade facade;
	
	@PostMapping("/api/v1/signup")
	public <T> ResponseEnvelop<UserModel> signUp(@RequestBody SignUpForm signUpForm){
		LOGGER.info("Inside signUp "+ signUpForm.getInputType());
		validateSignUpForm(signUpForm);
		UserModel model = facade.signUp(signUpForm);
		return new ResponseEnvelop<UserModel>(model, resolveToString(SIGN_UP_SUCCESS), OK);
	}
	
	@PostMapping("/api/v1/register")
	public <T> ResponseEnvelop<VerificationTokenForm> register(@Valid @RequestBody RegisterForm registrationForm){
		LOGGER.info("Inside register ");
		String randomeToken = facade.register(registrationForm);
		return new ResponseEnvelop<VerificationTokenForm>(new VerificationTokenForm(randomeToken), resolveToString(PLEASE_VERIFY_NUMBER), OK);		
	}
	
	@CrossOrigin
	@PostMapping("/api/v1/signin")
	public <T> ResponseEnvelop<UserModel> signIn(@RequestBody SignInForm signInform){
		LOGGER.info("Inside signin "+ signInform.getInputType());
		validateSignUpForm(signInform);
		UserModel model = facade.signIn(signInform);
		return new ResponseEnvelop<UserModel>(model, resolveToString(SIGN_IN_SUCCESSFULL), OK);
	}
	
	private String resolveToString(String input) {
		return AppUtil.of().getEnvProperty(input);
	}

	private void validateSignUpForm(SignUpForm signUpForm) {
		if("Email".equalsIgnoreCase(signUpForm.getInputType()))
			ValidateUtil.of().validateForm(signUpForm, FormType.SIGNUP_EMAIL);
		else
			ValidateUtil.of().validateForm(signUpForm, FormType.SIGNUP_PHONE);
	}

}
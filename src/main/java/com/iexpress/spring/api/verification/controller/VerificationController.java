package com.iexpress.spring.api.verification.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iexpress.spring.api.form.OtpVerificationForm;
import com.iexpress.spring.api.form.ResentEmailOrOTPForm;
import com.iexpress.spring.api.form.VerificationTokenForm;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.api.response.ResponseEnvelop;
import com.iexpress.spring.api.util.AppUtil;

import static com.iexpress.spring.api.util.RestConstant.*;

@RestController
public class VerificationController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(VerificationController.class);

	@Autowired private VerificationFacade verificationFacade;
	
	@PostMapping("/api/v1/verify_otp")
	public <T> ResponseEnvelop<UserModel> verifyOtp(@Valid @RequestBody OtpVerificationForm otpVerificationForm){
		LOGGER.info("Inside verifyOtp "+ otpVerificationForm.getPhoneNumber() + " user Id"+ otpVerificationForm.getUserId());
		return buildResponseUserModel(verificationFacade.verifyOtp(otpVerificationForm),VERIFICATION_SUCCESSFULL,OK);
	}

	@PostMapping("/api/v1/verify_initial_otp")
	public <T> ResponseEnvelop<VerificationTokenForm> verifyIntialOtp(@Valid @RequestBody OtpVerificationForm otpVerificationForm){
		LOGGER.info("Inside verifyOtp "+ otpVerificationForm.getPhoneNumber() + " user Id"+ otpVerificationForm.getUserId());
		return new ResponseEnvelop<VerificationTokenForm>(new VerificationTokenForm(verificationFacade.verifyIntialOtp(otpVerificationForm)),VERIFICATION_SUCCESSFULL,OK);
	}
	
	@PostMapping({"/api/v1/resend_otp"})
	public <T> ResponseEnvelop<ObjectNode> resentOtp(@RequestBody ResentEmailOrOTPForm resentOtpForm){
		LOGGER.info("Inside resentOtp  "+ resentOtpForm.getInputType());
		boolean isVerified = verificationFacade.resendOtpOrEmailLink(resentOtpForm);
		return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), resolveToString(isVerified ? ALREADY_VERIFIED : SENT_AGAIN), 
				OK);
	}
	
	@PostMapping({"/api/v1/resend_email"})
	public <T> ResponseEnvelop<ObjectNode> resentEmail(@RequestBody ResentEmailOrOTPForm resentOtpForm){
		LOGGER.info("Inside resentOtp  "+ resentOtpForm.getInputType());
		boolean isVerified = verificationFacade.resendOtpOrEmailLink(resentOtpForm);
		return new ResponseEnvelop<ObjectNode>(new ObjectMapper().createObjectNode(), resolveToString(isVerified ? ALREADY_VERIFIED : SENT_AGAIN),
				OK);
	}
	
	@GetMapping("/api/v1/verify_email")
	public String verifyEmail(@RequestParam ("randomeToken") String randomeToken, @RequestParam ("id") int id){
		boolean isVerified = verificationFacade.verifyEmailLink(randomeToken, id);
		if(isVerified) {
			return resolveToString(VERIFICATION_SUCCESSFULL);
		}
		return resolveToString(VERIFICATION_UNSUCCESSFULL);		
	}
	
	private ResponseEnvelop<UserModel> buildResponseUserModel(final UserModel userModel,final String msg, int statusCode) {
		return new ResponseEnvelop<UserModel>( userModel, resolveToString(msg), statusCode);
	}

	private String resolveToString(String input) {
		return AppUtil.of().getEnv().getProperty(input);
	}
}

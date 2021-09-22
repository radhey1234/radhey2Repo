package com.iexpress.spring.api.auth.service;


import org.springframework.stereotype.Service;
import com.iexpress.spring.api.form.ChangeOrResetPasswordForm;
import com.iexpress.spring.api.form.RegisterForm;
import com.iexpress.spring.api.form.SignUpForm;
import com.iexpress.spring.api.model.UserModel;
import com.iexpress.spring.jwt.JwtUser;

@Service
public interface AuthenticationService {
		
	public UserModel signInByPhone(final String countryCode, final String phoneNumber,final String password,final String deviceType,
			final String deviceToken);
	
	public UserModel signInByEmail(final String email,final String password,final String deviceType,
			final String deviceToken);
	
	public UserModel signUpByEmail(SignUpForm signUpForm);
	
	public UserModel signUpByPhone(SignUpForm signUpform);
	
	public UserModel changePassword(int userId, String currentPassword, String newPassword, String confirmPassword);
	
	public UserModel resetPassword(ChangeOrResetPasswordForm changeOrResetPasswordForm);

	public void logOut(JwtUser user);

	public String register( RegisterForm registrationForm);

	void overrideInputValuesIfRequiredFromVerificationToken(SignUpForm signUpForm);
	
}


package com.iexpress.spring.config.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.iexpress.spring.api.exception.handler.FailedAuthenticationHandler;
import com.iexpress.spring.api.exception.handler.FailedAuthorizationHandler;
import com.iexpress.spring.filter.JwtFilter;

@Configuration
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {
	private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfiguration.class);
	
	@Autowired JwtFilter jwtFilter;
	@Autowired FailedAuthorizationHandler failedAuthorizationHandler;
	@Autowired FailedAuthenticationHandler failedAuthenticationHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		LOGGER.debug("Inside configure(HttpSecurity).");
		

//		http.authorizeRequests().anyRequest().permitAll();
		
			http
				.authorizeRequests()
				.antMatchers("/resources/**","/postmanApi").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/signin").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/verify_otp").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/signup").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/resend_otp").permitAll()
				.antMatchers(HttpMethod.POST, "/api/v1/resend_email").permitAll()
				.antMatchers(HttpMethod.GET, "/api/v1/verify_email").permitAll()
				.antMatchers(HttpMethod.GET, "/api/v1/state").permitAll()
				.antMatchers(HttpMethod.GET, "/api/v1/city").permitAll()
				.antMatchers(HttpMethod.POST,"/api/v1/register").permitAll()
				.antMatchers(HttpMethod.POST,"/api/v1/verify_initial_otp").permitAll()
				.anyRequest().authenticated();
		
		http.csrf().disable();
		
		http
			.exceptionHandling()
			.accessDeniedHandler(failedAuthorizationHandler)
			.authenticationEntryPoint(failedAuthenticationHandler);
		
		http.cors().configurationSource(corsConfigurationSource());	
		http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
	}
	
	//This can be customized as required
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    List<String> allowOrigins = Arrays.asList("*");
	    configuration.setAllowedOrigins(allowOrigins);
	    configuration.setAllowedMethods(Collections.singletonList("*"));
	    configuration.setAllowedHeaders(Collections.singletonList("*"));
	    //in case authentication is enabled this flag MUST be set, otherwise CORS requests will fail
	    configuration.setAllowCredentials(true);
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}

}

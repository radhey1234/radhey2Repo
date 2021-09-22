package com.iexpress.spring.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.iexpress.spring.filter.JwtFilter;

@Configuration
@EnableAsync
public class RootConfiguration {

	@Autowired Environment env;
	
	@Bean
	public FilterRegistrationBean<JwtFilter> loggingFilter(){
	    FilterRegistrationBean<JwtFilter> registrationBean  = new FilterRegistrationBean<>();
	    registrationBean.setFilter(new JwtFilter());
	    registrationBean.setEnabled(false);
	    return registrationBean;
	}
	
	@Bean("passwordEncoderBean")
    public PasswordEncoder passwordEncoder() {
    	return new BCryptPasswordEncoder();
    }
	
	@Bean
    public AmazonS3 s3(){
    	BasicAWSCredentials awsCreds = new BasicAWSCredentials(env.getProperty("accessKey"), env.getProperty("secretAccessKey"));
		return AmazonS3ClientBuilder.standard()
				.withRegion(Regions.AP_SOUTH_1)
				.withCredentials(new AWSStaticCredentialsProvider(awsCreds))
				.build();
    }
	
}

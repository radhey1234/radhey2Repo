package com.iexpress.spring.api.auth.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.iexpress.spring.api.auth.service.LoginDetailService;
import com.iexpress.spring.api.repo.LoginDetailServiceRepo;
import com.iexpress.spring.domain.LogInBy;
import com.iexpress.spring.domain.LoginDetail;
import com.iexpress.spring.domain.User;

@Service
public class LoginDetailServiceImpl implements LoginDetailService {

	private LoginDetailServiceRepo loginDetailServiceRepo;
	
	@Autowired
	public LoginDetailServiceImpl(LoginDetailServiceRepo loginDetailServiceRepo) {
		this.loginDetailServiceRepo = loginDetailServiceRepo;
	}
	
	
	@Override
	public LoginDetail addLoginDetailsForRecords(LogInBy loginBy, User userExist, String deviceType, Date date) {
		LoginDetail loginDetail = new LoginDetail();
		loginDetail.setUser(userExist);
		loginDetail.setLoginAt(new Date());
		loginDetail.setLoginPlateform(deviceType);
		loginDetail.setLoginBy(loginBy);
		loginDetailServiceRepo.save(loginDetail);
		return loginDetail;
	}
	
	@Override
	public LoginDetail addLogoutDetailsForRecords(LogInBy loginBy, User userExist, String deviceType, Date date) {
		LoginDetail loginDetail = new LoginDetail();
		loginDetail.setUser(userExist);
		loginDetail.setLoginPlateform(deviceType);
		loginDetail.setLoginBy(loginBy);
		loginDetail.setLogoutAt(new Date());
		loginDetailServiceRepo.save(loginDetail);
		return loginDetail;
	}
}

package com.iexpress.spring.api.auth.service;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.iexpress.spring.domain.LogInBy;
import com.iexpress.spring.domain.LoginDetail;
import com.iexpress.spring.domain.User;

@Service
public interface LoginDetailService {

	public LoginDetail addLoginDetailsForRecords(LogInBy loginBy, User userExist, String deviceType, Date date);

	public LoginDetail addLogoutDetailsForRecords(LogInBy loginBy, User userExist, String deviceType, Date date);
}

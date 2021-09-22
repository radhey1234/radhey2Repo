package com.iexpress.spring.api.device.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iexpress.spring.api.device.service.DeviceDetailService;
import com.iexpress.spring.api.repo.DeviceDetailRepo;
import com.iexpress.spring.domain.DeviceDetail;

@Service
public class DeviceDetailServiceImpl implements DeviceDetailService{

	final private DeviceDetailRepo deviceDetailRepo;
	
	public DeviceDetailServiceImpl(DeviceDetailRepo deviceDetailRepo) {
		this.deviceDetailRepo = deviceDetailRepo;
	}
	

	@Override
	@Transactional
	public boolean deleteDeviceToken(String deviceToken) {
		 deviceDetailRepo.deleteDeviceToken(deviceToken);;
		 return true;
	}

	@Override
	@Transactional
	public DeviceDetail saveDeviceDetail(DeviceDetail device) {
		deleteDeviceToken(device.getDeviceToken());
		return deviceDetailRepo.save(device);
	}


	@Override
	@Transactional
	public void deleteByUserIdAndDeviceToken(int userId, String deviceToken) {
		deviceDetailRepo.deleteByUserIdAndDeviceToken(userId, deviceToken);
	}

}

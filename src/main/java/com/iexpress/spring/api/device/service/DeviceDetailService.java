package com.iexpress.spring.api.device.service;

import org.springframework.stereotype.Service;
import com.iexpress.spring.domain.DeviceDetail;

@Service
public interface DeviceDetailService {

	public boolean deleteDeviceToken(String deviceToken);
	
	public DeviceDetail saveDeviceDetail(DeviceDetail device);

	public void deleteByUserIdAndDeviceToken(int userId, String deviceToken);
	
}

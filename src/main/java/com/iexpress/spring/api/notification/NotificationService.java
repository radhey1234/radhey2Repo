package com.iexpress.spring.api.notification;

import java.util.List;

import org.springframework.stereotype.Service;

import com.iexpress.spring.api.model.NotificationModel;
import com.iexpress.spring.domain.DeviceDetail;

@Service
public interface NotificationService {

	void pushNotification(List<DeviceDetail> devices, NotificationModel notificationModel);

}

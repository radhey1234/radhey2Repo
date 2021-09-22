package com.iexpress.spring.api.notification;

import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iexpress.spring.api.model.NotificationModel;
import com.iexpress.spring.api.repo.NotificationRepo;
import com.iexpress.spring.domain.DeviceDetail;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NotificationServiceImpl implements NotificationService {

	private static final RestTemplate restTemplate = new RestTemplate();
	private static final RestTemplate restTemplateForFCM = new RestTemplate();
	private static final Logger LOGGER = LoggerFactory.getLogger(NotificationServiceImpl.class);

	private String ONE_SIGNAL_ENDPOINT = null;
	private String FCM_END_POINT = null;
	private String FCM_SERVER_API_KEY = null;
	private String ONE_SIGNAL_REST_API_KEY = null;
	private String ONE_SIGNAL_REST_API_ID = null;
	
	private NotificationRepo notificationRepo;
	
	@Autowired
	public NotificationServiceImpl(Environment env, NotificationRepo repo) {
		this.notificationRepo = repo;		
		this.ONE_SIGNAL_REST_API_ID = env.getProperty("ONE_SIGNAL_REST_API_ID");
		this.ONE_SIGNAL_REST_API_KEY = env.getProperty("ONE_SIGNAL_REST_API_KEY");
		this.FCM_SERVER_API_KEY = env.getProperty("FCM_SERVER_API_KEY");
		this.ONE_SIGNAL_ENDPOINT = env.getProperty("ONE_SIGNAL_ENDPOINT");
		this.FCM_END_POINT = env.getProperty("FCM_END_POINT");
	}
	
	@Override
	@Transactional
	public void pushNotification(List<DeviceDetail> devices, NotificationModel notificationModel){
		LOGGER.info("Inside pushNotification :: Choosing device type to send notification");
		for(DeviceDetail device : devices){
		String deviceName = device.getDeviceType();
		if("WEB".equalsIgnoreCase(deviceName) || "ANDROID".equalsIgnoreCase(deviceName)) {
		   pushNotification(notificationModel,Arrays.asList(device.getDeviceToken()));
		}

		if("IOS".equalsIgnoreCase(deviceName)) {
			pushNotificationForIOSFcm( notificationModel, Arrays.asList(device.getDeviceToken()));
		}
	   }
     }

	public void pushNotification(NotificationModel notificationModel, List<String> playerIds) {
		LOGGER.info("Inside pushNotification :: Sending push notification using One Signal");
		
		final String modelAsJSONString = convertModelToStringJSON(notificationModel);
		
		LOGGER.info("Notification being sent to {} from {} message {}",notificationModel.getUserId(), 
				notificationModel.getSenderId(), notificationModel.getNotificationMessage());
		
		for(String playerId : playerIds){
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", ONE_SIGNAL_REST_API_KEY);    
			headers.set("Content-Type", "application/json");
			
			String input = "{ \"include_player_ids\":[\""+playerId+"\"], "
					+ "\"app_id\":\""+ONE_SIGNAL_REST_API_ID+"\","
					+ "\"small_icon\":\"notificationicon\","
					+ "\"contents\":{\"en\":\""+notificationModel.getNotificationMessage()+"\"},"
					+ "\"data\":{\"model\":"+modelAsJSONString+"	}}";
			
			LOGGER.info("+============Input====== {}", input);
			HttpEntity< String > entity = new HttpEntity<>(input, headers);
			try{
				String res = restTemplate.postForObject(ONE_SIGNAL_ENDPOINT, entity, String.class);
				LOGGER.info("Notification Message Response == {}",res);
			}catch(Exception e){
				LOGGER.error("Problem encountered while sending notification player id {} Error {}",playerId, e);
			}
		}
		
	}


	public void pushNotificationForIOSFcm(NotificationModel notificationModel, List<String> playerIds) {
		LOGGER.info("Inside pushNotificationForIOSFcm :: Sending iOS push from FCM");
		
		final String modelAsJSONString = convertModelToStringJSON(notificationModel);
		
		LOGGER.info("Notification being sent to {} from {} message {}",notificationModel.getUserId(), 
				notificationModel.getSenderId(), notificationModel.getNotificationMessage());
		
		for(String playerId : playerIds){
			
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization",  FCM_SERVER_API_KEY);    
			headers.set("Content-Type", "application/json");
			
			String input = "{ \"to\":\""+playerId+"\", "
					+ "\"priority\":\"high\","
					+ "\"notification\":{"
					+ "\"title\":\""+notificationModel.getNotificationMessage()+"\","
					+ "\"click_action\":\"FCM_PLUGIN_ACTIVITY\","
					+ "\"icon\":\"fcm_push_icon\","
					+ "\"sound\":\"default\","
					+ "\"smallIcon\":\"notificationicon\","
					+ "\"badge\": "+notificationModel.getNotificationBadgeCount()+"},"
					+ "\"data\":{\"model\":"+modelAsJSONString+"	}}";
			
			HttpEntity< String > entity = new HttpEntity<>(input, headers);
			try{
			String res = restTemplateForFCM.postForObject(FCM_END_POINT, entity, String.class);
			LOGGER.info("Notification Message Response == {}",res);
			}catch(Exception e){
				LOGGER.error("Problem encountered while sending notification to iOS  device token {} Error {}",playerId, e);
			}
		}
	}

	
	
	private String convertModelToStringJSON(NotificationModel notificationModel) {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(notificationModel);
		} catch (JsonProcessingException e) {
			LOGGER.error("Error while converting Object to String {}",e);
		}
		return null;
	}



}

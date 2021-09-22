package com.iexpress.spring.api.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.TwilioRestException;
import com.twilio.sdk.resource.factory.MessageFactory;
import com.twilio.sdk.resource.instance.Message;

@Component
public class SmsServiceUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(SmsServiceUtil.class);
	@Autowired
	private Environment env;
	
	@Async
	public boolean sendMessage (String accountSID,String authToken,String fromPhoneNumber,String toPhoneNo, String msg) throws TwilioRestException {
	LOGGER.info("InsidesendMessage "+env.getProperty("is_sms_enabled"));
	
	if(env.getProperty("is_sms_enabled").equalsIgnoreCase("true")){
	TwilioRestClient client = new TwilioRestClient(accountSID, authToken);
    List<NameValuePair> params = new ArrayList<>();
    params.add(new BasicNameValuePair("From", fromPhoneNumber));
    params.add(new BasicNameValuePair("Body", msg));
    params.add(new BasicNameValuePair("To", toPhoneNo));
    MessageFactory messageFactory = client.getAccount().getMessageFactory();
    Message message = messageFactory.create(params);
    if (message != null) {
    		LOGGER.info("Message sent on {}", toPhoneNo ," {} with msg id: {}", message.getSid(), " msg {}", message.getBody());
    }
	}    
    
    return true;
}

}

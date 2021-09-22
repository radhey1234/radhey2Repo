package com.iexpress.spring.api.util;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

@Component
@PropertySource({"classpath:application.properties"})
public class MailService {

    
	private static final String MAIL_ENABLED = "mail.enabled";
    private static final String FROM_EMAIL = "from_email";
    private static final String SENDGRID_API_KEY = "sendgrid.api.key";
    private static final String TEXT_HTML = "text/html";
    private static final String MAIL_SEND = "mail/send";
    
    private String fromEmail;
    private String sendgridApiKey;
    
    
    @Autowired
    Environment env;

    public static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);

    @PostConstruct
    public void config() {
    	fromEmail = env.getProperty(FROM_EMAIL);
    	sendgridApiKey = env.getProperty(SENDGRID_API_KEY);
    }

    @Async
    public void sendMail(String to, String subject, String msg)  {
    	LOGGER.info("Inside sendMail MSG {} ", msg);
    	
        boolean mailEnabled = Boolean.parseBoolean(env.getProperty(MAIL_ENABLED));
        if (!mailEnabled) return;

    	LOGGER.debug("Inside sendMail {} ");
    	SendGrid sendGrid = new SendGrid(sendgridApiKey);
    	Email receiver = new Email(to);
    	Email from = new Email(fromEmail);
    	Content content = new Content(TEXT_HTML, msg);
    	Mail mail = new Mail(from, subject, receiver, content);
    	Request request = new Request();
    	request.setMethod(Method.POST);
        request.setEndpoint(MAIL_SEND);
        try {
			request.setBody(mail.build());
	        sendGrid.api(request);

		} catch (IOException e) {
			LOGGER.error("Problem encounter while sending email user {} msg {} exception {}", to , msg, e);
		}
        
        LOGGER.debug("Mail Sent Successfully {} ", subject);
    }

}
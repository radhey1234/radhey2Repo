package com.iexpress.spring.api.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.iexpress.spring.api.exception.GenericException;
import com.iexpress.spring.api.response.ErrorResponse;

import static com.iexpress.spring.api.util.RestConstant.*;

 @Component
 public class AppUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(AppUtil.class);
	
	public static final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_TIME_PATTERN2 = "dd-MM-yyyy HH:mm:ss";
	public static final String DATE_PATTERN = "yyyy-MM-dd";
	public static final int OTP_LENGTH = 4;
	public static final Integer OTP_EXPIRATION_TIME = 3600;   //in seconds
	public static final ObjectNode nullObj = new ObjectMapper().createObjectNode();
	private static final String BUCKET_NAME = "bucket_name";
	private static final String ORIGINAL_SIZE_RESOURCE_URL = "original_size_resource_url";
	private static final String SERVER_API_KEY_FCM = "SERVER_API_KEY_FCM";

	private  final String bucketName;
	private  final String resourePrefixAws; 
	private  final String serverApiKeyFcm;
	private  final Environment env;
	
	private static ApplicationContext context;

	
	@Autowired
	public AppUtil(Environment env) {
		this.env = env;
		this.bucketName = env.getProperty(BUCKET_NAME);
		this.resourePrefixAws = env.getProperty(ORIGINAL_SIZE_RESOURCE_URL);
		this.serverApiKeyFcm = env.getProperty(SERVER_API_KEY_FCM);
	}
	
	public static AppUtil of() {
	 return	(AppUtil) context.getBean("appUtil");
	}
	
	@Autowired
	public void setContext(ApplicationContext context) {
		this.context = context;
	}

	public  String getResourePrefixAws() {
		return resourePrefixAws;
	}

	public String getBucketName() {
		return this.bucketName;
	}

	public  static String convertDateToString(String format, Date date){
		DateFormat dateFormat = new SimpleDateFormat(format);  
		return dateFormat.format(date);  
	}
	
	public static Date convertStringToDate(String format, String inputDate) {
		try {
			return new SimpleDateFormat(format).parse(inputDate);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static String generateRandomNumericCode(int otpLength) {
		 String candidateChars = "123456789";
	        Random random = new Random();
	        StringBuilder sb = new StringBuilder();
	        for (int i = 0; i < otpLength; i++) {
	            sb.append(candidateChars.charAt(random.nextInt(candidateChars
	                    .length())));
	        }
	        return sb.toString();
	}
	
	public static boolean isValid(Double input) {
		return null != input && input >0.0;
	}
	
	public static boolean isValid(String input) {
		return null != input && !StringUtils.isEmpty(input);
	}
	
	public static String getRandomString() {
		return UUID.randomUUID().toString();
	}
	
	public static boolean isNullOrEmpty(String input) {
		return null ==  input || input.isEmpty();
	}

	public static boolean isNullOrEmpty(int readerUserId) {
		return readerUserId == 0;
	}

	public static String getRandomNumber() {
		return UUID.randomUUID().toString();
	}
	
	public static String generateRandomePassword() {
		return UUID.randomUUID().toString();
	}

	public static String getResizedBucketName() {
		return null;
	}
	
	
	public void waitForSixSecond() {
		try{
			LOGGER.info("Sleeping for 6 sec");
			Thread.sleep(6000);
		}catch(Exception e){
			LOGGER.error("Error while sleeping {}",e);
		}		
	}

	public String getServerApiKeyFcm() {
		return this.serverApiKeyFcm;
	}
	
	public String getUTCdate(Date date) {
		try {
			DateFormat dateFormat = new SimpleDateFormat(DATE_TIME_PATTERN2); 
			return dateFormat.format(date);
		} catch (Exception e) {
			return null;
		}
	}

	public static Date getExpiredAt(){
		DateTime currentDateTime = new DateTime();
		LOGGER.info("createdDateTime: {}", currentDateTime.toDate());
		DateTime expiredDateTime = currentDateTime.plusSeconds(OTP_EXPIRATION_TIME);
		LOGGER.info("expiredDateTime: {}", expiredDateTime.toDate());
		return expiredDateTime.toDate();
	}

	public static boolean isNull(Object input) {
		return null == input;
	}

	public Environment getEnv() {
		return env;
	}
	
	public String getEnvProperty(String key) {
		return getEnv().getProperty(key);
	}

	public static boolean isEmail(String inputType) {
		return inputType.equalsIgnoreCase(EMAIL);
	}

	public static boolean isPhone(String inputType) {
		return inputType.equalsIgnoreCase(PHONE);
	}

	public static void throwAlreadyVerified() {
		throw new GenericException(ALREADY_VERIFIED, BAD_REQUEST);
	}

	public static void throwIncorrectAnswer(List<Integer> answerIds) {
		List<ErrorResponse> errors = answerIds.stream().map( q -> (new ErrorResponse(String.valueOf(q), INCORRECT_ANSWER))).collect(Collectors.toList());
		throw new GenericException(INCORRECT_ANSWER, BAD_REQUEST, errors);		
	}

	public static void throwInvalidOtp() {
		throw new GenericException(INVALID_OTP ,BAD_REQUEST);
	}
	
	public static void throwExpiredOTP() {
		throw new GenericException(EXPIRED_OTP ,BAD_REQUEST);
	}

	public static GenericException throwPostNotFound() {
		return new GenericException(POST_NOT_FOUND ,BAD_REQUEST);
	}

	public static void throwNotExistingPhone() {
		throw new GenericException(NOT_EXISTING_PHONE_NUMBER ,BAD_REQUEST);		
	}
	public static void throwNotExistingEmail() {
		throw new GenericException(NOT_EXISTING_EMAIL ,BAD_REQUEST);		
	}
	
	public static GenericException throwResourceNotFound() {
		return  new GenericException(INVALID_RESOURCE_ID ,BAD_REQUEST);				
	}
	
	public static boolean isVerified(String input) {
		return input.equalsIgnoreCase(VERIFIED);
	}

	public static boolean isExpired(Date expiryDate) {
		return (expiryDate.compareTo(new Date()) < 0) ? true: false;
	}
	
	public static boolean isEqual(String input1, String input2) {
		if(isNullOrEmpty(input1) || isNullOrEmpty(input2)) {
			return false;
		}
		return input1.equalsIgnoreCase(input2);
	}

	public static String format(Date createdAt) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		return sdf.format(createdAt);
	}

	public static boolean isUserVerified(String mobileOrEmailStatus) {
		return ACTIVE.equalsIgnoreCase(mobileOrEmailStatus);
	}

}

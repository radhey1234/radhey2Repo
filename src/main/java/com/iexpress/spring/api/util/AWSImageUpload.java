package com.iexpress.spring.api.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.iexpress.spring.api.exception.GenericException;
import static com.iexpress.spring.api.util.ImageUtility.*;

@Component
public class AWSImageUpload {


	public static final Logger LOGGER = LoggerFactory.getLogger(AWSImageUpload.class);
	public static final String PROFILE_DIR = "profile/";
	public static final String RESOURCE_DIR = "resource/";
	public static final String FILENAME = "filename";
	public static final String COLOR_DIR = "color/";
	public static final String THUMBNAIL_DIR = "thumbnail/";
	public static final String DOT_PNG = ".png";
	
	private  AmazonS3 s3;
	private  Environment env;

	@Autowired
	public AWSImageUpload(AmazonS3 s3, Environment env) {
		this.s3 = s3;
		this.env = env;
	}

	public String saveResourceOfAnEvent(MultipartFile picFile, final String randomeFileName){
		
		LOGGER.info("Inside saveResourceOfAnEvent - Random file name: {}", randomeFileName);
		LOGGER.info("Inside saveResourceOfAnEvent - file name: {}",picFile.getOriginalFilename());
		LOGGER.info("Inside saveResourceOfAnEvent - file size: {}",picFile.getSize());
		LOGGER.info("Inside saveResourceOfAnEvent - file content type: {}",picFile.getContentType());

		String extension = getExtensionOfFile(picFile);
		
		final String fileNameWithExtension = randomeFileName + "."+ extension;
		
		final String bucket = env.getProperty("bucket_name");
		
		LOGGER.info("Bucket >>" + bucket);
		try {
			uploadImageToAwsS3Bucket(bucket,  RESOURCE_DIR + fileNameWithExtension, 
					picFile.getInputStream(), picFile.getContentType(), picFile.getSize(), randomeFileName);
			
			resizeImagesAndUploadToS3(bucket,  THUMBNAIL_DIR + fileNameWithExtension, 
					picFile.getInputStream(), picFile.getContentType(), picFile.getSize(), randomeFileName);

			
		} catch (Exception e) {
			LOGGER.error("Error while uploading image in {}", e);
			throw new GenericException(RestConstant.RESOURCE_CAN_NOT_BE_UPLOADED_SUCCESSFULLY, HttpStatus.SERVICE_UNAVAILABLE.value());
		}

		return RESOURCE_DIR + fileNameWithExtension;
	}

	private void resizeImagesAndUploadToS3(String bucket, String URI, InputStream inputStream, String contentType,
			long size, String randomeFileName) {
		try {
			ByteArrayOutputStream outStream = ImageUtility.getResizedImage(inputStream, 100, 100);
			InputStream inputStreamResize = new ByteArrayInputStream(outStream.toByteArray());
			uploadImageToAwsS3Bucket(bucket,  URI, inputStreamResize, contentType, outStream.size(), randomeFileName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private void uploadImageToAwsS3Bucket(String bucket, String URI, InputStream inputStream, String contentType,
			long size, String randomeFileName) {
		ObjectMetadata meta = new ObjectMetadata();
		meta.setContentType(contentType);
		meta.setContentLength(size);
		meta.setHeader(FILENAME, randomeFileName);
		PutObjectRequest putObjectRequest;
		putObjectRequest = new PutObjectRequest(bucket,URI ,inputStream, meta);
		putObjectRequest.setCannedAcl(CannedAccessControlList.PublicRead);
		s3.putObject(putObjectRequest);
		IOUtils.closeQuietly(inputStream);
		LOGGER.info("Profile Uploaded Successfully {}", URI);
	}

}

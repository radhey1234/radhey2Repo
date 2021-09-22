package com.iexpress.spring.api.util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

public class ImageUtility {
	
	private static final String QUESTION_MARK = "?";
	private static final String SEPARATOR = "/";


	public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
	    int imageWidth  = image.getWidth();
	    int imageHeight = image.getHeight();

	    double scaleX = (double)width/imageWidth;
	    double scaleY = (double)height/imageHeight;
	    AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
	    AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);
	    BufferedImage bufferedImage = bilinearScaleOp.filter(image,  new BufferedImage(width, height, image.getType()));
	    return bufferedImage;
	}
	
	
	public static ByteArrayOutputStream getResizedImage(InputStream inputImage,int tThumbWidth, int tThumbHeight  ) throws IOException {
		  BufferedImage originalImage =	ImageIO.read(inputImage); 
		  BufferedImage tThumbImage = new BufferedImage( tThumbWidth, tThumbHeight, BufferedImage.TYPE_INT_RGB );
		  Graphics2D tGraphics2D = tThumbImage.createGraphics(); //create a graphics object to paint to
		  tGraphics2D.setBackground( Color.WHITE );
		  tGraphics2D.setPaint( Color.WHITE );
		  tGraphics2D.fillRect( 0, 0, tThumbWidth, tThumbHeight );
		  tGraphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
		  tGraphics2D.drawImage( originalImage, 0, 0, tThumbWidth, tThumbHeight, null ); //draw the image scaled
		  ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		  ImageIO.write( tThumbImage, "JPG", outStream );
		  return outStream;
	}
	
	public static String getExtensionOfFile(MultipartFile picFile) {
		String extension = FilenameUtils.getExtension(picFile.getOriginalFilename());

		if( extension!=null && extension.contains("?")){
			extension = extension.substring(0, extension.indexOf(QUESTION_MARK));
		}
		
		if(AppUtil.isNullOrEmpty(extension)){
			String contentType = picFile.getContentType();
			extension = contentType.substring(contentType.indexOf(SEPARATOR)+1);
		}
		return extension;
	}

	
}

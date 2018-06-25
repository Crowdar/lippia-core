package com.crowdar.core;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.impl.Log4JLogger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public final class ScreenshotCapture {
	private static final Log4JLogger logger = new Log4JLogger(ScreenshotCapture.class.getSimpleName());

	
	public static void createScreenCapture(WebDriver driver){
//		BufferedImage image;
		try { 
			
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(getScreenCaptureFileLocation()));
			
//			image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
//			ImageIO.write(image, "png", new File(getScreenCaptureFileLocation()));
			
		}catch(IOException ioe){
			logger.error(ioe);
		}
	}
	
	public static String getScreenCaptureFileLocation(){
//		return System.getProperty("user.dir") + "/target/" + getScreenCaptureFileName() + ".png";
		return ReportManager.getReportPath().concat("img").concat(File.separator) + getScreenCaptureFileName();
	}
	
	public static String getScreenCaptureFileName(){
		return  (String)MyThreadLocal.get().getData(Context.CONTEXT_TEST_NAME_KEY) + (String)MyThreadLocal.get().getData(Context.CONTEXT_TEST_ID_KEY) + ".png";
	}

}

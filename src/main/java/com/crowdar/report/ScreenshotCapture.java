package com.crowdar.report;

import com.crowdar.core.Context;
import com.crowdar.core.MyThreadLocal;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.impl.Log4JLogger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;

public final class ScreenshotCapture {
	private static final Log4JLogger logger = new Log4JLogger(ScreenshotCapture.class.getSimpleName());

	
	public static void createScreenCapture(AppiumDriver driver){
		try {
			File scrFile = driver.getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(getScreenCaptureFileLocation()));
		}catch(IOException ioe){
			logger.error(ioe);
		}
	}

	public static void createScreenCapture(WebDriver driver){
		try {
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File(getScreenCaptureFileLocation()));

		}catch(IOException ioe){
			logger.error(ioe);
		}
	}
	
	public static String getScreenCaptureFileLocation(){
		return "target" + File.separator + getScreenCaptureFileName();
	}
	
	public static String getScreenCaptureFileName(){
		return  MyThreadLocal.get().getData(Context.CONTEXT_TEST_NAME_KEY) + (String)MyThreadLocal.get().getData(Context.CONTEXT_TEST_ID_KEY) + ".png";
	}

}

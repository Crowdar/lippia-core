package com.crowdar.core;

import com.crowdar.bdd.cukes.SharedDriver;
import com.crowdar.driver.DriverManager;
import com.crowdar.mobile.AppiumDriverManager;
import io.appium.java_client.AppiumDriver;
import org.apache.log4j.Logger;
import org.jbehave.core.annotations.Given;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;


/**
 *
 * @author jCarames
 *
 */
public abstract class PageSteps{

	protected RemoteWebDriver driver;
	protected Logger logger;


    public PageSteps(RemoteWebDriver driver){
        if (driver != null) {
            this.driver = driver;
        }
    }
	public PageSteps(SharedDriver driver){
		//this((WebDriver) driver);
		logger = Logger.getLogger(this.getClass());
	}

	public PageSteps(){

	}

    public RemoteWebDriver getDriver() {
        return driver;
    }
}

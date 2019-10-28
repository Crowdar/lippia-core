package com.crowdar.driver.setupStrategy.mobile;

import java.io.File;

import com.crowdar.driver.config.AutomationConfiguration;
import com.crowdar.driver.config.MobilePlatformConfiguration;

import com.crowdar.driver.setupStrategy.SetupStrategy;

public class LocalMobileExecutionStrategy implements SetupStrategy{
    
	@Override
	public void beforeDriverStartSetup(AutomationConfiguration contextConfiguration) {
		
		switch ((MobilePlatformConfiguration)contextConfiguration) {
			case ANDROID_CHROME:
				System.setProperty("webdriver.gecko.driver", getWebDriverPath().concat("geckodriver.exe"));
				break;
			case IOS:
				System.setProperty("webdriver.chrome.driver", getWebDriverPath().concat("chromedriver2.37.exe"));
				break;
		}
		
	}

	@Override
	public void afterDriverStartSetup() {
		// TODO Auto-generated method stub
		
	}
	
    private static String getWebDriverPath() {
        return System.getProperty("user.dir").concat(File.separator).concat("webDrivers").concat(File.separator);
    }

}

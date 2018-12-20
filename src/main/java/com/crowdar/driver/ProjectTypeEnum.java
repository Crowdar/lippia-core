package com.crowdar.driver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.config.AutomationConfiguration;
import com.crowdar.driver.config.BrowserConfiguration;
import com.crowdar.driver.config.MobilePlatformConfiguration;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public enum ProjectTypeEnum {
	WEB_CHROME {

		@Override
		public Class<? extends RemoteWebDriver> getDriverImplementation() {
			return ChromeDriver.class;
		}

		@Override
		public AutomationConfiguration getDriverConfig() {
			return BrowserConfiguration.CHROME;
		}
		
	},
	WEB_IE {

		@Override
		public Class<? extends RemoteWebDriver> getDriverImplementation() {
			return InternetExplorerDriver.class;
		}

		@Override
		public AutomationConfiguration getDriverConfig() {
			return BrowserConfiguration.IE;
		}
		
	},
	// WEB_SAFARI
	// WEB_FIREFOX
	MOBILE_ANDROID {

		@Override
		public Class<? extends RemoteWebDriver> getDriverImplementation() {
			return AndroidDriver.class;
		}

		@Override
		public AutomationConfiguration getDriverConfig() {
			return MobilePlatformConfiguration.ANDROID;
		}

	},
	MOBILE_IOS {
		
		@Override
		public Class<? extends RemoteWebDriver> getDriverImplementation() {
			return IOSDriver.class;
		}
		
		@Override
		public AutomationConfiguration getDriverConfig() {
			return MobilePlatformConfiguration.IOS;
		}
		
	},
	// MOBILE_CHROME

	WIN32 {

		@Override
		public Class<? extends RemoteWebDriver> getDriverImplementation() {
			return null;
		}

		@Override
		public AutomationConfiguration getDriverConfig() {
			return null;
		}
	};

	public abstract Class<? extends RemoteWebDriver> getDriverImplementation();

	public abstract AutomationConfiguration getDriverConfig();

	
	public static ProjectTypeEnum get(String key) {
		try {          
	         return Enum.valueOf(ProjectTypeEnum.class, key);

		} catch (IllegalArgumentException e) {
	         throw new RuntimeException("Invalid value for enum ProjectTypeEnum : " + key);
	      }
	}

}

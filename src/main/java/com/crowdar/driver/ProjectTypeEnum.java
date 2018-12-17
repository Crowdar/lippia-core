package com.crowdar.driver;

import com.crowdar.driver.config.AutomationConfiguration;
import com.crowdar.driver.config.BrowserConfiguration;
import com.crowdar.driver.config.MobilePlatformConfiguration;
import com.crowdar.driver.impl.AppiumDriver;
import com.crowdar.driver.impl.AutomationDriver;
import com.crowdar.driver.impl.SeleniumWebDriver;
import com.crowdar.driver.impl.WinAppDriver;

public enum ProjectTypeEnum {
	WEB_CHROME {

		@Override
		public Class<? extends AutomationDriver> getDriverImplementation() {
			return SeleniumWebDriver.class;
		}

		@Override
		public AutomationConfiguration getDriverConfig() {
			return BrowserConfiguration.CHROME;
		}
	},
	// WEB_IE
	// WEB_SAFARI
	// WEB_FIREFOX
	MOBILE_ANDROID {

		@Override
		public Class<? extends AutomationDriver> getDriverImplementation() {
			return AppiumDriver.class;
		}

		@Override
		public AutomationConfiguration getDriverConfig() {
			return MobilePlatformConfiguration.ANDROID;
		}

	},
	// MOBILE_CHROME

	WIN32 {

		@Override
		public Class<? extends AutomationDriver> getDriverImplementation() {
			return WinAppDriver.class;
		}

		@Override
		public AutomationConfiguration getDriverConfig() {
			return null;
		}
	};

	public abstract Class<? extends AutomationDriver> getDriverImplementation();

	public abstract AutomationConfiguration getDriverConfig();

	
	public static ProjectTypeEnum get(String key) {
		try {          
	         return Enum.valueOf(ProjectTypeEnum.class, key);

		} catch (IllegalArgumentException e) {
	         throw new RuntimeException("Invalid value for enum ProjectTypeEnum : " + key);
	      }
	}

}

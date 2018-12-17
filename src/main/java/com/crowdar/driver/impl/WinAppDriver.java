package com.crowdar.driver.impl;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.config.AutomationConfiguration;

public class WinAppDriver extends RemoteWebDriver implements AutomationDriver{

	public WinAppDriver(AutomationConfiguration configuration) {
		super(getDesiredCapabilities());
	}

	@Override
	public AutomationConfiguration getConfiguration() {
		return null;
	}
	
	
	private static DesiredCapabilities getDesiredCapabilities() {
		 DesiredCapabilities capabilities = new DesiredCapabilities();
		 String appKey =""; // 
		 capabilities.setCapability("app", appKey);
//		 capabilities.setCapability("app", "Root");
//		 cafmCapabilities.setCapability("appTopLevelWindow", "0x180FCC");
		 return capabilities;
	}
	
}

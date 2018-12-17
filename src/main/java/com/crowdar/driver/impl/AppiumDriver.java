package com.crowdar.driver.impl;

import java.net.URL;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.config.AutomationConfiguration;

public class AppiumDriver extends RemoteWebDriver implements AutomationDriver{

	AutomationConfiguration mobilePlatformConfiguration;
	
	public AppiumDriver(AutomationConfiguration configuration) {
		super(configuration.getDesiredCapabilities());

		mobilePlatformConfiguration = configuration;
	}

	public AppiumDriver(URL remoteAddress, AutomationConfiguration configuration) {
		super(remoteAddress, configuration.getDesiredCapabilities());
		mobilePlatformConfiguration = configuration;
	}

	@Override
	public AutomationConfiguration getConfiguration() {
		return mobilePlatformConfiguration;
	}
	
}

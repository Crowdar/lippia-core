package com.crowdar.driver.impl;

import java.net.URL;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.config.AutomationConfiguration;
import com.crowdar.driver.config.BrowserConfiguration;

public class SeleniumWebDriver extends RemoteWebDriver implements AutomationDriver {

	AutomationConfiguration browserConfiguration;

	public SeleniumWebDriver(AutomationConfiguration configuration) {
		super(configuration.getDesiredCapabilities());
		browserConfiguration = configuration;
	}

	public SeleniumWebDriver(URL remoteAddress, AutomationConfiguration configuration) {
		super(remoteAddress, configuration.getDesiredCapabilities());
		browserConfiguration = configuration;
	}

	@Override
	public AutomationConfiguration getConfiguration() {
		return browserConfiguration;
	}

}

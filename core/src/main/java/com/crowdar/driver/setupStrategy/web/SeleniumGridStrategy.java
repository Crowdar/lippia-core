package com.crowdar.driver.setupStrategy.web;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.config.AutomationConfiguration;
import com.crowdar.driver.setupStrategy.SetupStrategy;

public class SeleniumGridStrategy implements SetupStrategy {

    @Override
    public void beforeDriverStartSetup(AutomationConfiguration contextConfiguration) {
    }

    @Override
    public void afterDriverStartSetup(RemoteWebDriver driver) {
    	driver.manage().window().maximize();
    }
}

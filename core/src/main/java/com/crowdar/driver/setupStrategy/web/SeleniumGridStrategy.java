package com.crowdar.driver.setupStrategy.web;

import com.crowdar.driver.config.AutomationConfiguration;
import com.crowdar.driver.setupStrategy.SetupStrategy;
import org.openqa.selenium.remote.RemoteWebDriver;

public class SeleniumGridStrategy implements SetupStrategy {

    @Override
    public void beforeDriverStartSetup(AutomationConfiguration contextConfiguration) {
    }

    @Override
    public void afterDriverStartSetup(RemoteWebDriver driver) {
        driver.manage().window().maximize();
    }
}

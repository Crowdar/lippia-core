package com.crowdar.driver.setupStrategy;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.config.AutomationConfiguration;

public interface SetupStrategy {

    public void beforeDriverStartSetup(AutomationConfiguration contextConfiguration);

    public void afterDriverStartSetup(RemoteWebDriver driver);
}

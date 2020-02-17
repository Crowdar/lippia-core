package com.crowdar.driver.setupStrategy;

import com.crowdar.driver.config.AutomationConfiguration;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface SetupStrategy {

    public void beforeDriverStartSetup(AutomationConfiguration contextConfiguration);

    public void afterDriverStartSetup(RemoteWebDriver driver);
}

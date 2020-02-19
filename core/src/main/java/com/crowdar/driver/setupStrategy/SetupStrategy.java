package com.crowdar.driver.setupStrategy;

import com.crowdar.driver.config.AutomationConfiguration;
import org.openqa.selenium.remote.RemoteWebDriver;

public interface SetupStrategy {

    void beforeDriverStartSetup(AutomationConfiguration contextConfiguration);

    void afterDriverStartSetup(RemoteWebDriver driver);
}

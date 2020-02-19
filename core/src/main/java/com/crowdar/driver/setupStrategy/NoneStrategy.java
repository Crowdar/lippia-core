package com.crowdar.driver.setupStrategy;

import com.crowdar.driver.config.AutomationConfiguration;
import org.openqa.selenium.remote.RemoteWebDriver;

public class NoneStrategy implements SetupStrategy {

    @Override
    public void beforeDriverStartSetup(AutomationConfiguration contextConfiguration) {
    }

    @Override
    public void afterDriverStartSetup(RemoteWebDriver driver) {
    }
}

package com.crowdar.driver.setupStrategy;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.ProjectTypeEnum;

public class NoneStrategy implements SetupStrategy {

    @Override
    public void beforeDriverStartSetup(ProjectTypeEnum configuration) {
    }

    @Override
    public void afterDriverStartSetup(RemoteWebDriver driver) {
    }
}

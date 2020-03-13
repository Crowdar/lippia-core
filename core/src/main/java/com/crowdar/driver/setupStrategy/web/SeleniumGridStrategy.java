package com.crowdar.driver.setupStrategy.web;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.ProjectTypeEnum;
import com.crowdar.driver.setupStrategy.SetupStrategy;

public class SeleniumGridStrategy implements SetupStrategy {

    @Override
    public void beforeDriverStartSetup(ProjectTypeEnum configuration) {
    }

    @Override
    public void afterDriverStartSetup(RemoteWebDriver driver) {
        driver.manage().window().maximize();
    }
}

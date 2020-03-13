package com.crowdar.driver.setupStrategy;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.ProjectTypeEnum;

public interface SetupStrategy {

    void beforeDriverStartSetup(ProjectTypeEnum configuration);

    void afterDriverStartSetup(RemoteWebDriver driver);
}

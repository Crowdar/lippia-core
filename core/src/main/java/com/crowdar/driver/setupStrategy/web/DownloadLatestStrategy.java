package com.crowdar.driver.setupStrategy.web;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.ProjectTypeEnum;
import com.crowdar.driver.setupStrategy.SetupStrategy;

import io.github.bonigarcia.wdm.WebDriverManager;

public class DownloadLatestStrategy implements SetupStrategy {

    @Override
    public void beforeDriverStartSetup(ProjectTypeEnum configuration) {
        WebDriverManager.getInstance(configuration.getLocalDriverImplementation()).setup();
    }

    @Override
    public void afterDriverStartSetup(RemoteWebDriver driver) {
        driver.manage().window().maximize();
    }

}

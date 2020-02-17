package com.crowdar.driver.setupStrategy.web;

import com.crowdar.driver.config.AutomationConfiguration;
import com.crowdar.driver.config.BrowserConfiguration;
import com.crowdar.driver.setupStrategy.SetupStrategy;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.remote.RemoteWebDriver;

public class DownloadLatestStrategy implements SetupStrategy {

    @Override
    public void beforeDriverStartSetup(AutomationConfiguration contextConfiguration) {
        WebDriverManager.getInstance(((BrowserConfiguration) contextConfiguration).getDriverManagerType()).setup();
    }

    @Override
    public void afterDriverStartSetup(RemoteWebDriver driver) {
        driver.manage().window().maximize();
    }


}

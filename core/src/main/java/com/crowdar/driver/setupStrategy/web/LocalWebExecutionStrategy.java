package com.crowdar.driver.setupStrategy.web;

import com.crowdar.driver.config.AutomationConfiguration;
import com.crowdar.driver.config.BrowserConfiguration;
import com.crowdar.driver.setupStrategy.SetupStrategy;

import java.io.File;

public class LocalWebExecutionStrategy implements SetupStrategy {

    @Override
    public void beforeDriverStartSetup(AutomationConfiguration contextConfiguration) {

        switch ((BrowserConfiguration) contextConfiguration) {
            case FIREFOX:
                System.setProperty("webdriver.gecko.driver", getWebDriverPath().concat("geckodriver.exe"));
                break;
            case CHROME:
                System.setProperty("webdriver.chrome.driver", getWebDriverPath().concat("chromedriver2.37.exe"));
                break;
            case EDGE:
                System.setProperty("webdriver.edge.driver", getWebDriverPath().concat("MicrosoftWebDriver.exe"));
                break;
            case IE:
                System.setProperty("webdriver.ie.driver", getWebDriverPath().concat("IEDriverServer.exe"));
        }

    }

    @Override
    public void afterDriverStartSetup() {
        // TODO Auto-generated method stub

    }

    private static String getWebDriverPath() {
        return System.getProperty("user.dir").concat(File.separator).concat("webDrivers").concat(File.separator);
    }

}

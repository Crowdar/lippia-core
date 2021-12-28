package com.crowdar.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;


/**
 * @author jCarames
 */
public abstract class PageSteps {

    protected RemoteWebDriver driver;
    protected Logger logger;

    public PageSteps(RemoteWebDriver driver) {
        this.driver = driver;
        logger = LogManager.getLogger(this.getClass());
    }

    public PageSteps() {

    }

    public RemoteWebDriver getDriver() {
        return driver;
    }
}

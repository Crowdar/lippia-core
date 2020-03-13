package com.crowdar.core;

import org.apache.log4j.Logger;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.bdd.cukes.SharedDriver;


/**
 * @author jCarames
 */
public abstract class PageSteps {

    protected RemoteWebDriver driver;
    protected Logger logger;

    public PageSteps(RemoteWebDriver driver) {
        this.driver = driver;
        logger = Logger.getLogger(this.getClass());
    }

    public PageSteps(SharedDriver driver) {
        //this((WebDriver) driver);
        logger = Logger.getLogger(this.getClass());
    }

    public PageSteps() {

    }

    public RemoteWebDriver getDriver() {
        return driver;
    }
}

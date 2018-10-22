package com.crowdar.mobile;

import java.util.concurrent.TimeUnit;

import com.crowdar.core.Constants;
import com.crowdar.mobile.mobileDriver.PlatformConfiguration;

import io.appium.java_client.AppiumDriver;

public final class AppiumDriverManager {

    private static AppiumDriver driver;
    private static Enum<PlatformConfiguration> platformConfiguration = null;

    private AppiumDriverManager() {
    }

    public static AppiumDriver getDriverInstance() {
        if (driver == null || driver.getSessionId() == null) {
            driver = getDriver();
        }
        return driver;
    }

    @Deprecated
    public static AppiumDriver getNewDriverInstance() {
        return getDriver();
    }

    public static void build(Enum<PlatformConfiguration> platformConfig) {
        platformConfiguration = platformConfig;
    }

    private static AppiumDriver getDriver() {
        driver = ((PlatformConfiguration) platformConfiguration).getDriver();
        driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
        return driver;
    }

    public static void dismissDriver() {
        driver.closeApp();
        driver.quit();
        //driver.resetApp();
    }
}
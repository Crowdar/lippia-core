package com.crowdar.driver;

import org.openqa.selenium.remote.RemoteWebDriver;

import io.appium.java_client.AppiumDriver;

public class DriverManager {

    private DriverManager() {

    }

    private static ThreadLocal<RemoteWebDriver> localDriver = new ThreadLocal<RemoteWebDriver>();


    public static RemoteWebDriver getDriverInstance() {

        if (!isDriverCreated() || !isAValidDriver()) {
            if (localDriver.get() != null) {
                localDriver.remove();
            }
            localDriver.set(DriverFactory.createDriver());
        }
        return localDriver.get();
    }

    public static void dismissCurrentDriver() {
        if (isDriverCreated()) {
            localDriver.get().quit();
            localDriver.remove();
        }
    }

    private static boolean isDriverCreated() {
        return localDriver.get() != null;
    }

    private static boolean isAValidDriver() {

        return localDriver.get().getSessionId() != null;
    }

    public static void dismissDriver() {
        ((AppiumDriver) getDriverInstance()).closeApp();
        getDriverInstance().quit();
    }

    public static void resetDriver() {
        dismissCurrentDriver();
    }


}

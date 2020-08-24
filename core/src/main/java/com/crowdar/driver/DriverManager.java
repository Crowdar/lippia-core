package com.crowdar.driver;

import java.net.URL;
import java.util.Map;

import com.crowdar.core.PropertyManager;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.setupStrategy.SetupStrategy;

import io.appium.java_client.AppiumDriver;

public class DriverManager {

    private DriverManager() {

    }

    private static ThreadLocal<RemoteWebDriver> localDriver = new ThreadLocal<>();

    
    public static void initialize(Map<String, ?> extraCapabilities){
    	if (!isDriverCreated() || !isAValidDriver()) {
            if (localDriver.get() != null) {
                localDriver.remove();
            }
            localDriver.set(DriverFactory.createDriver(extraCapabilities));
        }
    }
    
    public static void initialize(ProjectTypeEnum projectType, SetupStrategy setupStrategy, URL driverHub, Map<String, ?> extraCapabilities) throws Exception {
    	
    	if (!isDriverCreated() || !isAValidDriver()) {
            if (localDriver.get() != null) {
                localDriver.remove();
            }
            localDriver.set(DriverFactory.createDriver(projectType, setupStrategy, driverHub, extraCapabilities));
        }else {
        	throw new Exception("Driver initialized!");
        }
    }

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

    public static void dismissMobileDriver() {
        ((AppiumDriver) getDriverInstance()).closeApp();
        dismissCurrentDriver();
    }

    public static void resetDriver() {
        dismissCurrentDriver();
    }

    public static String getName(){
        return ProjectTypeEnum.get(PropertyManager.getProperty("crowdar.projectType")).getName();
    }
}

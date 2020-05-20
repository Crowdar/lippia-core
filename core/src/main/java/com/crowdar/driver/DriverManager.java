package com.crowdar.driver;

import java.net.URL;
import java.util.Map;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.setupStrategy.SetupStrategy;

import io.appium.java_client.AppiumDriver;

public class DriverManager {

    private DriverManager() {

    }

    private static ThreadLocal<RemoteWebDriver> localDriver = new ThreadLocal<RemoteWebDriver>();

    
    public static void initialize(Map<String, ?> extraCapabilities) throws Exception {
//    	String created = String.valueOf(isDriverCreated());
    	
    	if (!isDriverCreated() || !isAValidDriver()) {
            if (localDriver.get() != null) {
                localDriver.remove();
            }
            localDriver.set(DriverFactory.createDriver(extraCapabilities));
        }else {
        	throw new Exception("Driver initialized!");
        }
    	
//        String logTemplate = "######  %s - Thread id %s --- isDriverCreated %s --- DriverId %s";
//        System.out.println(logTemplate.format(logTemplate, "Initialize", Thread.currentThread().getId(), created, localDriver.get().getSessionId()));
        
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
    	
//    	String created = String.valueOf(isDriverCreated());
    	
        if (!isDriverCreated() || !isAValidDriver()) {
            if (localDriver.get() != null) {
                localDriver.remove();
            }
            localDriver.set(DriverFactory.createDriver());
        }
        
//        String logTemplate = "######  %s - Thread id %s --- isDriverCreated %s --- DriverId %s";
//        System.out.println(logTemplate.format(logTemplate, "GetInstance", Thread.currentThread().getId(), created, localDriver.get().getSessionId()));
        
        return localDriver.get();
    }

    public static void dismissCurrentDriver() {
//    	String logTemplate = "######  %s - Thread id %s --- isDriverCreated %s --- DriverId %s";
//    	System.out.println(logTemplate.format(logTemplate, "DismissDriver", Thread.currentThread().getId(), isDriverCreated(), localDriver.get().getSessionId()));

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

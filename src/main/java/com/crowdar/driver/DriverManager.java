package com.crowdar.driver;

import com.crowdar.driver.factory.RemoteDriverProvider;

import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;

public class DriverManager {

    private DriverManager(){

    }

    private static HashMap<Long,RemoteWebDriver> driverPool = new HashMap<>();


    public static RemoteWebDriver getDriverInstance() {

        if (!isDriverCreated()|| !isAValidDriver()) {
            driverPool.put(Thread.currentThread().getId(),DriverFactory.createDriver());
        }
        return driverPool.get(Thread.currentThread().getId());
    }


    public static void dismissAllDriver() {

        for(RemoteWebDriver d : driverPool.values()){
            d.quit();
        }
        driverPool.clear();
    }

    public static void dismissCurrentDriver(){
        if(isDriverCreated()){
            driverPool.get(Thread.currentThread().getId()).quit();
            driverPool.remove(Thread.currentThread().getId());
        }
    }

    private static boolean isDriverCreated(){
        return driverPool.containsKey(Thread.currentThread().getId());
    }
    private static boolean isAValidDriver(){
        return driverPool.get(Thread.currentThread().getId()).getSessionId() != null;
    }




}

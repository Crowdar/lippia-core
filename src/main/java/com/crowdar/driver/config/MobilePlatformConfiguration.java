package com.crowdar.driver.config;

import com.crowdar.core.PropertyManager;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.Platform;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.stqa.selenium.factory.SingleWebDriverPool;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public enum MobilePlatformConfiguration implements AutomationConfiguration{

    ANDROID {

        @Override
        public DesiredCapabilities getDesiredCapabilities() {

        	DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("deviceName", PropertyManager.getProperty("crowdar.mobile.deviceName"));
            capabilities.setCapability("avd",PropertyManager.getProperty("crowdar.mobile.avd"));
            //capabilities.setCapability("automationName","automationName");
            capabilities.setCapability("browserName","android");
            capabilities.setCapability("app",PropertyManager.getProperty("crowdar.mobile.apk.path"));
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability("platformName","Android");
            return capabilities;
        }
    },
    IOS {
        
        @Override
        public DesiredCapabilities getDesiredCapabilities() {

            DesiredCapabilities capabilities = DesiredCapabilities.iphone();
            capabilities.setCapability("device", PropertyManager.getProperty("app.platform"));
            capabilities.setCapability("deviceName", "Iphone X");
            capabilities.setCapability("platformName", PropertyManager.getProperty("app.platform"));
            capabilities.setCapability("appPackage", PropertyManager.getProperty("app.package"));
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability("app", System.getProperty("user.dir").concat(File.separator).concat(MOBILE_APP_PATH.concat(File.separator).concat(PropertyManager.getProperty("mobile.app.name"))));
            return capabilities;
        }

    };

    private static final String MOBILE_APP_PATH = PropertyManager.getProperty("crowdar.mobile.apk.path");

	public static MobilePlatformConfiguration getPlatformConfiguration(String key) {

        for (MobilePlatformConfiguration current : values()) {
            if (current.name().equalsIgnoreCase(key)) {
                return current;
            }
        }

        return null;
    }


}

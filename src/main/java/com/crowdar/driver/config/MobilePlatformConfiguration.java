package com.crowdar.driver.config;

import com.crowdar.core.PropertyManager;

import org.openqa.selenium.remote.DesiredCapabilities;

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
            capabilities.setCapability("app", PropertyManager.getProperty("crowdar.mobile.apk.path"));
            return capabilities;
        }

    };

   	public static MobilePlatformConfiguration getPlatformConfiguration(String key) {

        for (MobilePlatformConfiguration current : values()) {
            if (current.name().equalsIgnoreCase(key)) {
                return current;
            }
        }

        return null;
    }


}

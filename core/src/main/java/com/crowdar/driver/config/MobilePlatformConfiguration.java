package com.crowdar.driver.config;

import com.crowdar.core.PropertyManager;

import org.openqa.selenium.remote.DesiredCapabilities;

public enum MobilePlatformConfiguration implements AutomationConfiguration {

    ANDROID_APK {
        @Override
        public DesiredCapabilities getDesiredCapabilities() {

            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("deviceName", PropertyManager.getProperty("crowdar.mobile.deviceName"));
            if (!PropertyManager.getProperty("crowdar.mobile.avd").isEmpty()) {
                capabilities.setCapability("avd", PropertyManager.getProperty("crowdar.mobile.avd"));
            }
            capabilities.setCapability("browserName", "android");
            capabilities.setCapability("app", PropertyManager.getProperty("crowdar.mobile.apk.path"));
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability("platformName", "Android");
            capabilities.setCapability("platformVersion", PropertyManager.getProperty("crowdar.mobile.platformVersion"));

//            if (!PropertyManager.getProperty("crowdar.mobile.appium.newCommandTimeout").isEmpty()) {
//            }
            capabilities.setCapability("newCommandTimeout", Integer.parseInt(PropertyManager.getProperty("crowdar.mobile.appium.newCommandTimeout")));
            capabilities.setCapability("automationName",PropertyManager.getProperty("crowdar.mobile.automationName"));
            
            capabilities.setCapability("unicodeKeyboard", true);
            capabilities.setCapability("resetKeyboard", true);
            return capabilities;
        }
    }, ANDROID_CHROME {
        @Override
        public DesiredCapabilities getDesiredCapabilities() {

            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setCapability("deviceName", PropertyManager.getProperty("crowdar.mobile.deviceName"));
            capabilities.setCapability("platformName", PropertyManager.getProperty("crowdar.platformName"));
            capabilities.setVersion(PropertyManager.getProperty("crowdar.browserVersion"));
            return capabilities;
        }

    }, IOS {
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

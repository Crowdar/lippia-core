package com.crowdar.mobile.mobileDriver;

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

public enum PlatformConfiguration {

    ANDROID {
        File app;

        @Override
        public void localSetup() {
            File appDir = new File(System.getProperty("user.dir") + "/src/main/resources/mobile/app");
            app = new File(appDir, PropertyManager.getProperty("app.apk.name"));
        }

        @Override
        public DesiredCapabilities getDesiredCapabilities() {

            DesiredCapabilities capabilities = DesiredCapabilities.android();
            capabilities.setPlatform(Platform.ANDROID);

            capabilities.setCapability("device", PropertyManager.getProperty("app.platform"));

            capabilities.setCapability("deviceName", "Xperia XA1");
            capabilities.setCapability("platformName", PropertyManager.getProperty("app.platform"));

            capabilities.setCapability("appPackage", PropertyManager.getProperty("app.package"));
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability("unicodeKeyboard", true);
            capabilities.setCapability("resetKeyboard", true);
            capabilities.setCapability("app", app.getAbsolutePath());

            return capabilities;
        }

        @Override
        public AppiumDriver initDriver(String url, DesiredCapabilities desiredCapabilities) {
            try {
                return new AndroidDriver(new URL(url), desiredCapabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        }
    },
    IOS {
        File app;

        @Override
        public void localSetup() {
            File appDir = new File(System.getProperty("user.dir") + "/src/main/resources/app/");
            app = new File(appDir, PropertyManager.getProperty("app.app.name"));
        }

        @Override
        public DesiredCapabilities getDesiredCapabilities() {

            DesiredCapabilities capabilities = DesiredCapabilities.iphone();
            //capabilities.setPlatform(Platform.ANDROID);

            capabilities.setCapability("device", PropertyManager.getProperty("app.platform"));

            capabilities.setCapability("deviceName", "Iphone X");
            capabilities.setCapability("platformName", PropertyManager.getProperty("app.platform"));

            capabilities.setCapability("appPackage", PropertyManager.getProperty("app.package"));
            capabilities.setCapability("autoGrantPermissions", true);
            capabilities.setCapability("unicodeKeyboard", true);
            capabilities.setCapability("resetKeyboard", true);

            capabilities.setCapability("app", app.getAbsolutePath());

            return capabilities;
        }

        @Override
        public AppiumDriver initDriver(String url, DesiredCapabilities desiredCapabilities) {
            try {
                return new IOSDriver(new URL(url), desiredCapabilities);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }
    };

    public static PlatformConfiguration getPlatformConfiguration(String key) {

        for (PlatformConfiguration current : values()) {
            if (current.name().equalsIgnoreCase(key)) {
                return current;
            }
        }

        return null;
    }

    private final String DRIVER_GRID_HUB_KEY = "gridHub";

    public abstract void localSetup();

    public abstract DesiredCapabilities getDesiredCapabilities();

    public abstract AppiumDriver initDriver(String url, DesiredCapabilities desiredCapabilities);

    public AppiumDriver getDriver() {
        AppiumDriver driver = null;
        if (isGridConfiguration()) {
            try {
                System.out.println("############################################ AppiumDriver mode: Grid");
                driver = (AppiumDriver) SingleWebDriverPool.DEFAULT.getDriver(new URL(System.getProperty(DRIVER_GRID_HUB_KEY)), getDesiredCapabilities());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("############################################ AppiumDriver mode: Default");
            localSetup();
            driver = initDriver("http://127.0.0.1:4723/wd/hub", getDesiredCapabilities());
        }
        return driver;
    }


    private boolean isGridConfiguration() {
        String driverHub = System.getProperty(DRIVER_GRID_HUB_KEY);
        return driverHub != null && !driverHub.isEmpty();
    }

}

package com.crowdar.driver;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.config.AutomationConfiguration;
import com.crowdar.driver.config.BrowserConfiguration;
import com.crowdar.driver.config.MobilePlatformConfiguration;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.safari.SafariDriver;

public enum ProjectTypeEnum {
    WEB_CHROME {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return ChromeDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.CHROME;
        }

    },
    WEB_FIREFOX {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return FirefoxDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.FIREFOX;
        }

    },
    WEB_EDGE {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return EdgeDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.EDGE;
        }

    },
    WEB_IE {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return InternetExplorerDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.IE;
        }

    },
    WEB_SAFARI {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return SafariDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.SAFARI;
        }

    },
    WEB_CHROME_EXTENCION {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return ChromeDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.CHROME_EXTENCION;
        }

    },
    WEB_CHROME_HEADLESS {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return ChromeDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.CHROME_HEADLESS;
        }

    },
    WEB_CHROME_CUSTOM {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return ChromeDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.CUSTOM_CHROME;
        }

    },
    MOBILE_ANDROID_APK {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return AndroidDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return MobilePlatformConfiguration.ANDROID_APK;
        }

    },
    MOBILE_ANDROID_CHROME {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return AndroidDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return MobilePlatformConfiguration.ANDROID_CHROME;
        }

    },
    MOBILE_IOS {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return IOSDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return MobilePlatformConfiguration.IOS;
        }

    },
    // MOBILE_CHROME

    WIN32 {
        @Override
        public Class<? extends RemoteWebDriver> getDriverImplementation() {
            return null;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return null;
        }
    };

    public abstract Class<? extends RemoteWebDriver> getDriverImplementation();

    public abstract AutomationConfiguration getDriverConfig();


    public static ProjectTypeEnum get(String key) {
        try {
            return Enum.valueOf(ProjectTypeEnum.class, key);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid value for enum ProjectTypeEnum : " + key);
        }
    }

}

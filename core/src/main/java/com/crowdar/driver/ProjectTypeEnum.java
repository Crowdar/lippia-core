package com.crowdar.driver;

import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.crowdar.core.PropertyManager;
import com.crowdar.driver.config.AutomationConfiguration;
import com.crowdar.driver.config.BrowserConfiguration;
import com.crowdar.driver.config.MobilePlatformConfiguration;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public enum ProjectTypeEnum {

    /**
     * driver:  Set first part of driver name. Possible values: Chrome, Firefox, Edge, InternetExplorer, Safari, Android, IOS.
     */
    GENERIC {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {

            String driverClass = PropertyManager.getProperty("crowdar.localDriverType");

            if (driverClass.isEmpty()) {
                String msg = String.format("Error getting driver type -- For local runs you need to specify a valid crowdar.localDriverType property. Possible values: Chrome, Firefox, Edge, InternetExplorer, Safari, Android, IOS. /r Current Values is '%s'", driverClass);
                logger.error(msg);
                throw new RuntimeException(msg);
            }

            Class<? extends RemoteWebDriver> localDriverImplementation = null;
            String classPackage = "org.openqa.selenium." + driverClass.toLowerCase() + ".";

            try {
                localDriverImplementation = (Class<? extends RemoteWebDriver>) Class.forName(classPackage + driverClass + "Driver");
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Invalid value for localDriverImplementation: " + driverClass);
            }

            return localDriverImplementation;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.GENERIC;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
        	   String driverClass = PropertyManager.getProperty("crowdar.remoteDriverType");

               if (driverClass.isEmpty()) {
                   String msg = String.format("Error getting driver type -- For remote runs you need to specify a valid crowdar.remoteDriverType property. /r Current Values is '%s'", driverClass);
                   logger.error(msg);
                   throw new RuntimeException(msg);
               }
               
               Class<? extends RemoteWebDriver> remoteDriverImplementation;
			try {
				remoteDriverImplementation = (Class<? extends RemoteWebDriver>) Class.forName(driverClass);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Invalid value for remoteDriverImplementation: " + driverClass);
			}
            return remoteDriverImplementation;
        }
    },
    WEB_CHROME {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            return ChromeDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.CHROME;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return RemoteWebDriver.class;
        }
    },
    WEB_FIREFOX {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            return FirefoxDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.FIREFOX;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return RemoteWebDriver.class;
        }

    },
    WEB_EDGE {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            return EdgeDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.EDGE;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return RemoteWebDriver.class;
        }

    },
    WEB_IE {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            return InternetExplorerDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.IE;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return RemoteWebDriver.class;
        }

    },
    WEB_SAFARI {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            return SafariDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.SAFARI;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return RemoteWebDriver.class;
        }

    },
    WEB_CHROME_EXTENCION {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            return ChromeDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.CHROME_EXTENCION;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return RemoteWebDriver.class;
        }

    },
    WEB_CHROME_HEADLESS {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            return ChromeDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.CHROME_HEADLESS;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return RemoteWebDriver.class;
        }

    },
    WEB_CHROME_CUSTOM {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            return ChromeDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return BrowserConfiguration.CUSTOM_CHROME;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return RemoteWebDriver.class;
        }

    },
    MOBILE_ANDROID_APK {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            //TODO: no aplica para appium!
            return null;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return MobilePlatformConfiguration.ANDROID_APK;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return AndroidDriver.class;
        }

    },
    MOBILE_ANDROID_CHROME {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            //TODO: no aplica para appium!
            return null;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return MobilePlatformConfiguration.ANDROID_CHROME;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return AndroidDriver.class;
        }

    },
    MOBILE_IOS {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            return IOSDriver.class;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return MobilePlatformConfiguration.IOS;
        }


        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return getLocalDriverImplementation();
        }

    },
    // MOBILE_CHROME

    WIN32 {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            return null;
        }

        @Override
        public AutomationConfiguration getDriverConfig() {
            return null;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return null;
        }
    };

    private static Logger logger = Logger.getLogger(ProjectTypeEnum.class);

    /**
     * @return driver type to create instance on LocalWebExecutionStrategy or DownloadLatestStrategy. both local.
     */
    public abstract Class<? extends RemoteWebDriver> getLocalDriverImplementation();

    /**
     * @return driver type to create instance with selenium hub Strategy.
     */
    public abstract Class<? extends RemoteWebDriver> getRemoteDriverImplementation();

    public abstract AutomationConfiguration getDriverConfig();


    public static ProjectTypeEnum get(String key) {
        try {
            return Enum.valueOf(ProjectTypeEnum.class, key);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid value for enum ProjectTypeEnum : " + key);
        }
    }

}
package com.crowdar.driver;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import com.crowdar.core.JsonUtils;
import com.crowdar.core.PropertyManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;

public enum ProjectTypeEnum {

    GENERIC {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {

            String driverClass = PropertyManager.getProperty("crowdar.projectType.localDriverType");

            if (driverClass.isEmpty()) {
                String msg = String.format("Error getting driver type -- For local runs you need to specify a valid crowdar.localDriverType property. /r Current Values is '%s'", driverClass);
                logger.error(msg);
                throw new RuntimeException(msg);
            }

            Class<? extends RemoteWebDriver> localDriverImplementation = null;

            try {
                localDriverImplementation = (Class<? extends RemoteWebDriver>) Class.forName(driverClass);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException("Invalid value for localDriverImplementation: " + driverClass);
            }

            return localDriverImplementation;
        }

        @Override
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
        	/**
        	 * complete package and class name, example: "org.openqa.selenium.remote.RemoteWebDriver"
        	 */
        	   String driverClass = PropertyManager.getProperty("crowdar.projectType.remoteDriverType");

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
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return RemoteWebDriver.class;
        }

    },
    MOBILE_ANDROID {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
            //TODO: no aplica para appium!
            return null;
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
        public Class<? extends RemoteWebDriver> getRemoteDriverImplementation() {
            return getLocalDriverImplementation();
        }

    },

    WIN32 {
        @Override
        public Class<? extends RemoteWebDriver> getLocalDriverImplementation() {
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

    
    public DesiredCapabilities getDesiredCapabilities() {
        String path = PropertyManager.getProperty("crowdar.projectType.driverCapabilities.jsonFile");
        if (path == null || path.isEmpty()) {
            String msg = String.format("Error creating driver -- Please define property crowdar.driver.capabilities.json.path in config.property properly");
            logger.error(msg);
            throw new RuntimeException(msg);
        }
        TreeMap<String, ?> result = null;
        try {
            String capabilities = JsonUtils.getJSON(Paths.get(path));
            capabilities = JsonUtils.replaceVarsFromPropertyManager(capabilities);

            result = new ObjectMapper().readValue(capabilities, TreeMap.class);

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return new DesiredCapabilities(result);

    }

    public static ProjectTypeEnum get(String key) {
        try {
            return Enum.valueOf(ProjectTypeEnum.class, key);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid value for enum ProjectTypeEnum : " + key);
        }
    }

}
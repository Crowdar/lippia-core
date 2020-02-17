package com.crowdar.driver.config;

import com.crowdar.core.JsonUtils;
import com.crowdar.core.PropertyManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.bonigarcia.wdm.DriverManagerType;
import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.TreeMap;

public enum BrowserConfiguration implements AutomationConfiguration {

    GENERIC {
        @Override
        public DriverManagerType getDriverManagerType() {
            String bonigarciaDriverType = PropertyManager.getProperty("crowdar.localDriverType");
            return DriverManagerType.valueOf(bonigarciaDriverType.toUpperCase());
        }

        @Override
        public DesiredCapabilities getDesiredCapabilities() {
            return capabilitiesFromJson("BrowserConfiguration_GENERIC");
        }
    },
    FIREFOX {
        @Override
        public DriverManagerType getDriverManagerType() {
            return DriverManagerType.FIREFOX;
        }

        @Override
        public DesiredCapabilities getDesiredCapabilities() {
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            capabilities.setBrowserName("firefox");
            FirefoxOptions options = new FirefoxOptions();
            options.addArguments("disable-infobars");
            options.addArguments("--ignore-certificate-errors");
            capabilities.setCapability(FirefoxOptions.FIREFOX_OPTIONS, options);
            return capabilities;
        }
    },
    EDGE {
        @Override
        public DriverManagerType getDriverManagerType() {
            return DriverManagerType.EDGE;
        }

        @Override
        public DesiredCapabilities getDesiredCapabilities() {
            DesiredCapabilities edgeCapabilities = DesiredCapabilities.edge();
            edgeCapabilities.setPlatform(Platform.WIN10);
            edgeCapabilities.setBrowserName(edgeCapabilities.getBrowserName());

            return edgeCapabilities;
        }
    },
    IE {
        @Override
        public DriverManagerType getDriverManagerType() {
            return DriverManagerType.IEXPLORER;
        }

        @Override
        public DesiredCapabilities getDesiredCapabilities() {
            DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
            capabilities.setPlatform(Platform.VISTA);
            capabilities.setBrowserName(capabilities.getBrowserName());
            return capabilities;
        }
    },
    SAFARI {
        @Override
        public DriverManagerType getDriverManagerType() {
            return null;
        }

        @Override
        public DesiredCapabilities getDesiredCapabilities() {
            DesiredCapabilities capabilities = DesiredCapabilities.safari();
            capabilities.setPlatform(Platform.MAC);
            capabilities.setBrowserName("safari");
            return capabilities;
        }
    },
    CHROME {
        @Override
        public DriverManagerType getDriverManagerType() {
            return DriverManagerType.CHROME;
        }

        @Override
        public DesiredCapabilities getDesiredCapabilities() {

            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setBrowserName(capabilities.getBrowserName());
            capabilities.setCapability("platformName", PropertyManager.getProperty("crowdar.platformName"));
            capabilities.setVersion(PropertyManager.getProperty("crowdar.browserVersion"));
            ChromeOptions options = new ChromeOptions();
            options.addArguments("disable-infobars");
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("start-maximized");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            return capabilities;
        }
    },
    CHROME_HEADLESS {
        @Override
        public DriverManagerType getDriverManagerType() {
            return DriverManagerType.CHROME;
        }

        @Override
        public DesiredCapabilities getDesiredCapabilities() {

            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setBrowserName(capabilities.getBrowserName());

            ChromeOptions options = new ChromeOptions();
            options.addArguments("disable-infobars");
            options.addArguments("--ignore-certificate-errors");
            options.addArguments("--headless");
            options.addArguments("start-maximized");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            return capabilities;
        }
    },
    CHROME_EXTENCION {
        @Override
        public DriverManagerType getDriverManagerType() {
            return DriverManagerType.CHROME;
        }

        @Override
        public DesiredCapabilities getDesiredCapabilities() {
            DesiredCapabilities capabilities = DesiredCapabilities.chrome();
            capabilities.setBrowserName(capabilities.getBrowserName());
            ChromeOptions options = new ChromeOptions();
            ChromeUtils.insertHeadersExtension(options);
            options.addArguments("disable-infobars");
            options.addArguments("start-maximized");
            capabilities.setCapability(ChromeOptions.CAPABILITY, options);
            return capabilities;
        }
    },
    CUSTOM_CHROME {
        @Override
        public DriverManagerType getDriverManagerType() {
            return DriverManagerType.CHROME;
        }

        @Override
        public DesiredCapabilities getDesiredCapabilities() {
            return capabilitiesFromJson("CUSTOM_CHROME");
        }
    };

    public DesiredCapabilities capabilitiesFromJson(String driverName) {
        String path = PropertyManager.getProperty("crowdar.driver.capabilities.json_path");
        if (path == null || path.isEmpty()) {
            String msg = String.format("Error creating %s driver -- Please define property crowdar.driver.capabilities.json.path in config.property properly ", driverName);
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

    private Logger logger = Logger.getLogger(BrowserConfiguration.class);

    public static BrowserConfiguration getBrowserConfiguration(String key) {

        for (BrowserConfiguration current : values()) {
            if (current.name().equalsIgnoreCase(key)) {
                return current;
            }
        }
        return null;
    }

    public abstract DriverManagerType getDriverManagerType();


}

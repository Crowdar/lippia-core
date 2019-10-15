package com.crowdar.driver.config;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import com.crowdar.core.PropertyManager;
import com.crowdar.json.JsonUtil;
import com.crowdar.web.ChromeUtils;
import com.fasterxml.jackson.core.type.TypeReference;

import io.github.bonigarcia.wdm.DriverManagerType;

public enum BrowserConfiguration implements AutomationConfiguration{

    FIREFOX {
    	@Override
		public DriverManagerType getDriverManagerType() {
			return DriverManagerType.FIREFOX;
		}

        @Override
        public DesiredCapabilities getDesiredCapabilities() {
            DesiredCapabilities capabilities = DesiredCapabilities.firefox();
            capabilities.setBrowserName("firefox");
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

//			System.setProperty("webdriver.ie.driver.loglevel","DEBUG");

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

//			System.setProperty("webdriver.ie.driver.loglevel","DEBUG");

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
	CUSTOM_CHROME{
    	@Override
		public DriverManagerType getDriverManagerType() {
			return DriverManagerType.CHROME;
		}

		@Override
		public DesiredCapabilities getDesiredCapabilities() {
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilitiesFromJson(capabilities,"CUSTOM_CHROME");
			return capabilities;

		}
	};

	public  void capabilitiesFromJson(DesiredCapabilities capabilities,String driverName)  {
        String path =  PropertyManager.getProperty("crowdar.driver.capabilities.json.path");
        if(path == null || path.isEmpty()){
        	String msg = String.format("Error creating %s driver -- Please define property crowdar.driver.capabilities.json.path in config.property properly ",driverName);
        	logger.error(msg);
        	throw new RuntimeException(msg);
        }

		try {
			Map<String, String> map = JsonUtil.i().getMapper().readValue(new File(path),new TypeReference<Map<String,String>>(){});
			map.keySet().stream().forEach(k->{capabilities.setCapability(k,map.get(k));});
		} catch (IOException e) {
        	logger.error(e.getMessage());
			e.printStackTrace();
		}

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

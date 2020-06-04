package com.crowdar.driver;

import com.crowdar.core.PropertyManager;
import com.crowdar.driver.setupStrategy.SetupStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

class DriverFactory {

    private static Logger logger = Logger.getLogger(DriverFactory.class);
    private static final String DEFAULT_STRATEGY = "NoneStrategy";
    private static final String STRATEGY_CLASS = "com.crowdar.driver.setupStrategy.%s";

    
    public static RemoteWebDriver createDriver() {
        try {
        	ProjectTypeEnum projectType = getProjectType();

        	SetupStrategy setupStrategy = getStrategy();
            
            URL driverHub = getDriverHub();
            
            return createDriver(projectType, setupStrategy, driverHub, null);

        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | UnreachableBrowserException e) {
            logger.error(e.getCause());
            throw new RuntimeException("Error creating driver", e.getCause());

        } catch (ClassNotFoundException e) {
            logger.error("error loading strategy class: com.crowdar.driver.setupStrategy." + PropertyManager.getProperty("crowdar.setupStrategy"));
            logger.error("Verify if path exist.");
            throw new RuntimeException("Error creating driver");

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    
    public static RemoteWebDriver createDriver(Map<String, ?> extraCapabilities) {
        try {
        	ProjectTypeEnum projectType = getProjectType();

            SetupStrategy setupStrategy = getStrategy();
            
            URL driverHub = getDriverHub();
            
            return createDriver(projectType, setupStrategy, driverHub, extraCapabilities);

        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | UnreachableBrowserException e) {
            logger.error(e.getCause());
            throw new RuntimeException("Error creating driver", e.getCause());

        } catch (ClassNotFoundException e) {
            logger.error("error loading strategy class: com.crowdar.driver.setupStrategy." + PropertyManager.getProperty("crowdar.setupStrategy"));
            logger.error("Verify if path exist.");
            throw new RuntimeException("Error creating driver");

        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static RemoteWebDriver createDriver(ProjectTypeEnum projectType, SetupStrategy setupStrategy, URL driverHub, Map<String, ?> extraCapabilities) {
    	try {
            setupStrategy.beforeDriverStartSetup(projectType);

            RemoteWebDriver driver;
            
            DesiredCapabilities capabilities = getCapabilities(projectType.getDesiredCapabilities(), extraCapabilities);
            
            if (driverHub == null) {
                Constructor<?> constructor = projectType.getLocalDriverImplementation().getDeclaredConstructor(Capabilities.class);
                driver = (RemoteWebDriver) constructor.newInstance(capabilities);
            } else {
                Constructor<?> constructor = projectType.getRemoteDriverImplementation().getDeclaredConstructor(URL.class, Capabilities.class);
                driver = (RemoteWebDriver) constructor.newInstance(driverHub, capabilities);
            }

            setupStrategy.afterDriverStartSetup(driver);
            return driver;

        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
                | IllegalArgumentException | InvocationTargetException | UnreachableBrowserException e) {
            logger.error(e.getCause());
            throw new RuntimeException("Error creating driver", e.getCause());
        }
    }



    private static URL getDriverHub() throws MalformedURLException {
		//driver Hub Configuration
		URL driverHub = null;
		if (!StringUtils.isEmpty(PropertyManager.getProperty("crowdar.driverHub"))) {
			driverHub = new URL(PropertyManager.getProperty("crowdar.driverHub"));
		}
		return driverHub;
	}

	private static SetupStrategy getStrategy() throws ClassNotFoundException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// Strategy to Execute 
		String strategy = PropertyManager.getProperty("crowdar.setupStrategy");
		Class<?> StrategyClass;
		if (StringUtils.isEmpty(strategy)) {
		    StrategyClass = Class.forName(String.format(STRATEGY_CLASS, DEFAULT_STRATEGY));
		} else {
		    StrategyClass = Class.forName(String.format(STRATEGY_CLASS, strategy));
		}
		SetupStrategy setupStrategy = (SetupStrategy) StrategyClass.getDeclaredConstructor().newInstance();
		return setupStrategy;
	}

	private static ProjectTypeEnum getProjectType() {
		//project type configuration 
		ProjectTypeEnum projectType = ProjectTypeEnum.get(PropertyManager.getProperty(ProjectTypeEnum.PROJECT_TYPE_KEY));
		return projectType;
	}
    
    
    
	private static DesiredCapabilities getCapabilities(DesiredCapabilities desiredCapabilities, Map<String, ?> extraCapabilities) {
		
		if(extraCapabilities!=null) {
			desiredCapabilities = desiredCapabilities.merge(new DesiredCapabilities(extraCapabilities));
		}
		
		return desiredCapabilities;
	}
    

}

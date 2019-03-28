package com.crowdar.driver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import com.crowdar.core.PropertyManager;
import com.crowdar.driver.setupStrategy.SetupStrategy;

class  DriverFactory {
	
	private static Logger logger = Logger.getLogger(DriverFactory.class);

	

	protected static RemoteWebDriver createDriver() {
		try {
			
			ProjectTypeEnum projectType = ProjectTypeEnum.get(PropertyManager.getProperty("crowdar.projectType"));

			Class<?> StrategyClass = Class.forName("com.crowdar.driver.setupStrategy."+PropertyManager.getProperty("crowdar.setupStrategy"));
			SetupStrategy setupStrategy = (SetupStrategy)StrategyClass.getDeclaredConstructor().newInstance();
			
			setupStrategy.beforeDriverStartSetup(projectType.getDriverConfig());
			
			RemoteWebDriver driver;
			
			if(StringUtils.isEmpty(PropertyManager.getProperty("crowdar.driverHub"))) {
				Constructor<?> constructor = projectType.getDriverImplementation().getDeclaredConstructor(Capabilities.class);
				driver = (RemoteWebDriver) constructor.newInstance(projectType.getDriverConfig().getDesiredCapabilities());
			}else {
				Constructor<?> constructor = projectType.getDriverImplementation().getDeclaredConstructor(URL.class, Capabilities.class);
				URL url = new URL(PropertyManager.getProperty("crowdar.driverHub"));
				driver = (RemoteWebDriver) constructor.newInstance(url, projectType.getDriverConfig().getDesiredCapabilities());
			}
			
			setupStrategy.afterDriverStartSetup();
			
			return driver;

		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException | UnreachableBrowserException e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating driver: ");
			
		} catch (ClassNotFoundException e) {
			logger.error("error loading strategy class: com.crowdar.driver.setupStrategy." +  PropertyManager.getProperty("crowdar.setupStrategy"));
			logger.error("Verify if path exist.");
			throw new RuntimeException("Error creating driver: ");
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}



}

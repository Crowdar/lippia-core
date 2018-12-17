package com.crowdar.driver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.core.PropertyManager;
import com.crowdar.driver.config.AutomationConfiguration;

public class DriverManager {
	
	private static RemoteWebDriver driver;
	
	public static RemoteWebDriver getDriverInstance() {
		if (driver == null || driver.getSessionId() == null) {
			driver = getDriver();
		}
		return driver;
	}

	private static RemoteWebDriver getDriver() {
		try {

			ProjectTypeEnum projectType = ProjectTypeEnum.get(PropertyManager.getProperty("crowdar.projectType"));

			Constructor<?> constructor = projectType.getDriverImplementation().getDeclaredConstructor(AutomationConfiguration.class);
			return (RemoteWebDriver) constructor.newInstance(projectType.getDriverConfig());

		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
			throw new RuntimeException("Error creating driver: ");
		}
	}

	public static void dismissDriver() {
		 driver.quit();
	}

}

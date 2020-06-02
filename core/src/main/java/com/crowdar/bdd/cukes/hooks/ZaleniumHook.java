package com.crowdar.bdd.cukes.hooks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.crowdar.driver.DriverManager;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.Before;

public class ZaleniumHook{

	@Before("@zaleniumHook")
	public void beforeScenario(Scenario scenario) throws IOException{
		
		Map<String, String> extraCapabilities = new HashMap<String, String>();
		extraCapabilities.put("testFileNameTemplate", "{testName}");
		extraCapabilities.put("build",System.getProperty("build.identifier"));
		extraCapabilities.put("name", scenario.getName());
		
		try {
			DriverManager.initialize(extraCapabilities);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
	}
	

}

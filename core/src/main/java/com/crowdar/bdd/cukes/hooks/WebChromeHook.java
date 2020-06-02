package com.crowdar.bdd.cukes.hooks;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.crowdar.driver.DriverManager;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.Before;

public class WebChromeHook{

	@Before("@webChromeHook")
	public void beforeScenario(Scenario scenario) throws IOException{
		
	}
	

}

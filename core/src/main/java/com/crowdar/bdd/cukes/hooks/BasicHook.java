package com.crowdar.bdd.cukes.hooks;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.crowdar.core.Injector;
import com.crowdar.driver.DriverManager;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;

public class BasicHook {

	@Before()
	public void beforeScenario(Scenario scenario) throws IOException{
		Logger.getRootLogger().info("------ Starting -----" + scenario.getName() + "-----");
	}
	
	@BeforeStep
	public void beforeStep(Scenario scenario) {

	}

    @AfterStep
    public void afterStep(Scenario scenario) {
    	
    }
	
	@After()
	public void afterScenario(Scenario scenario) throws IllegalAccessException, NoSuchFieldException {
		Logger.getRootLogger().info("------ Ending -----" + scenario.getName() + "-----");
		DriverManager.dismissCurrentDriver();
		Injector.cleanThreadCache();
	}


}

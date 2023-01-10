package com.crowdar.bdd.cukes.hooks;

import java.io.IOException;

import com.crowdar.core.Injector;
import com.crowdar.core.actions.ActionManager;
import com.crowdar.driver.DriverManager;

import io.cucumber.core.api.Scenario;
import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import org.apache.log4j.Logger;

public class BasicHook {

	@Before()
	public void beforeScenario(Scenario scenario) throws IOException{
		Logger.getLogger(this.getClass()).info("------ Starting -----" + scenario.getName() + "-----");
	}
	
	@After()
	public void afterScenario(Scenario scenario) throws IllegalAccessException, NoSuchFieldException {
		Logger.getLogger(this.getClass()).info("------ Ending -----" + scenario.getName() + "-----");
		DriverManager.dismissCurrentDriver();
		Injector.cleanThreadCache();
		ActionManager.clean();
	}


}

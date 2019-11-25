package com.crowdar.driver.setupStrategy;

import com.crowdar.driver.config.AutomationConfiguration;

public interface SetupStrategy {
	
	public void beforeDriverStartSetup(AutomationConfiguration contextConfiguration);
	public void afterDriverStartSetup(); 
}

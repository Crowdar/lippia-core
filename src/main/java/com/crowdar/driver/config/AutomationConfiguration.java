package com.crowdar.driver.config;

import org.openqa.selenium.remote.DesiredCapabilities;

public interface AutomationConfiguration {

//	void localSetup();
	
    public abstract DesiredCapabilities getDesiredCapabilities();

}

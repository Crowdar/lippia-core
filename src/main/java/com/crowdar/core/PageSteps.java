package com.crowdar.core;

import com.crowdar.bdd.cukes.SharedDriver;

import org.apache.log4j.Logger;
import org.jbehave.core.annotations.When;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import com.paulhammant.ngwebdriver.NgWebDriver;


/**
 *
 * @author jCarames
 *
 */
public abstract class PageSteps{
	
	private WebDriver driver;
	private NgWebDriver ngWebDriver;
	protected Logger logger;
	
	public PageSteps(WebDriver driver){
	    if (driver != null) {
	        this.driver = driver;
	        ngWebDriver = new NgWebDriver((JavascriptExecutor) driver);
	    }
	}

	public PageSteps(SharedDriver driver){
		this((WebDriver) driver);
		logger = Logger.getLogger(this.getClass());
	}

	public WebDriver getDriver() {
		return driver;
	}

	public NgWebDriver getNgWebDriver() {
		return ngWebDriver;
	}

	@When("close the active page")
	public void closePage(){
		driver.close();
	}
	
	@When("refresh the active page")
	public void refreshPage(){
		driver.navigate().refresh();
		getNgWebDriver().waitForAngularRequestsToFinish();
	}
}

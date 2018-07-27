package com.crowdar.bdd.cukes;


import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.openqa.selenium.WebDriver;


public class TestNgRunner extends AbstractTestNGCucumberTests {
    protected WebDriver driver;
    protected String BASE_URL = "https://www.kayak.com/cars";

   /* @After("@browser")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png"); //stick it in the report
        }
        driver.close();
    }*/


}
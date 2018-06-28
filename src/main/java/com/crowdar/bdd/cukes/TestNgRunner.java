package com.crowdar.bdd.cukes;


import cucumber.api.CucumberOptions;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import cucumber.api.testng.TestNGCucumberRunner;
import cucumber.api.testng.CucumberFeatureWrapper;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/*@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"com/crowdar/examples/testng"},
        tags = {"~@Ignore"},
        format = {
                "pretty",
                "html:target/cucumber-reports/cucumber-pretty",
                "json:target/cucumber-reports/json-reports/CucumberTestReport.json",
                "rerun:target/cucumber-reports/rerun-reports/rerun.txt"
        })*/
public class TestNgRunner extends AbstractTestNGCucumberTests {
    protected WebDriver driver;
    protected String BASE_URL = "https://www.kayak.com/cars";
}
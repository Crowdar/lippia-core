package com.crowdar.bdd.cukes;


import com.crowdar.zapi.collaborator.ZapiBuilder;
import com.crowdar.zapi.jenkins.reporter.ZfjConstants;
import com.crowdar.zapi.jenkins.reporter.ZfjReporter;
import com.crowdar.zapi.model.ZapiTestCase;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static List<Scenario> scenaries = new ArrayList<Scenario>();




    @After
    public void before(Scenario s) {
        if(s != null) {
          scenaries.add(s);
        }
    }

    @After("@browser")
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            final byte[] screenshot = ((TakesScreenshot) driver)
                    .getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png"); //stick it in the report
        }
        driver.close();
    }


    @AfterSuite
    public void afterSuite(){
        System.out.println("Starting reporting for zapi ------");
        System.out.println("Scenario size :"+scenaries.size());
        ZfjReporter reporter = ZapiBuilder.buildZapiReporterWithNewCycleForEachBuild();
        reporter.perform(1,getScenarioStatusMap(scenaries));
    }

    public Map<ZapiTestCase,Boolean> getScenarioStatusMap(List<Scenario> scenaries){
           Map<ZapiTestCase,Boolean> mapScenaries = new HashMap<ZapiTestCase,Boolean>();

            for(Scenario s : scenaries){
                ZapiTestCase test = new ZapiTestCase();
                test.setSummary(s.getName());
                mapScenaries.put(test,!s.isFailed());
            }
            return mapScenaries;
    }
}
package com.crowdar.bdd.jbehave;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.AfterScenario.Outcome;
import org.jbehave.core.failures.PendingStepFound;
import org.jbehave.core.failures.UUIDExceptionWrapper;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.web.selenium.WebDriverProvider;
import org.jbehave.web.selenium.WebDriverScreenshotOnFailure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotsOnFailure extends WebDriverScreenshotOnFailure {
	
	
	private WebDriver driver;
	
	public ScreenshotsOnFailure(WebDriverProvider driverProvider) {
        super(driverProvider, new StoryReporterBuilder());
    }

	public void setDriver(WebDriver driver) {
		this.driver = driver;
	}
	
	@AfterScenario(uponOutcome = Outcome.FAILURE)
    public void afterScenarioFailure(UUIDExceptionWrapper uuidWrappedFailure) throws Exception {
        if (uuidWrappedFailure instanceof PendingStepFound) {
            return; // we don't take screen-shots for Pending Steps
        }
        String screenshotPath = screenshotPath(uuidWrappedFailure.getUUID());
        String currentUrl = "[unknown page title]";
        try {
            currentUrl = driver.getCurrentUrl();
        } catch (Exception e) {
        }
        boolean savedIt = false;
        try {
            File file = new File(screenshotPath);
            byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            file.getParentFile().mkdirs();
            try {
                file.createNewFile();
                IOUtils.write(bytes, new FileOutputStream(file));
            } catch (IOException e) {
                throw new RuntimeException("Can't save file", e);
            }
            savedIt = true;
        } catch (Exception e) {
            System.out.println("Screenshot of page '" + currentUrl + ". Will try again. Cause: " + e.getMessage());
            // Try it again.  WebDriver (on SauceLabs at least?) has blank-page and zero length files issues.
            try {
                savedIt = driverProvider.saveScreenshotTo(screenshotPath);
            } catch (Exception e1) {
                System.err.println("Screenshot of page '" + currentUrl + "' has **NOT** been saved to '" + screenshotPath + "' because error '" + e.getMessage() + "' encountered. Stack trace follows:");
                e.printStackTrace();
                return;
            }
        }
        if (savedIt) {
            System.out.println("Screenshot of page '" + currentUrl + "' has been saved to '" + screenshotPath +"' with " + new File(screenshotPath).length() + " bytes");
        } else {
            System.err.println("Screenshot of page '" + currentUrl + "' has **NOT** been saved. If there is no error, perhaps the WebDriver type you are using is not compatible with taking screenshots");
        }
    }
}

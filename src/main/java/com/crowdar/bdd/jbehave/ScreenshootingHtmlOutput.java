package com.crowdar.bdd.jbehave;

import java.io.PrintStream;

import org.jbehave.core.failures.UUIDExceptionWrapper;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.web.selenium.WebDriverHtmlOutput;
import org.jbehave.web.selenium.WebDriverProvider;
import org.openqa.selenium.WebDriver;

public class ScreenshootingHtmlOutput extends WebDriverHtmlOutput {
	 
    private ScreenshotsOnFailure screenshotMaker;
    private WebDriver driver;
 
    public ScreenshootingHtmlOutput(PrintStream output,
            StoryReporterBuilder reporterBuilder,
            WebDriverProvider webDriverProvider) {
        super(output, reporterBuilder.keywords());
        this.driver = webDriverProvider.get();
        screenshotMaker = new ScreenshotsOnFailure(
            webDriverProvider);
    }
 
    @Override
    public void failed(String step, Throwable storyFailure) {
        super.failed(step, storyFailure);
        
        try {
            UUIDExceptionWrapper uuidWrappedFailure =
                (UUIDExceptionWrapper) storyFailure;
            screenshotMaker.setDriver(driver);
            screenshotMaker.afterScenarioFailure(uuidWrappedFailure);
        } catch (Exception e) {
            System.out.println("Screenshot failed");
        }
    }
}
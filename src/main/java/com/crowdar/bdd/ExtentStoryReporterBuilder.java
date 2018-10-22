package com.crowdar.bdd;

import com.crowdar.report.JbehaveExtentReporter;
import org.jbehave.core.reporters.NullStoryReporter;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.openqa.selenium.WebDriver;

public class ExtentStoryReporterBuilder extends StoryReporterBuilder {

	private WebDriver driver;
    public ExtentStoryReporterBuilder() {

    }
	
	public ExtentStoryReporterBuilder(WebDriver driver) {
		this.driver = driver;
	}
	
    @Override
    public StoryReporter build(String storyPath) {
        StoryReporter delegate = super.build(storyPath);
        return new JbehaveExtentReporter(new NullStoryReporter(), delegate, false);
    }

}

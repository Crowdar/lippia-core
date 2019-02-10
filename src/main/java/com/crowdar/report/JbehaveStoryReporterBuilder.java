package com.crowdar.report;

import org.jbehave.core.reporters.NullStoryReporter;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class JbehaveStoryReporterBuilder extends StoryReporterBuilder {

	private RemoteWebDriver driverInstance;
	
    public JbehaveStoryReporterBuilder(RemoteWebDriver driverInstance) {
    	this.driverInstance = driverInstance;
	}

	@Override
    public StoryReporter build(String storyPath) {
        StoryReporter delegate = super.build(storyPath);
        return new JbehaveExtentReporter(driverInstance, new NullStoryReporter(), delegate, false);  
    }

}

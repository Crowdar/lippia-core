package com.crowdar.report;

import org.jbehave.core.reporters.NullStoryReporter;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.openqa.selenium.WebDriver;

public class JbehaveStoryReporterBuilder extends StoryReporterBuilder {

	private WebDriver driverInstance;
	
    public JbehaveStoryReporterBuilder(WebDriver driverInstance) {
    	this.driverInstance = driverInstance;
	}

	@Override
    public StoryReporter build(String storyPath) {
        StoryReporter delegate = super.build(storyPath);
        return new JbehaveExtentReporter(driverInstance, new NullStoryReporter(), delegate, false);  
    }

}

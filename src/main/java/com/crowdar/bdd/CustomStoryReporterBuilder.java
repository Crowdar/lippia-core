package com.crowdar.bdd;

import org.jbehave.core.reporters.NullStoryReporter;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.openqa.selenium.WebDriver;

public class CustomStoryReporterBuilder extends StoryReporterBuilder {

	private WebDriver driverInstance;
	
    public CustomStoryReporterBuilder(WebDriver driverInstance) {
    	this.driverInstance = driverInstance;
	}

	@Override
    public StoryReporter build(String storyPath) {
        StoryReporter delegate = super.build(storyPath);
        return new CustomStoryReporter(driverInstance, new NullStoryReporter(), delegate, false);  
    }

}

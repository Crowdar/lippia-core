package com.crowdar.report;

import org.jbehave.core.model.*;
import org.jbehave.core.reporters.StoryReporter;

import java.util.List;
import java.util.Map;

public class JbehaveReport implements StoryReporter {
    @Override
    public void storyNotAllowed(Story story, String s) {

    }

    @Override
    public void storyCancelled(Story story, StoryDuration storyDuration) {

    }

    @Override
    public void beforeStory(Story story, boolean b) {

    }

    @Override
    public void afterStory(boolean b) {

    }

    @Override
    public void narrative(Narrative narrative) {

    }

    @Override
    public void lifecyle(Lifecycle lifecycle) {

    }

    @Override
    public void scenarioNotAllowed(Scenario scenario, String s) {

    }

    @Override
    public void beforeScenario(Scenario scenario) {

    }

    @Override
    public void beforeScenario(String s) {

    }

    @Override
    public void scenarioMeta(Meta meta) {

    }

    @Override
    public void afterScenario() {

    }

    @Override
    public void beforeGivenStories() {

    }

    @Override
    public void givenStories(GivenStories givenStories) {

    }

    @Override
    public void givenStories(List<String> list) {

    }

    @Override
    public void afterGivenStories() {

    }

    @Override
    public void beforeExamples(List<String> list, ExamplesTable examplesTable) {

    }

    @Override
    public void example(Map<String, String> map) {

    }

    @Override
    public void afterExamples() {

    }

    @Override
    public void beforeStep(String s) {

    }

    @Override
    public void successful(String s) {

    }

    @Override
    public void ignorable(String s) {

    }

    @Override
    public void comment(String s) {

    }

    @Override
    public void pending(String s) {

    }

    @Override
    public void notPerformed(String s) {

    }

    @Override
    public void failed(String s, Throwable throwable) {

    }

    @Override
    public void failedOutcomes(String s, OutcomesTable outcomesTable) {

    }

    @Override
    public void restarted(String s, Throwable throwable) {

    }

    @Override
    public void restartedStory(Story story, Throwable throwable) {

    }

    @Override
    public void dryRun() {

    }

    @Override
    public void pendingMethods(List<String> list) {

    }
}

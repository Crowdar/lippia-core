package com.crowdar.report;


import cucumber.api.PickleStepTestStep;
import cucumber.api.TestCase;
import cucumber.api.event.*;
import gherkin.ast.Examples;
import gherkin.ast.Feature;
import gherkin.ast.ScenarioOutline;
import gherkin.ast.Step;

import java.util.ArrayList;
import java.util.List;


public class CucumberExtentReport extends CucumberEventListener{


    private ThreadLocal<String> currentFeatureFile = new ThreadLocal<String>();
    private ThreadLocal<ScenarioOutline> currentScenarioOutline = new ThreadLocal<ScenarioOutline>();
    private List<String> uris = new ArrayList<String>();



    @Override
    public void handleTestSourceRead(TestSourceRead event) {
        sourceModel.addTestSourceReadEvent(event.uri, event);
    }

    @Override
    public void handleTestCaseStarted(TestCaseStarted event) {
       System.out.println("TESTCASE "+event.testCase.getName() +" STARTED EN HILO :"+ Thread.currentThread().getId());
       handleStartOfFeature(event.testCase);
       handleScenarioOutline(event.testCase);
       ExtentReportManager.createScenarioOutlineOrStandard(event.testCase.getName(),event.testCase.getScenarioDesignation(),event.testCase.getTags());
    }

    @Override
    public void handleTestStepStarted(TestStepStarted event) {

        if (event.testStep instanceof PickleStepTestStep) {
            System.out.println("TEST STEP STARTED EN HILO :"+ Thread.currentThread().getId());
            ExtentReportManager.addCucumberStep((PickleStepTestStep) event.testStep);
        }

    }

    @Override
    public void handleTestStepFinished(TestStepFinished event) {
        System.out.println("TEST STEP FINISHED EN HILO :"+ Thread.currentThread().getId());
        PickleStepTestStep pickleStepTestStep =  ExtentReportManager.pollCucumberStep();
        String keyword = getStepKeyword(pickleStepTestStep);
        ExtentReportManager.matchCucumberStep(keyword,pickleStepTestStep.getStepText());
        ExtentReportManager.createResult(event.result);
    }

    @Override
    public void handleEmbed(EmbedEvent event) {

    }

    @Override
    public void handleWrite(WriteEvent event) {

    }

    @Override
    public void finishReport() {
        System.out.println("TEST STEP FINISHED EN HILO :"+ Thread.currentThread().getId());
        ExtentReportManager.flush();

    }


    private void handleScenarioOutline(TestCase testCase) {
        CucumberSourceModel.AstNode astNode = sourceModel.getAstNode(currentFeatureFile.get(), testCase.getLine());
        if (CucumberSourceModel.isScenarioOutlineScenario(astNode)) {
            ScenarioOutline scenarioOutline = (ScenarioOutline)CucumberSourceModel.getScenarioDefinition(astNode);
            if (currentScenarioOutline.get() == null || !currentScenarioOutline.get().equals(scenarioOutline)) {
                currentScenarioOutline.set(scenarioOutline);
                ExtentReportManager.createScenarioOutlineNode(currentScenarioOutline.get().getName());
            }
            Examples examples = (Examples)astNode.parent.node;
            ExtentReportManager.createExampleCucumber(examples);

        } else {
            currentScenarioOutline.set(null);
        }
    }
    private void handleStartOfFeature(TestCase testCase) {
       Feature feature = sourceModel.getFeature(testCase.getUri());
       ExtentReportManager.createFeature(feature.getName(),feature.getTags());
       currentFeatureFile.set(testCase.getUri());
    }

    private String getStepKeyword(PickleStepTestStep testStep) {
        CucumberSourceModel.AstNode astNode = sourceModel.getAstNode(currentFeatureFile.get(), testStep.getStepLine());
        if (astNode != null) {
            Step step = (Step) astNode.node;
            return step.getKeyword();
        } else {
            return "";
        }
    }

}

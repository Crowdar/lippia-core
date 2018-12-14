package com.crowdar.report;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import gherkin.formatter.model.*;

import java.util.*;


public class CucumberExtentReport extends CucumberReport {
    private static boolean scenarioOutlineFlag;

    public void syntaxError(String state, String event, List<String> legalEvents, String uri,
                            Integer line) {

    }

    public void uri(String uri) {

    }

    public void feature(Feature feature) {
        ExtentTest test = ExtentReportManager.createTest(feature.getName());
        for (Tag tag : feature.getTags()) {
            test.assignCategory(tag.getName());
        }
    }

    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        scenarioOutlineFlag = true;
        ExtentReportManager.createScenarioOutlineNode(scenarioOutline.getName());
    }

    public void examples(Examples examples) {
        ExtentTest test = ExtentReportManager.getOutlineScenarioNode();

        String[][] data = null;
        List<ExamplesTableRow> rows = examples.getRows();
        int rowSize = rows.size();
        for (int i = 0; i < rowSize; i++) {
            ExamplesTableRow examplesTableRow = rows.get(i);
            List<String> cells = examplesTableRow.getCells();
            int cellSize = cells.size();
            if (data == null) {
                data = new String[rowSize][cellSize];
            }
            for (int j = 0; j < cellSize; j++) {
                data[i][j] = cells.get(j);
            }
        }
        test.info(MarkupHelper.createTable(data));
    }

    public void startOfScenarioLifeCycle(Scenario scenario) {
        if (scenarioOutlineFlag) {
            scenarioOutlineFlag = false;
        }
        ExtentTest scenarioNode = ExtentReportManager.createScenarioOutlineOrStandard(scenario.getName(),scenario.getKeyword());
        for (Tag tag : scenario.getTags()) {
            scenarioNode.assignCategory(tag.getName());
        }
    }

    public void background(Background background) {

    }

    public void scenario(Scenario scenario) {

    }

    public void step(Step step) {
        if (scenarioOutlineFlag) {
            return;
        }
        ExtentReportManager.addCucumberStep(step);
    }

    public void endOfScenarioLifeCycle(Scenario scenario) {

    }

    public void done() {
        ExtentReportManager.flush();
    }

    public void close() {

    }

    public void eof() {

    }

    public void before(Match match, Result result) {

    }

    public void result(Result result) {
        if (scenarioOutlineFlag) {
            return;
        }
        if (Result.PASSED.equals(result.getStatus())) {
           ExtentReportManager.addCucumberPassStep();
        } else if (Result.FAILED.equals(result.getStatus())) {
            ExtentReportManager.addCucumberFailStep(result.getError());
        } else if (Result.SKIPPED.equals(result)) {
            ExtentReportManager.addCucumberSkipStep();
        } else if (Result.UNDEFINED.equals(result)) {
            ExtentReportManager.addCucumberUndefinedStep();
        }
    }


    public void match(Match match) {
        Step step = ExtentReportManager.pollCucumberStep();
        String data[][] = null;
        if (step.getRows() != null) {
            List<DataTableRow> rows = step.getRows();
            int rowSize = rows.size();
            for (int i = 0; i < rowSize; i++) {
                DataTableRow dataTableRow = rows.get(i);
                List<String> cells = dataTableRow.getCells();
                int cellSize = cells.size();
                if (data == null) {
                    data = new String[rowSize][cellSize];
                }
                for (int j = 0; j < cellSize; j++) {
                    data[i][j] = cells.get(j);
                }
            }
        }
       ExtentReportManager.matchCucumberStep(data, step);
    }



}

package com.crowdar.zapi.jenkins.reporter;

import com.crowdar.report.CucumberReport;
import com.crowdar.zapi.collaborator.ZapiBuilder;
import com.crowdar.zapi.model.ZapiStepExecution;
import com.crowdar.zapi.model.ZapiTestCase;
import cucumber.api.java.After;
import gherkin.formatter.model.*;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JiraReporter extends CucumberReport {
    private Logger logger = Logger.getLogger(JiraReporter.class);

    @Override
    public void syntaxError(String s, String s1, List<String> list, String s2, Integer integer) {
        String out = String.format("@@@@@ Syntax Error(): s=%s, s1=%s, list=%s, s2=%s, integer=%s", s, s1, list, s2, integer);
        System.out.println(out);
    }

    @Override
    public void feature(Feature feature) {
        logger.info("-------- Report Jira starting for : "+ feature.getName());
    }

    @Override
    public void step(Step step) {
       logger.debug(" JiraReport ###################STEP");
        MonitorReport.getCurrentTest().getExecution().addStepExecution(new ZapiStepExecution(step.getName()));
    }

    @After
    public void getScenarioData(cucumber.api.Scenario scenario){
        logger.debug(" JiraReport ###################AFTER CUCUMBER");
        MonitorReport.getCurrentTest().setSummary(scenario.getName());
        String tag = scenario.getSourceTagNames().stream().filter(t -> t.contains("JIRA_")).findFirst().orElse(null);
        if(tag != null)
            MonitorReport.getCurrentTest().setJiraTicket(tag);
        MonitorReport.addTest(!scenario.isFailed());
    }

    @Override
    public void endOfScenarioLifeCycle(Scenario scenario) {
        logger.debug("###################ENDOFScenarioLife");
    }

    @Override
    public void close() {
        MonitorReport.doReportToJira();
    }

    @Override
    public void result(Result result) {

        if(result != null && !result.getStatus().equalsIgnoreCase("Passed")){
            MonitorReport.getCurrentTest().setResult(result);
        }

    }

    @Override
    public void after(Match match, Result result) {
        //System.out.println("###################After");
        MonitorReport.getCurrentTest().getExecution().addMatchResult(match,result);
    }


    static class MonitorReport{
        private static ZfjReporter reporter = ZapiBuilder.buildZapiReporterWithNewCycleForEachBuild();
        private static ZapiTestCase testCase;
        private static Map<ZapiTestCase,Boolean> testResult = new HashMap<>();
        private static Logger logger = Logger.getLogger(MonitorReport.class);

        synchronized static void addTest(Boolean status){
            testResult.put(testCase,status);
            testCase = null;
        }

        synchronized  static ZapiTestCase getCurrentTest(){
            if(testCase == null){
                testCase = new ZapiTestCase();
            }
            return testCase;
        }

        synchronized static void doReportToJira(){
            logger.info("---------------------Sending report to Jira ---------------------");
            logger.info(String.format("---------------------Tests to send : %d ---------------------",testResult.size()));
            reporter.perform(1,testResult);
            logger.info("--------------------- Done  Sending report to Jira ---------------------");
        }

    }
}



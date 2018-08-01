package com.crowdar.zapi.jenkins.reporter;

import com.crowdar.zapi.collaborator.ZapiBuilder;
import com.crowdar.zapi.model.ZapiStepExecution;
import com.crowdar.zapi.model.ZapiTestCase;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import gherkin.formatter.Formatter;
import gherkin.formatter.Reporter;
import gherkin.formatter.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JiraReporter implements Reporter,Formatter {



    @Override
    public void syntaxError(String s, String s1, List<String> list, String s2, Integer integer) {
        String out = String.format("@@@@@ Syntax Error(): s=%s, s1=%s, list=%s, s2=%s, integer=%s", s, s1, list, s2, integer);
        System.out.println(out);
    }

    @Override
    public void uri(String s) {

    }

    @Override
    public void feature(Feature feature) {
        System.out.println("-------- Report Jira starting for : "+ feature.getName());
    }

    @Override
    public void scenarioOutline(ScenarioOutline scenarioOutline) {

    }

    @Override
    public void examples(Examples examples) {

    }

    @Override
    public void startOfScenarioLifeCycle(Scenario scenario) {


    }

    @Override
    public void background(Background background) {


    }
    @Before
    public void beforeScenario(cucumber.api.Scenario scenario){


    }

    @Override
    public void scenario(Scenario scenario) {



    }

    @Override
    public void step(Step step) {
        System.out.println("###################STEP");
        MonitorReport.getCurrentTest().getExecution().addStepExecution(new ZapiStepExecution(step.getName()));
    }



    @After
    public void getScenarioData(cucumber.api.Scenario scenario){
        System.out.println("###################AFTER CUCUMBER");

        MonitorReport.getCurrentTest().setSummary(scenario.getName());
        String tag = scenario.getSourceTagNames().stream().filter(t -> t.contains("JIRA_")).findFirst().orElse(null);
        if(tag != null)
            MonitorReport.getCurrentTest().setJiraTicket(tag);
        MonitorReport.addTest(!scenario.isFailed());

    }

    @Override
    public void endOfScenarioLifeCycle(Scenario scenario) {
        System.out.println("###################ENDOFScenarioLife");

    }

    @Override
    public void done() {
        System.out.println("###################done");
     }



    @Override
    public void close() {
        System.out.println("###################Close");
        MonitorReport.doReportToJira();

    }

    @Override
    public void eof() {


    }

    @Override
    public void before(Match match, Result result) {



    }

    @Override
    public void result(Result result) {

        if(result != null && !result.getStatus().equalsIgnoreCase("Passed")){
            MonitorReport.getCurrentTest().setResult(result);
        }

    }

    @Override
    public void after(Match match, Result result) {
        System.out.println("###################After");
        MonitorReport.getCurrentTest().getExecution().addMatchResult(match,result);
    }

    @Override
    public void match(Match match) {

    }

    @Override
    public void embedding(String s, byte[] bytes) {

    }

    @Override
    public void write(String s) {

    }

    static class MonitorReport{
        private static ZfjReporter reporter = ZapiBuilder.buildZapiReporterWithNewCycleForEachBuild();
        private static ZapiTestCase testCase;
        private static Map<ZapiTestCase,Boolean> testResult = new HashMap<>();

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
            System.out.println("---------------------Sending report to Jira ---------------------");
            System.out.println(String.format("---------------------Tests to send : %d ---------------------",testResult.size()));
            reporter.perform(1,testResult);
            System.out.println("--------------------- Done  Sending report to Jira ---------------------");
        }

    }
}



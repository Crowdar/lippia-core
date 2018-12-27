package com.crowdar.report;

import com.crowdar.zapi.collaborator.ZapiBuilder;
import com.crowdar.zapi.jenkins.reporter.ZfjReporter;
import com.crowdar.zapi.model.ZapiTestCase;
import cucumber.api.event.*;
import org.apache.log4j.Logger;
import java.util.HashMap;
import java.util.Map;

public class JiraCucumberReport extends CucumberEventListener {
    private Logger logger = Logger.getLogger(JiraCucumberReport.class);

    @Override
    public void handleTestSourceRead(TestSourceRead event) {

    }

    @Override
    public void handleTestCaseStarted(TestCaseStarted event) {

    }

    @Override
    public void handleTestStepStarted(TestStepStarted event) {

    }

    @Override
    public void handleTestStepFinished(TestStepFinished event) {

    }

    @Override
    public void handleEmbed(EmbedEvent event) {

    }

    @Override
    public void handleWrite(WriteEvent event) {

    }

    @Override
    public void finishReport() {

    }

    /*
        public void syntaxError(String s, String s1, List<String> list, String s2, Integer integer) {
            String out = String.format("@@@@@ Syntax Error(): s=%s, s1=%s, list=%s, s2=%s, integer=%s", s, s1, list, s2, integer);
            System.out.println(out);
        }


        public void feature(Feature feature) {
            logger.info("-------- Report Jira starting for : "+ feature.getName());
        }

        @Override
        public void step(Step step) {
           logger.debug(" JiraReport ###################STEP");
            MonitorReport.getCurrentTest().getExecution().addStepExecution(new ZapiStepExecution(step.getName()));
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

    */
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



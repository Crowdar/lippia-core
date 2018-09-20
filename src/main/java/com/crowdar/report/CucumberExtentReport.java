package com.crowdar.report;

import com.crowdar.core.PropertyManager;
import com.crowdar.web.WebDriverManager;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import gherkin.formatter.model.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;


public class CucumberExtentReport extends CucumberReport {
    private static ExtentReports extent;
    private static ExtentTest logger;
    private static String scenarioName;
    private static String scenarioDesc;
    private static Map extentTestMap = new HashMap();
    private static List<String> steps;
    private static List<String> scenarioOutlineExamples;
    private static int stepi;
    private static int examplei;
    private static String featureName;
    private static boolean isScenarioOutline;
    private static String errorMessage;
    private static SimpleDateFormat sdf;
    private static boolean isScenarioOK;


    private static Logger logger4j = Logger.getLogger(CucumberExtentReport.class);

    private static String reportPath = "target".concat(File.separator).concat(PropertyManager.getProperty("crowdar.extent.report.path"));


    private static ExtentReports getExtent(){
        if(extent == null){
            sdf = new SimpleDateFormat("dd-MM-YYYY-hh-mm-SS-SSS");
            extent = new ExtentReports(reportPath.concat(File.separator)
                     .concat(PropertyManager.getProperty("crowdar.extent.report.name"))
                     .concat(".html"), true);
            extent.addSystemInfo("Host Name", PropertyManager.getProperty("crowdar.extent.host.name"))
                  .addSystemInfo("Environment", PropertyManager.getProperty("crowdar.extent.environment"))
                  .addSystemInfo("User Name", PropertyManager.getProperty("crowdar.extent.user.name"));
            extent.loadConfig(new File(System.getProperty("user.dir").concat(File.separator).concat("extent-config.xml")));

        }
        return extent;
    }

    private static List<String> getSteps(){
        if(steps == null){
            steps = new ArrayList<String>();
        }
        return steps;
    }

    public synchronized static void closeReporter() {
        if(extent!=null){
            extent.flush();
        }
    }

    public void eof() {
        if(extent!=null){
            extent.close();
        }
    }


    public static synchronized ExtentTest getTest() {
        return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
    }

    public static synchronized void endTest() {
        getExtent().endTest((ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId())));
    }

    public static synchronized ExtentTest startTest(String testName) {
        return startTest(testName, "");
    }

    public static synchronized ExtentTest startTest(String testName, String desc) {
        ExtentTest test = getExtent().startTest(testName, desc);
        extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);

        return test;
    }


    public void done(){
        closeReporter();
    }

    public void startOfScenarioLifeCycle(Scenario scenario) {
        try {
            initiate(scenario);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void endOfScenarioLifeCycle(Scenario scenario) {
        try {
            terminate(scenario);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

    }





    /**
     * Invoked before every cucumber scenario or scenario outline example run
     * Gets feature name followed by scenario name and creates a new test in extent reports
     * If it is a scenario outline, it will also append the Example key
     * It will also assign it to a category which is the feature name
     * Another improvement is to use the ExtentReports assignChild for scenario outline example runs (not yet implemented)
     * @param scenario
     * @throws IOException
     * @throws InterruptedException
     */

    public static void initiate(Scenario scenario) throws IOException, InterruptedException {
        isScenarioOK = true;
        logger4j.info("SCENARIO ID AND NAME:" + scenario.getName());
        StringBuilder tags = new StringBuilder();
        tags.append("-");
        scenario.getTags().stream().forEach(t-> tags.append(t.getName()).append("-"));
        logger4j.info("TAGS: "+tags.toString());

        if (isScenarioOutline) {
            System.out.println(examplei);
            scenarioName = featureName + " : " + scenario.getName()+ " : Example #" + examplei;
            scenarioDesc = "Tags: " + tags.toString() + "\n Example Data: " + scenarioOutlineExamples.get(examplei - 1);
            examplei++;
            if (examplei == scenarioOutlineExamples.size() + 1) {
                isScenarioOutline = false;
            }
        } else {
            scenarioName = featureName + " : " + scenario.getName().toString();
            scenarioDesc = "Tags: " +tags.toString();
        }

        logger = startTest(scenarioName, scenarioDesc);
        logger.assignCategory("Feature: " + featureName);
    }

    /**
     * Invoked after the completion of a Cucumber scenario
     * Resets static members where required
     * Also ends the test in the report
     * @param scenario
     * @throws Throwable
     */

    public static void terminate(Scenario scenario) throws Throwable {
        if(PropertyManager.getProperty("crowdar.extent.report.name")== null || PropertyManager.getProperty("crowdar.extent.report.name").isEmpty() ){
            return;
        }
        String status = isScenarioOK ? "Success" : "Failure";
        logger4j.info("SCENARIO STATUS: " + status);

        //Reset step count and clear org.stag.runner.steps list
        steps.clear();
        stepi = 0;
        isScenarioOK = true;

        //End the logger test for each scenario
        endTest();
    }

    /**
     * This is the method that is automatically invoked after EVERY step in a scenario, not the step method
     * Because of this, and we only have access to the result parameter
     *
     * @param result
     */

    public void result(Result result) {
        String currentStep = getSteps().get(stepi);
        logger4j.info("AFTER EVERY STEP RESULT: " + result.getStatus() + "||" + currentStep + "||" + stepi);
        stepi++; //Increase step count
        errorMessage = "<b>Error message: </b>" + result.getError() + " <b> | Stack Trace: </b>" + result.getErrorMessage(); //Get failed step error message
        logger = getTest();
        if (result.getStatus().equalsIgnoreCase("failed")) {
            screenshotTake(currentStep,LogStatus.FAIL);
            getTest().log(LogStatus.FAIL, currentStep, result.getError());
            isScenarioOK = false;
        } else if (result.getStatus().equalsIgnoreCase("skipped")) {
            getTest().log(LogStatus.SKIP, currentStep, "Step skipped due to previous step failing");
        } else { //Step passed at this point
            screenshoTakeOnSuccess(currentStep);
            getTest().log(LogStatus.PASS, currentStep, "Description");
        }
    }

    public void feature(Feature feature) {
        featureName = feature.getName();
    }

    /**
     * Invoked if the scenario is a scenario outline
     * sets the scenario outline boolean member to true and the index to start at 1 to ignore the row title
     * @param scenarioOutline
     */

    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        logger4j.info("SCENARIO OUTLINE: " + scenarioOutline.getName());
        isScenarioOutline = true;
        examplei = 1;
    }

    /**
     * Create new examples array and assigns the scenario outline examples to it
     * This is then iterated through based on how many times the scenario is run
     * @param examples
     */

    public void examples(Examples examples) {
        scenarioOutlineExamples = new ArrayList<>();
        logger4j.info("EXAMPLES: " + examples.getName());
        List<ExamplesTableRow> er = examples.getRows();
        Iterator<ExamplesTableRow> examplesIterator = er.iterator();
        examplesIterator.next();
        while (examplesIterator.hasNext()) {
            String examplesDataTableLine = examplesIterator.next().getCells().toString();
            logger4j.info(examplesDataTableLine);
            scenarioOutlineExamples.add(examplesDataTableLine);
        }
    }

    /**
     * Unfortunately, this step is actually invoked at the start of the scenario by the gherkin parser
     * It lists all the steps out in 1 go before the scenario is actually executed
     * Therefore we store it into a static step member and iterate through it every time the result method is invoked
     * @param step
     */

    public void step(Step step) {
        logger4j.info("STEPS IN SCENARIO: " + step.getKeyword() + step.getName());
        String stepParams = "";
        List<DataTableRow> rows = step.getRows();
        if (rows != null) {
            Iterator<DataTableRow> rowsIterator = rows.iterator();
            while (rowsIterator.hasNext()) {
                String stepDataTableLine = rowsIterator.next().getCells().toString();
                logger4j.info(stepDataTableLine);
                stepParams = stepParams + stepDataTableLine;
            }
        }
        logger4j.info(step.getKeyword() + step.getName() + " " + stepParams);
        getSteps().add(step.getKeyword() + step.getName() + " " + stepParams);
    }

    private void screenshotTake(String stepName , LogStatus logStatus){
        String fileLocation = "";
        String name = "";
        final File screenshot = ((TakesScreenshot) WebDriverManager.getDriverInstance()).getScreenshotAs(OutputType.FILE);
         name = "screenshot".concat(Long.toString(System.currentTimeMillis()));
         fileLocation = reportPath.concat(File.separator).concat(name);
        try {
          FileUtils.copyFile(screenshot, new File(fileLocation));
        } catch (IOException e) {
            e.printStackTrace();
        }
        getTest().log(logStatus, logger.addScreenCapture(name));
    }

    private  void screenshoTakeOnSuccess(String stepName){
        String screenShotOnSuccessStep = PropertyManager.getProperty("crowdar.extent.screenShotOnSuccessStep");
        if(screenShotOnSuccessStep != null && !screenShotOnSuccessStep.isEmpty()){
            screenshotTake(stepName,LogStatus.PASS);
        }
    }



}

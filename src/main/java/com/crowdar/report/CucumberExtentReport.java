package com.crowdar.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.crowdar.core.PropertyManager;
import com.crowdar.web.WebDriverManager;
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
    private static ExtentHtmlReporter htmlReporter;
    private static ThreadLocal<ExtentTest> featureTestThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenarioOutlineThreadLocal = new InheritableThreadLocal<>();
    static ThreadLocal<ExtentTest> scenarioThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<LinkedList<Step>> stepListThreadLocal = new InheritableThreadLocal<>();
    static ThreadLocal<ExtentTest> stepTestThreadLocal = new InheritableThreadLocal<>();
    private static boolean scenarioOutlineFlag;


    private static Logger logger4j = Logger.getLogger(CucumberExtentReport.class);

    private static String reportPath = "target".concat(File.separator).concat(PropertyManager.getProperty("crowdar.extent.report.path"));

    private static ExtentHtmlReporter getExtentHtmlReport() {
        if(htmlReporter != null){
            return htmlReporter;
        }

        File file = new File(reportPath.concat(File.separator)
                    .concat(PropertyManager.getProperty("crowdar.extent.report.name"))
                    .concat(".html"));
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        htmlReporter = new ExtentHtmlReporter(file);
        logger4j.info("#####Extent xml configuration : "+ System.getProperty("user.dir").concat(File.separator).concat("extent-config.xml"));
        htmlReporter.loadConfig(System.getProperty("user.dir").concat(File.separator).concat("extent-config.xml"));
        stepListThreadLocal.set(new LinkedList<>());
        scenarioOutlineFlag = false;
       return  htmlReporter;
    }

    private static void htmlReporSetup(){
        htmlReporter.config().setChartVisibilityOnOpen(true);
        // report title
        if(PropertyManager.isPropertyPresentAndNotEmpty("crowdar.extent.report.document.title")){
            htmlReporter.config().setDocumentTitle(PropertyManager.getProperty("crowdar.extent.report.document.title"));
        }
        // encoding, default = UTF-8
        if(PropertyManager.isPropertyPresentAndNotEmpty("crowdar.extent.report.encoding")){
            htmlReporter.config().setEncoding(PropertyManager.getProperty("crowdar.extent.report.encoding"));
        }
        // protocol (http, https)
        if(PropertyManager.isPropertyPresentAndNotEmpty("crowdar.extent.report.protocol")){
            htmlReporter.config().setProtocol(Protocol.valueOf(PropertyManager.getProperty("crowdar.extent.report.protocol")));
        }
        // report or build name
        if(PropertyManager.isPropertyPresentAndNotEmpty("crowdar.extent.report.name")){
            htmlReporter.config().setReportName(PropertyManager.getProperty("crowdar.extent.report.name"));
        }
        // chart location - top, bottom
        if(PropertyManager.isPropertyPresentAndNotEmpty("crowdar.extent.report.chart.location")) {
            htmlReporter.config().setTestViewChartLocation(ChartLocation.valueOf(PropertyManager.getProperty("crowdar.extent.report.chart.location")));
        }
        // theme - standard, dark
        if(PropertyManager.isPropertyPresentAndNotEmpty("crowdar.extent.report.theme")) {
            htmlReporter.config().setTheme(Theme.valueOf(PropertyManager.getProperty("crowdar.extent.report.theme").toUpperCase()));
        }

        // set timeStamp format
        if(PropertyManager.isPropertyPresentAndNotEmpty("crowdar.extent.report.timestampformat")) {
            htmlReporter.config().setTimeStampFormat(PropertyManager.getProperty("crowdar.extent.report.timestampformat"));
        }
        // add custom css
        htmlReporter.config().setCSS("css-string");
        // add custom javascript
        htmlReporter.config().setJS("js-string");

    }

    private static ExtentReports getExtentReports(){
        if(extent == null){
            extent = new ExtentReports();
            extent.setSystemInfo("Host Name", PropertyManager.getProperty("crowdar.extent.host.name"));
            extent.setSystemInfo("Environment", PropertyManager.getProperty("crowdar.extent.environment"));
            extent.setSystemInfo("User Name", PropertyManager.getProperty("crowdar.extent.user.name"));
            extent.attachReporter(getExtentHtmlReport());
            //(htmlReporter.loadConfig(System.getProperty("user.dir").concat(File.separator).concat("extent-config.xml"));
            htmlReporSetup();
        }

        return extent;
    }



    public void syntaxError(String state, String event, List<String> legalEvents, String uri,
                            Integer line) {

    }

    public void uri(String uri) {

    }

    public void feature(Feature feature) {
        featureTestThreadLocal.set(getExtentReports().createTest(com.aventstack.extentreports.gherkin.model.Feature.class, feature.getName()));
        ExtentTest test = featureTestThreadLocal.get();

        for (Tag tag : feature.getTags()) {
            test.assignCategory(tag.getName());
        }
    }

    public void scenarioOutline(ScenarioOutline scenarioOutline) {
        scenarioOutlineFlag = true;
        ExtentTest node = featureTestThreadLocal.get()
                .createNode(com.aventstack.extentreports.gherkin.model.ScenarioOutline.class, scenarioOutline.getName());
        scenarioOutlineThreadLocal.set(node);
    }

    public void examples(Examples examples) {
        ExtentTest test = scenarioOutlineThreadLocal.get();

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

        ExtentTest scenarioNode;
        if (scenarioOutlineThreadLocal.get() != null && scenario.getKeyword().trim()
                .equalsIgnoreCase("Scenario Outline")) {
            scenarioNode =
            scenarioOutlineThreadLocal.get().createNode(com.aventstack.extentreports.gherkin.model.Scenario.class, scenario.getName());
        } else {
            scenarioNode =
                    featureTestThreadLocal.get().createNode(com.aventstack.extentreports.gherkin.model.Scenario.class, scenario.getName());
        }

        for (Tag tag : scenario.getTags()) {
            scenarioNode.assignCategory(tag.getName());
        }
        scenarioThreadLocal.set(scenarioNode);
    }

    public void background(Background background) {

    }

    public void scenario(Scenario scenario) {

    }

    public void step(Step step) {
        if (scenarioOutlineFlag) {
            return;
        }
        stepListThreadLocal.get().add(step);
    }

    public void endOfScenarioLifeCycle(Scenario scenario) {

    }

    public void done() {
        getExtentReports().flush();
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
            stepTestThreadLocal.get().pass(Result.PASSED);

            String screenShotOnSuccessStep = PropertyManager.getProperty("crowdar.extent.screenshotOnSuccess");
            if(screenShotOnSuccessStep != null && !screenShotOnSuccessStep.isEmpty()){
                try {
                    stepTestThreadLocal.get().addScreenCaptureFromPath(takeScreeshot());
                }catch (IOException e){
                    logger4j.error(e.getStackTrace());
                }
            }

        } else if (Result.FAILED.equals(result.getStatus())) {
            stepTestThreadLocal.get().fail(result.getError());
            try {
                stepTestThreadLocal.get().addScreenCaptureFromPath(takeScreeshot());
            }catch (IOException e){
                logger4j.error(e.getStackTrace());
            }
        } else if (Result.SKIPPED.equals(result)) {
            stepTestThreadLocal.get().skip(Result.SKIPPED.getStatus());
        } else if (Result.UNDEFINED.equals(result)) {
            stepTestThreadLocal.get().skip(Result.UNDEFINED.getStatus());
        }
    }


    public void match(Match match) {
        Step step = stepListThreadLocal.get().poll();
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

        ExtentTest scenarioTest = scenarioThreadLocal.get();
        ExtentTest stepTest = null;

        try {
            stepTest = scenarioTest.createNode(new GherkinKeyword(step.getKeyword()), step.getKeyword() + step.getName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (data != null) {
            Markup table = MarkupHelper.createTable(data);
            stepTest.info(table);
        }

        stepTestThreadLocal.set(stepTest);
    }

    private String takeScreeshot()throws IOException{
        String fileLocation = "";
        String name = "";
        final File screenshot = ((TakesScreenshot) WebDriverManager.getDriverInstance()).getScreenshotAs(OutputType.FILE);
        name = "screenshot".concat(Long.toString(System.currentTimeMillis()));
        fileLocation = reportPath.concat(File.separator).concat(name);
        FileUtils.copyFile(screenshot, new File(fileLocation));
        return name;
    }

}

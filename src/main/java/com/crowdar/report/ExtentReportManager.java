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
import gherkin.formatter.model.Result;
import gherkin.formatter.model.Step;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static ExtentHtmlReporter htmlReporter;
    private static ThreadLocal<ExtentTest> featureTestThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenarioOutlineThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenarioThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<LinkedList<Step>> stepListThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> stepTestThreadLocal = new InheritableThreadLocal<>();

    private static Logger logger4j = Logger.getLogger(ExtentReportManager.class);

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

    private static ExtentReports getExtent(){
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

    public static void flush(){
        getExtent().flush();
    }

    public static ExtentTest createTest(String storyFeatureName){
        featureTestThreadLocal.set(getExtent().createTest(com.aventstack.extentreports.gherkin.model.Feature.class, storyFeatureName));
        return featureTestThreadLocal.get();
    }

    public static void createScenarioOutlineNode(String scenarioName){
        ExtentTest node = featureTestThreadLocal.get()
                .createNode(com.aventstack.extentreports.gherkin.model.ScenarioOutline.class, scenarioName);
        scenarioOutlineThreadLocal.set(node);
    }

    public static ExtentTest getOutlineScenarioNode(){
        return scenarioOutlineThreadLocal.get();
    }



    public static  ExtentTest createScenarioOutlineOrStandard(String scenarioName , String keyword){
        ExtentTest scenarioNode;
        if (scenarioOutlineThreadLocal.get() != null && keyword.trim()
                .equalsIgnoreCase("Scenario Outline")) {
            scenarioNode =
                    scenarioOutlineThreadLocal.get().createNode(com.aventstack.extentreports.gherkin.model.Scenario.class,scenarioName);
        } else {
            scenarioNode =
                    featureTestThreadLocal.get().createNode(com.aventstack.extentreports.gherkin.model.Scenario.class,scenarioName);
        }
        scenarioThreadLocal.set(scenarioNode);

        return scenarioNode;
    }

    public static void addCucumberStep(Step step){
        stepListThreadLocal.get().add(step);
    }

    public static void addCucumberPassStep(){
        stepTestThreadLocal.get().pass(Result.PASSED);
        String screenShotOnSuccessStep = PropertyManager.getProperty("crowdar.extent.screenshotOnSuccess");
        if(screenShotOnSuccessStep != null && !screenShotOnSuccessStep.isEmpty()){
            try {
                stepTestThreadLocal.get().addScreenCaptureFromPath(takeScreeshot());
            }catch (IOException e){
                logger4j.error(e.getStackTrace());
            }
        }
    }

    public static void addCucumberFailStep(Throwable error){
        stepTestThreadLocal.get().fail(error);
        try {
            stepTestThreadLocal.get().addScreenCaptureFromPath(takeScreeshot());
        }catch (IOException e){
            logger4j.error(e.getStackTrace());
        }
    }

    public static void addCucumberSkipStep(){
        stepTestThreadLocal.get().skip(Result.SKIPPED.getStatus());
    }

    public static Step pollCucumberStep(){
        return stepListThreadLocal.get().poll();
    }

    public static void addCucumberUndefinedStep(){
        stepTestThreadLocal.get().skip(Result.UNDEFINED.getStatus());
    }

    public static void matchCucumberStep(String data[][] , Step step){
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

    private static String takeScreeshot()throws IOException{
        String fileLocation = "";
        String name = "";
        final File screenshot = ((TakesScreenshot) WebDriverManager.getDriverInstance()).getScreenshotAs(OutputType.FILE);
        name = "screenshot".concat(Long.toString(System.currentTimeMillis()));
        fileLocation = reportPath.concat(File.separator).concat(name);
        FileUtils.copyFile(screenshot, new File(fileLocation));
        return name;
    }




}

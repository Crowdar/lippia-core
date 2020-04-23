package com.crowdar.report;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.gherkin.model.Feature;
import com.aventstack.extentreports.gherkin.model.Scenario;
import com.aventstack.extentreports.gherkin.model.ScenarioOutline;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.Protocol;
import com.aventstack.extentreports.reporter.configuration.Theme;
import com.crowdar.core.PropertyManager;
import com.crowdar.driver.DriverManager;
import cucumber.api.PickleStepTestStep;
import cucumber.api.Result;
import gherkin.ast.Examples;
import gherkin.ast.TableCell;
import gherkin.ast.TableRow;
import gherkin.ast.Tag;
import gherkin.pickles.PickleTag;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ExtentReportManager {

    private static ExtentReports extent;
    private static ExtentHtmlReporter htmlReporter;
    private static ThreadLocal<ExtentTest> featureTest = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenarioOutline = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenario = new InheritableThreadLocal<>();
    private static ThreadLocal<LinkedList<PickleStepTestStep>> stepList = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> stepTest = new InheritableThreadLocal<>();
    private static Map<String, ExtentTest> mapFeature = new HashMap<>();

    private static Logger logger4j = Logger.getLogger(ExtentReportManager.class);
    private static String reportPath;
    private static String reportName = "CrowdarReport";
    private static final String BASE_REPORT_PATH = "target";

    private static void setReportPath(String newReportPath) {
        reportPath = newReportPath;
    }

    private static String getReportPath() {
        return reportPath;
    }

    private static ExtentHtmlReporter getExtentHtmlReport() {
        if (htmlReporter != null) {
            return htmlReporter;
        }
        if (!StringUtils.isEmpty(PropertyManager.getProperty("crowdar.extent.report.path"))) {
            setReportPath(BASE_REPORT_PATH.concat(File.separator).concat(PropertyManager.getProperty("crowdar.extent.report.path")));
        } else {
            setReportPath(BASE_REPORT_PATH);
        }
        if (!StringUtils.isEmpty(PropertyManager.getProperty("crowdar.extent.report.name"))) {
            setReportName(PropertyManager.getProperty("crowdar.extent.report.name"));
        }
        File file = new File(getReportPath().concat(File.separator)
                .concat(getReportName())
                .concat(".html"));
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        htmlReporter = new ExtentHtmlReporter(file);
        logger4j.info("#####Extent xml configuration : " + System.getProperty("user.dir").concat(File.separator).concat("extent-config.xml"));
        htmlReporter.loadConfig(System.getProperty("user.dir").concat(File.separator).concat("extent-config.xml"));
        return htmlReporter;
    }

    private static void htmlReporSetup() {
        // htmlReporter.config().setChartVisibilityOnOpen(true);
        // report title
        if (!StringUtils.isEmpty(PropertyManager.getProperty("crowdar.extent.report.document.title"))) {
            htmlReporter.config().setDocumentTitle(PropertyManager.getProperty("crowdar.extent.report.document.title"));
        }
        // encoding, default = UTF-8
        if (!StringUtils.isEmpty(PropertyManager.getProperty("crowdar.extent.report.encoding"))) {
            htmlReporter.config().setEncoding(PropertyManager.getProperty("crowdar.extent.report.encoding"));
        }
        // protocol (http, https)
        if (!StringUtils.isEmpty(PropertyManager.getProperty("crowdar.extent.report.protocol"))) {
            htmlReporter.config().setProtocol(Protocol.valueOf(PropertyManager.getProperty("crowdar.extent.report.protocol")));
        }
        // report or build name
        if (!StringUtils.isEmpty(PropertyManager.getProperty("crowdar.extent.report.name"))) {
            htmlReporter.config().setReportName(PropertyManager.getProperty("crowdar.extent.report.name"));
        }else{
            htmlReporter.config().setReportName(getReportName());
        }
        // chart location - top, bottom
        /*if(!StringUtils.isEmpty(PropertyManager.getProperty("crowdar.extent.report.chart.location"))) {
            htmlReporter.config().setTestViewChartLocation(ChartLocation.valueOf(PropertyManager.getProperty("crowdar.extent.report.chart.location")));
        }*/

        // theme - standard, dark
        if (!StringUtils.isEmpty(PropertyManager.getProperty("crowdar.extent.report.theme"))) {
            htmlReporter.config().setTheme(Theme.valueOf(PropertyManager.getProperty("crowdar.extent.report.theme").toUpperCase()));
        }

        // set timeStamp format
        if (!StringUtils.isEmpty(PropertyManager.getProperty("crowdar.extent.report.timestampformat"))) {
            htmlReporter.config().setTimeStampFormat(PropertyManager.getProperty("crowdar.extent.report.timestampformat"));
        }
        // add custom css
        htmlReporter.config().setCSS("css-string");
        // add custom javascript
        htmlReporter.config().setJS("js-string");
        //setting UTF8 encoding
        htmlReporter.config().setEncoding("UTF-8");
        //appending existing report
        logger4j.info("--------EXTENTREPORT   Setting Append Existing to true -------------------------------");
        /*htmlReporter.setAppendExisting(true);*/

    }

    private static synchronized ExtentReports getExtent() {
        if (extent == null) {
            logger4j.info("------ creating extent report infor --------------------------------");
            extent = new ExtentReports();
            extent.setSystemInfo("Host Name", PropertyManager.getProperty("crowdar.extent.host.name"));
            extent.setSystemInfo("Environment", PropertyManager.getProperty("crowdar.extent.environment"));
            extent.setSystemInfo("User Name", PropertyManager.getProperty("crowdar.extent.user.name"));
            extent.attachReporter(getExtentHtmlReport());
            htmlReporSetup();
        }


        return extent;
    }

    private static synchronized ExtentTest getFeature(String featurName, List<Tag> tags) {
        if (mapFeature.containsKey(featurName)) {
            featureTest.set(mapFeature.get(featurName));
            return mapFeature.get(featurName);
        }
        ExtentTest feature = getExtent().createTest(Feature.class, featurName);
        featureTest.set(feature);
        for (Tag tag : tags) {
            featureTest.get().assignCategory(tag.getName());
        }
        mapFeature.put(featurName, feature);
        return feature;
    }

    public static void flush() {
        logger4j.info("------flushing Thread : " + Thread.currentThread().getId());
        getExtent().flush();
    }


    public static void createFeature(String storyFeatureName, List<Tag> tags) {
        logger4j.info("------Creating Test Feature with Name : " + storyFeatureName + " " + Thread.currentThread().getId());
        getFeature(storyFeatureName, tags);
    }

    public static void createScenarioOutlineNode(String scenarioName) {
        logger4j.info("------Creating Scenario Outline Node : " + Thread.currentThread().getId());
        //if(firstFeature.get() != null && firstFeature.get()){
        ExtentTest node = featureTest.get()
                .createNode(ScenarioOutline.class, scenarioName);
        scenarioOutline.set(node);
        //}
    }

    public static void createExampleCucumber(Examples examples) {
        logger4j.info("------Creating Scenario example cucumber : " + Thread.currentThread().getId());
        ExtentTest test = scenarioOutline.get();

        String[][] data = null;
        List<TableRow> rows = examples.getTableBody();
        int rowSize = rows.size();
        for (int i = 0; i < rowSize; i++) {
            TableRow examplesTableRow = rows.get(i);
            List<TableCell> cells = examplesTableRow.getCells();
            int cellSize = cells.size();
            if (data == null) {
                data = new String[rowSize][cellSize];
            }
            for (int j = 0; j < cellSize; j++) {
                data[i][j] = cells.get(j).getValue();
            }
        }
        test.info(MarkupHelper.createTable(data));

    }


    public static void createResult(Result result) {
        logger4j.info("------Creating Result cucumber : " + Thread.currentThread().getId());
        if (Result.Type.PASSED.equals(result.getStatus())) {
            ExtentReportManager.addCucumberPassStep();
        } else if (Result.Type.FAILED.equals(result.getStatus())) {
            ExtentReportManager.addCucumberFailStep(result.getError());
        } else if (Result.Type.SKIPPED.equals(result.getStatus())) {
            ExtentReportManager.addCucumberSkipStep();
        } else if (Result.UNDEFINED.equals(result.getStatus())) {
            ExtentReportManager.addCucumberUndefinedStep();
        }
    }


    public static void createScenarioOutlineOrStandard(String scenarioName, String keyword, List<PickleTag> tags) {
        logger4j.info("------ Create Scenario Outline Or Standard : " + Thread.currentThread().getId());
        ExtentTest scenarioNode;
        if (scenarioOutline.get() != null && keyword.trim()
                .equalsIgnoreCase("Scenario Outline")) {
            scenarioNode =
                    scenarioOutline.get().createNode(Scenario.class, scenarioName);
        } else {
            scenarioNode =
                    featureTest.get().createNode(Scenario.class, scenarioName);
        }
        scenario.set(scenarioNode);

        for (PickleTag tag : tags) {
            scenarioNode.assignCategory(tag.getName());
        }
    }

    public static void addCucumberStep(PickleStepTestStep step) {

        stepList().get().add(step);
    }

    private static ThreadLocal<LinkedList<PickleStepTestStep>> stepList() {

        if (stepList.get() == null) {
            stepList.set(new LinkedList<>());
        }
        return stepList;

    }

    public static void addCucumberPassStep() {
        stepTest.get().pass(Result.Type.PASSED.toString());

        Boolean screenShotOnSuccessStep = new Boolean(PropertyManager.getProperty("crowdar.report.screenshotOnSuccess"));
        if (screenShotOnSuccessStep) {
            try {
                stepTest.get().addScreenCaptureFromPath(takeScreenshot());
            } catch (IOException e) {
                logger4j.error(e.getStackTrace());
            }
        }
    }

    public static void addCucumberFailStep(Throwable error) {

        Boolean stackTrace = new Boolean(PropertyManager.getProperty("crowdar.report.stackTraceDetail"));
        if (stackTrace) {
            stepTest.get().fail(error);
        } else {
            stepTest.get().fail(error.getMessage());
        }

        Boolean screenshotDisable = new Boolean(PropertyManager.getProperty("crowdar.report.disable_screenshot_on_failure"));
        if (!screenshotDisable) {
            try {
                stepTest.get().addScreenCaptureFromPath(takeScreenshot());
            } catch (IOException e) {
                logger4j.error(e.getStackTrace());
            }
        }

    }

    private static void addCucumberSkipStep() {
        stepTest.get().skip(Result.Type.SKIPPED.toString());
    }

    public static PickleStepTestStep pollCucumberStep() {
        return stepList().get().poll();
    }

    private static void addCucumberUndefinedStep() {
        stepTest.get().skip(Result.Type.UNDEFINED.toString());
    }

    public static void matchCucumberStep(String keyword, String name) {
        ExtentTest scenarioTest = scenario.get();
        ExtentTest stepTest = null;
        try {
            stepTest = scenarioTest.createNode(new GherkinKeyword(keyword), name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        String data[][] = null;


        if (data != null) {
            Markup table = MarkupHelper.createTable(data);
            stepTest.info(table);
        }

        ExtentReportManager.stepTest.set(stepTest);
    }

    private static String takeScreenshot() throws IOException {
        String fileLocation = "";
        String name = "";
        final File screenshot = ((TakesScreenshot) DriverManager.getDriverInstance()).getScreenshotAs(OutputType.FILE);
        name = "screenshot".concat(Long.toString(System.currentTimeMillis()));
        fileLocation = getReportPath().concat(File.separator).concat(name);
        FileUtils.copyFile(screenshot, new File(fileLocation));
        return name;
    }

    public static String getReportName() {
        return reportName;
    }

    public static void setReportName(String reportName) {
        ExtentReportManager.reportName = reportName;
    }
}

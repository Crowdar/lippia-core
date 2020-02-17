package com.crowdar.report;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.GherkinKeyword;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.TestSourcesModel;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.service.ExtentService;
import com.crowdar.core.PropertyManager;
import com.crowdar.driver.DriverManager;
import cucumber.api.HookTestStep;
import cucumber.api.PickleStepTestStep;
import cucumber.api.Result;
import cucumber.api.TestCase;
import cucumber.api.event.*;
import cucumber.runtime.CucumberException;
import cucumber.runtime.io.URLOutputStream;
import gherkin.ast.*;
import gherkin.pickles.*;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class CucumberKlovExtentReport implements ConcurrentEventListener {

    private static Logger logger4j = Logger.getLogger(CucumberKlovExtentReport.class);

    private static final String SCREENSHOT_DIR_PROPERTY = "screenshot.dir";
    private static String reportPath = "target".concat(File.separator).concat(PropertyManager.getProperty("crowdar.extent.report.path"));

    private static Map<String, ExtentTest> featureMap = new ConcurrentHashMap<>();
    private static ThreadLocal<ExtentTest> featureTestThreadLocal = new InheritableThreadLocal<>();
    private static Map<String, ExtentTest> scenarioOutlineMap = new ConcurrentHashMap<>();
    private static ThreadLocal<ExtentTest> scenarioOutlineThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> scenarioThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<Boolean> isHookThreadLocal = new InheritableThreadLocal<>();
    private static ThreadLocal<ExtentTest> stepTestThreadLocal = new InheritableThreadLocal<>();

    @SuppressWarnings("serial")
    private static final Map<String, String> MIME_TYPES_EXTENSIONS = new HashMap<String, String>() {
        {
            put("image/bmp", "bmp");
            put("image/gif", "gif");
            put("image/jpeg", "jpg");
            put("image/png", "png");
            put("image/svg+xml", "svg");
            put("video/ogg", "ogg");
        }
    };

    private static final AtomicInteger EMBEDDED_INT = new AtomicInteger(0);

    private final TestSourcesModel testSources = new TestSourcesModel();

    private ThreadLocal<String> currentFeatureFile = new ThreadLocal<>();
    private ThreadLocal<ScenarioOutline> currentScenarioOutline = new InheritableThreadLocal<>();
    private ThreadLocal<Examples> currentExamples = new InheritableThreadLocal<>();

    private EventHandler<TestSourceRead> testSourceReadHandler = new EventHandler<TestSourceRead>() {
        @Override
        public void receive(TestSourceRead event) {
            handleTestSourceRead(event);
        }
    };
    private EventHandler<TestCaseStarted> caseStartedHandler = new EventHandler<TestCaseStarted>() {
        @Override
        public void receive(TestCaseStarted event) {
            handleTestCaseStarted(event);
        }
    };
    private EventHandler<TestStepStarted> stepStartedHandler = new EventHandler<TestStepStarted>() {
        @Override
        public void receive(TestStepStarted event) {
            handleTestStepStarted(event);
        }
    };
    private EventHandler<TestStepFinished> stepFinishedHandler = new EventHandler<TestStepFinished>() {
        @Override
        public void receive(TestStepFinished event) {
            handleTestStepFinished(event);
        }
    };
    private EventHandler<EmbedEvent> embedEventhandler = new EventHandler<EmbedEvent>() {
        @Override
        public void receive(EmbedEvent event) {
            handleEmbed(event);
        }
    };
    private EventHandler<WriteEvent> writeEventhandler = new EventHandler<WriteEvent>() {
        @Override
        public void receive(WriteEvent event) {
            handleWrite(event);
        }
    };
    private EventHandler<TestRunFinished> runFinishedHandler = new EventHandler<TestRunFinished>() {
        @Override
        public void receive(TestRunFinished event) {
            finishReport();
        }
    };

    public CucumberKlovExtentReport(String arg) {
    }

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestSourceRead.class, testSourceReadHandler);
        publisher.registerHandlerFor(TestCaseStarted.class, caseStartedHandler);
        publisher.registerHandlerFor(TestStepStarted.class, stepStartedHandler);
        publisher.registerHandlerFor(TestStepFinished.class, stepFinishedHandler);
        publisher.registerHandlerFor(EmbedEvent.class, embedEventhandler);
        publisher.registerHandlerFor(WriteEvent.class, writeEventhandler);
        publisher.registerHandlerFor(TestRunFinished.class, runFinishedHandler);
    }

    private void handleTestSourceRead(TestSourceRead event) {
        testSources.addTestSourceReadEvent(event.uri, event);
    }

    private synchronized void handleTestCaseStarted(TestCaseStarted event) {
        handleStartOfFeature(event.testCase);
        handleScenarioOutline(event.testCase);
        createTestCase(event.testCase);
        if (testSources.hasBackground(currentFeatureFile.get(), event.testCase.getLine())) {
            // background
        }
    }

    private synchronized void handleTestStepStarted(TestStepStarted event) {
        isHookThreadLocal.set(false);

        if (event.testStep instanceof HookTestStep) {

//			CrowdarMod - in hook case, not create testNode
//			ExtentTest t = scenarioThreadLocal.get().createNode(Asterisk.class, event.testStep.getCodeLocation());
//			stepTestThreadLocal.set(t);

            isHookThreadLocal.set(true);
        }

        if (event.testStep instanceof PickleStepTestStep) {
            PickleStepTestStep testStep = (PickleStepTestStep) event.testStep;
            createTestStep(testStep);
        }
    }

    private synchronized void handleTestStepFinished(TestStepFinished event) {

//		CrowdarMod - only pickleSteps
        if (event.testStep instanceof PickleStepTestStep) {
            updateResult(event.result);
        }
    }

    public synchronized void updateResult(Result result) {
        switch (result.getStatus().lowerCaseName()) {
            case "failed":
                Boolean screenshotDisable = new Boolean(PropertyManager.getProperty("crowdar.report.disable_screenshot_on_failure"));
                if (screenshotDisable.equals(Boolean.FALSE)) {
                    try {
                        stepTestThreadLocal.get().log(Status.FAIL, result.getErrorMessage(), MediaEntityBuilder.createScreenCaptureFromPath(reportPath + File.separator + takeScreenshot()).build());
                    } catch (IOException e) {
                        logger4j.error(e.getStackTrace());
                    }
                } else {
                    stepTestThreadLocal.get().log(Status.FAIL, result.getErrorMessage());
                }
                break;
            case "skipped":
            case "pending":
                Boolean currentEndingEventSkipped = stepTestThreadLocal.get().getModel().getLogContext() != null
                        && !stepTestThreadLocal.get().getModel().getLogContext().isEmpty()
                        ? stepTestThreadLocal.get().getModel().getLogContext().getLast().getStatus() == Status.SKIP
                        : false;
                if (result.getError() != null) {
                    stepTestThreadLocal.get().skip(result.getError());
                } else if (!currentEndingEventSkipped) {
                    String details = result.getErrorMessage() == null ? "Step skipped" : result.getErrorMessage();
                    stepTestThreadLocal.get().skip(details);
                }
                break;
            case "passed":
                if (stepTestThreadLocal.get() != null && stepTestThreadLocal.get().getModel().getLogContext().isEmpty()) {
                    stepTestThreadLocal.get().pass("");
                }
                break;
            default:
                break;
        }
    }

    private synchronized void handleEmbed(EmbedEvent event) {
        String mimeType = event.mimeType;
        String extension = MIME_TYPES_EXTENSIONS.get(mimeType);
        if (extension != null) {
            StringBuilder fileName = new StringBuilder("embedded").append(EMBEDDED_INT.incrementAndGet()).append(".")
                    .append(extension);
            try {
                URL url = toUrl(fileName.toString());
                writeBytesToURL(event.data, url);
                try {
                    File f = new File(url.toURI());
                    stepTestThreadLocal.get().info("",
                            MediaEntityBuilder.createScreenCaptureFromPath(f.getAbsolutePath()).build());
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void writeBytesToURL(byte[] buf, URL url) throws IOException {
        OutputStream out = createReportFileOutputStream(url);
        try {
            out.write(buf);
        } catch (IOException e) {
            throw new IOException("Unable to write to report file item: ", e);
        }
    }

    private static OutputStream createReportFileOutputStream(URL url) {
        try {
            return new URLOutputStream(url);
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private URL toUrl(String fileName) {
        try {
            Object prop = ExtentService.getProperty(SCREENSHOT_DIR_PROPERTY);
            String screenshotDir = prop == null ? "test-output/" : String.valueOf(prop);
            URL url = Paths.get(screenshotDir, fileName).toUri().toURL();
            return url;
        } catch (IOException e) {
            throw new CucumberException(e);
        }
    }

    private void handleWrite(WriteEvent event) {
        String text = event.text;
        if (text != null && !text.isEmpty()) {
            stepTestThreadLocal.get().info(text);
        }
    }

    private void finishReport() {
        ExtentService.getInstance().flush();
    }

    private synchronized void handleStartOfFeature(TestCase testCase) {
        if (currentFeatureFile == null || !currentFeatureFile.equals(testCase.getUri())) {
            currentFeatureFile.set(testCase.getUri());
            createFeature(testCase);
        }
    }

    private synchronized void createFeature(TestCase testCase) {
        Feature feature = testSources.getFeature(testCase.getUri());
        if (feature != null) {
            if (featureMap.containsKey(feature.getName())) {
                featureTestThreadLocal.set(featureMap.get(feature.getName()));
                return;
            }
            if (featureTestThreadLocal.get() != null
                    && featureTestThreadLocal.get().getModel().getName().equals(feature.getName())) {
                return;
            }
            ExtentTest t = ExtentService.getInstance().createTest(
                    com.aventstack.extentreports.gherkin.model.Feature.class, feature.getName(),
                    feature.getDescription());
            featureTestThreadLocal.set(t);
            featureMap.put(feature.getName(), t);
            List<String> tagList = createTagsList(feature.getTags());
            tagList.forEach(featureTestThreadLocal.get()::assignCategory);
        }
    }

    private List<String> createTagsList(List<Tag> tags) {
        List<String> tagList = new ArrayList<>();
        for (Tag tag : tags) {
            tagList.add(tag.getName());
        }
        return tagList;
    }

    private synchronized void handleScenarioOutline(TestCase testCase) {
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile.get(), testCase.getLine());
        if (TestSourcesModel.isScenarioOutlineScenario(astNode)) {
            ScenarioOutline scenarioOutline = (ScenarioOutline) TestSourcesModel.getScenarioDefinition(astNode);
            if (currentScenarioOutline.get() == null
                    || !currentScenarioOutline.get().getName().equals(scenarioOutline.getName())) {
                scenarioOutlineThreadLocal.set(null);
                createScenarioOutline(scenarioOutline);
                currentScenarioOutline.set(scenarioOutline);
                addOutlineStepsToReport(scenarioOutline);
            }
            Examples examples = (Examples) astNode.parent.node;
            if (currentExamples.get() == null || !currentExamples.get().equals(examples)) {
                currentExamples.set(examples);
                createExamples(examples);
            }
        } else {
            scenarioOutlineThreadLocal.set(null);
            currentScenarioOutline.set(null);
            currentExamples.set(null);
        }
    }

    private synchronized void createScenarioOutline(ScenarioOutline scenarioOutline) {
        if (scenarioOutlineMap.containsKey(scenarioOutline.getName())) {
            scenarioOutlineThreadLocal.set(scenarioOutlineMap.get(scenarioOutline.getName()));
            return;
        }
        if (scenarioOutlineThreadLocal.get() == null) {
            ExtentTest t = featureTestThreadLocal.get().createNode(
                    com.aventstack.extentreports.gherkin.model.ScenarioOutline.class, scenarioOutline.getName(),
                    scenarioOutline.getDescription());
            scenarioOutlineThreadLocal.set(t);
            scenarioOutlineMap.put(scenarioOutline.getName(), t);
            List<String> tags = createTagsList(scenarioOutline.getTags());
            tags.forEach(scenarioOutlineThreadLocal.get()::assignCategory);
        }
    }

    private synchronized void addOutlineStepsToReport(ScenarioOutline scenarioOutline) {
        for (Step step : scenarioOutline.getSteps()) {
            if (step.getArgument() != null) {
                Node argument = step.getArgument();
                if (argument instanceof DocString) {
                    createDocStringMap((DocString) argument);
                } else if (argument instanceof DataTable) {

                }
            }
        }
    }

    private Map<String, Object> createDocStringMap(DocString docString) {
        Map<String, Object> docStringMap = new HashMap<String, Object>();
        docStringMap.put("value", docString.getContent());
        return docStringMap;
    }

    private void createExamples(Examples examples) {
        List<TableRow> rows = new ArrayList<>();
        rows.add(examples.getTableHeader());
        rows.addAll(examples.getTableBody());
        String[][] data = getTable(rows);
        String markup = MarkupHelper.createTable(data).getMarkup();
        if (examples.getName() != null && !examples.getName().isEmpty()) {
            markup = examples.getName() + markup;
        }
        markup = scenarioOutlineThreadLocal.get().getModel().getDescription() + markup;
        scenarioOutlineThreadLocal.get().getModel().setDescription(markup);
    }

    private String[][] getTable(List<TableRow> rows) {
        String data[][] = null;
        int rowSize = rows.size();
        for (int i = 0; i < rowSize; i++) {
            TableRow row = rows.get(i);
            List<TableCell> cells = row.getCells();
            int cellSize = cells.size();
            if (data == null) {
                data = new String[rowSize][cellSize];
            }
            for (int j = 0; j < cellSize; j++) {
                data[i][j] = cells.get(j).getValue();
            }
        }
        return data;
    }

    private synchronized void createTestCase(TestCase testCase) {
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile.get(), testCase.getLine());
        if (astNode != null) {
            ScenarioDefinition scenarioDefinition = TestSourcesModel.getScenarioDefinition(astNode);
            ExtentTest parent = scenarioOutlineThreadLocal.get() != null ? scenarioOutlineThreadLocal.get()
                    : featureTestThreadLocal.get();
            ExtentTest t = parent.createNode(com.aventstack.extentreports.gherkin.model.Scenario.class,
                    scenarioDefinition.getName(), scenarioDefinition.getDescription());
            scenarioThreadLocal.set(t);
        }
        if (!testCase.getTags().isEmpty()) {
            testCase.getTags().forEach(x -> scenarioThreadLocal.get().assignCategory(x.getName()));
        }
    }

    private synchronized void createTestStep(PickleStepTestStep testStep) {
        String stepName = testStep.getStepText();
        TestSourcesModel.AstNode astNode = testSources.getAstNode(currentFeatureFile.get(), testStep.getStepLine());
        if (astNode != null) {
            Step step = (Step) astNode.node;
            try {
                String name = stepName == null || stepName.isEmpty()
                        ? step.getText().replace("<", "&lt;").replace(">", "&gt;")
                        : stepName;
                ExtentTest t = scenarioThreadLocal.get().createNode(new GherkinKeyword(step.getKeyword().trim()),
                        step.getKeyword() + name, testStep.getCodeLocation());
                stepTestThreadLocal.set(t);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        if (!testStep.getStepArgument().isEmpty()) {
            Argument argument = testStep.getStepArgument().get(0);
            if (argument instanceof PickleString) {
                createDocStringMap((PickleString) argument);
            } else if (argument instanceof PickleTable) {
                List<PickleRow> rows = ((PickleTable) argument).getRows();
                stepTestThreadLocal.get().pass(MarkupHelper.createTable(getPickleTable(rows)).getMarkup());
            }
        }
    }

    private String[][] getPickleTable(List<PickleRow> rows) {
        String data[][] = null;
        int rowSize = rows.size();
        for (int i = 0; i < rowSize; i++) {
            PickleRow row = rows.get(i);
            List<PickleCell> cells = row.getCells();
            int cellSize = cells.size();
            if (data == null) {
                data = new String[rowSize][cellSize];
            }
            for (int j = 0; j < cellSize; j++) {
                data[i][j] = cells.get(j).getValue();
            }
        }
        return data;
    }

    private Map<String, Object> createDocStringMap(PickleString docString) {
        Map<String, Object> docStringMap = new HashMap<String, Object>();
        docStringMap.put("value", docString.getContent());
        return docStringMap;
    }

    // the below additions are from PR #33
    // https://github.com/extent-framework/extentreports-cucumber4-adapter/pull/33
    public static synchronized void addTestStepLog(String message) {
        stepTestThreadLocal.get().info(message);
    }

    public static synchronized void addTestStepScreenCaptureFromPath(String imagePath) throws IOException {
        stepTestThreadLocal.get().addScreenCaptureFromPath(imagePath);
    }

    public static synchronized void addTestStepScreenCaptureFromPath(String imagePath, String title)
            throws IOException {
        stepTestThreadLocal.get().addScreenCaptureFromPath(imagePath, title);
    }

    private static String takeScreenshot() throws IOException {
        String fileLocation = "";
        String name = "";
        final File screenshot = ((TakesScreenshot) DriverManager.getDriverInstance()).getScreenshotAs(OutputType.FILE);
        name = "screenshot".concat(Long.toString(System.currentTimeMillis()));
        fileLocation = reportPath.concat(File.separator).concat(name);
        FileUtils.copyFile(screenshot, new File(fileLocation));
        return name;
    }
}

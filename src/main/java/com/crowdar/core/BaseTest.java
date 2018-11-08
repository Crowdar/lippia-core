package com.crowdar.core;

import com.crowdar.bdd.StoryRunner;
import com.crowdar.email.EmailUtil;
import com.crowdar.report.ReportManager;
import com.crowdar.report.ScreenshotCapture;
import com.crowdar.web.BrowserConfiguration;
import com.crowdar.web.WebDriverManager;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author jCarames
 */
public abstract class BaseTest {

    private static final String STATUS_TEST_CONTEXT_KEY = "status";

    public BaseTest() {
        super();
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        setRunInstanceProperty();
        setFrameworkRootProperty();
        RetryManager.setRetryTests(context);
        System.setProperty("org.freemarker.loggerLibrary", "SLF4j");
        WebDriverManager.build(BrowserConfiguration.getBrowserConfiguration(PropertyManager.getProperty("crowdar.jbehave.browser")));
    }

    @BeforeTest(alwaysRun = true)
    public void startTest(final ITestContext testContext) {
        StoryRunner.setTestContextProperties(testContext.getName());
        testContext.setAttribute(STATUS_TEST_CONTEXT_KEY, null);
        MyThreadLocal.get().setData(STATUS_TEST_CONTEXT_KEY, null);
        String reportDescription = testContext.getCurrentXmlTest().getParameter("reportDescription");
        ReportManager.startParentTest(reportDescription);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        StoryRunner.setMethodContextProperties(method.getName());
    }

    private void logTestDescription(String testDescription) {
        if (testDescription != null && !testDescription.isEmpty()) {
            ReportManager.writeResult(LogStatus.INFO, testDescription);
        }
    }







    private void setRunInstanceProperty() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String runInstance = sdf.format(cal.getTime());
        System.setProperty(Constants.SYSTEM_PROPERTY_RUN_INSTANCE, runInstance);
    }

    private void setFrameworkRootProperty() {
        String userDir = System.getProperty("user.dir");
        System.setProperty(Constants.SYSTEM_PROPERTY_FRAMEWORK_ROOT,
                userDir.substring(0, userDir.lastIndexOf(File.separator)));
    }
}
package com.crowdar.mobile.core;

import com.crowdar.core.*;
import com.relevantcodes.extentreports.LogStatus;
import com.crowdar.email.EmailUtil;
import com.crowdar.mobile.AppiumDriverManager;
import com.crowdar.mobile.mobileDriver.PlatformConfiguration;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.*;
import com.crowdar.report.ReportManager;
import com.crowdar.report.ScreenshotCapture;

import java.io.File;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @author Juan Manuel Spoleti
 */
public abstract class BaseTest {

    private static final String STATUS_TEST_CONTEXT_KEY = "status";

    public BaseTest() {
        super();
    }

    @BeforeSuite
    public void beforeSuite() {
        System.setProperty("org.freemarker.loggerLibrary", "SLF4j");
        setRunInstanceProperty();
        setFrameworkRootProperty();
        AppiumDriverManager.build(PlatformConfiguration.getPlatformConfiguration(System.getProperty("platform")));

    }

    @BeforeTest
    public void startTest(final ITestContext testContext) {
        GUIStoryRunner.setTestContextProperties(testContext.getName());
        testContext.setAttribute(STATUS_TEST_CONTEXT_KEY, null);
        MyThreadLocal.get().setData(STATUS_TEST_CONTEXT_KEY, null);
        String flowDescription = testContext.getCurrentXmlTest().getParameter("flowDescription");
        ReportManager.startParentTest(flowDescription);
    }

    @BeforeMethod
    @Parameters({"testDescription"})
    public void beforeMethod(final ITestContext testContext, Method method, ITestResult result,
                             @Optional String testDescription) {

        ReportManager.startChildTest(method.getName());

        this.logTestDescription(testDescription);
    }

    private void logTestDescription(String testDescription) {
        if (testDescription != null && !testDescription.isEmpty()) {
            ReportManager.writeResult(LogStatus.INFO, testDescription);
        }
    }

    @AfterMethod
    public void afterMethod(final ITestContext testContext, Method method, ITestResult result) {
        switch (result.getStatus()) {
            case ITestResult.FAILURE:
                ReportManager.writeResult(LogStatus.FAIL, "Test Case Failed is " + result.getName());
                ReportManager.writeResult(LogStatus.FAIL, "Test Case Failed is " + result.getThrowable());

                String img = ReportManager.addScreenCapture(
                        "../" + ReportManager.getRelativeHtmlPath(ScreenshotCapture.getScreenCaptureFileName()));
                ReportManager.writeResult(LogStatus.FAIL, "Screenshot" + img);
                ReportManager.writeParentResult(LogStatus.FAIL, method.getName());

                MyThreadLocal.get().setData(STATUS_TEST_CONTEXT_KEY, ITestResult.SKIP);
                break;

            case ITestResult.SKIP:
                ReportManager.writeResult(LogStatus.SKIP, "Test Case Skipped is " + result.getName());
                break;

            case ITestResult.SUCCESS:
                ReportManager.writeResult(LogStatus.PASS, method.getName() + " pass successful");
                break;
        }

        ReportManager.writeResult(LogStatus.INFO, "<a href='" + ReportManager.getScenariosHtmlRelativePath(GUIStoryRunner.getStoryLogFileName()) + "'>Scenarios</a>");
        ReportManager.endTest();
    }

    @AfterTest
    public void afterTest() {
        AppiumDriverManager.dismissDriver();
    }

    @AfterSuite
    public void afterSuite() {
        ReportManager.endReport();
        if (Boolean.valueOf(PropertyManager.getProperty("report.mail.available"))) {
            EmailUtil.sendReportEmail();
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

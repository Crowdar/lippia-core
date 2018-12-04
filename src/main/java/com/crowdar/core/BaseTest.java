package com.crowdar.core;

import com.crowdar.bdd.StoryRunner;
import com.crowdar.email.EmailUtil;
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
@Listeners({ com.crowdar.core.listeners.SuiteTestngListener.class })
public abstract class BaseTest {

    private static final String STATUS_TEST_CONTEXT_KEY = "status";

    public BaseTest() {
        super();
    }

    @BeforeTest(alwaysRun = true)
    public void startTest(final ITestContext testContext) {
        StoryRunner.setTestContextProperties(testContext.getName());
        testContext.setAttribute(STATUS_TEST_CONTEXT_KEY, null);
        MyThreadLocal.get().setData(STATUS_TEST_CONTEXT_KEY, null);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        StoryRunner.setMethodContextProperties(method.getName());
    }


}
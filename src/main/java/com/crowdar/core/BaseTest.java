package com.crowdar.core;

import com.crowdar.bdd.GUIStoryRunnerV2;
import com.crowdar.bdd.RetryAnalyzerImpl;
import com.crowdar.email.EmailUtil;
import com.crowdar.report.ReportManager;
import com.crowdar.report.ScreenshotCapture;
import com.crowdar.web.BrowserConfiguration;
import com.crowdar.web.WebDriverManager;
import com.relevantcodes.extentreports.LogStatus;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;
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


	@BeforeTest(alwaysRun = true)
	public void startTest(final ITestContext testContext) {
		GUIStoryRunnerV2.setTestContextProperties(testContext.getName());
		testContext.setAttribute(STATUS_TEST_CONTEXT_KEY, null);
		MyThreadLocal.get().setData(STATUS_TEST_CONTEXT_KEY, null);
		String reportDescription = testContext.getCurrentXmlTest().getParameter("reportDescription");
		ReportManager.startParentTest(reportDescription);
	}

	@BeforeMethod(alwaysRun = true)
	public void beforeMethod(Method method) {
		GUIStoryRunnerV2.setMethodContextProperties(method.getName());
	}

	private void logTestDescription(String testDescription) {
		if (testDescription != null && !testDescription.isEmpty()) {
			ReportManager.writeResult(LogStatus.INFO, testDescription);
		}
	}








}
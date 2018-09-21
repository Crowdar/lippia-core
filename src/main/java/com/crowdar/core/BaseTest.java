package com.crowdar.core;

import com.crowdar.bdd.GUIStoryRunnerV2;
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
 *
 * @author jCarames
 *
 */
public abstract class BaseTest {

	private static final String STATUS_TEST_CONTEXT_KEY = "status";

	public BaseTest() {
		super();
	}

	@BeforeSuite(alwaysRun=true)
	public void beforeSuite() {
		System.setProperty("org.freemarker.loggerLibrary", "SLF4j");
		setRunInstanceProperty();
		setFrameworkRootProperty();
		WebDriverManager.build(BrowserConfiguration.getBrowserConfiguration(PropertyManager.getProperty("crowdar.jbehave.browser")));
	}

	@BeforeTest(alwaysRun=true)
	public void startTest(final ITestContext testContext) {
		GUIStoryRunnerV2.setTestContextProperties(testContext.getName());
		testContext.setAttribute(STATUS_TEST_CONTEXT_KEY, null);
		MyThreadLocal.get().setData(STATUS_TEST_CONTEXT_KEY, null);
		String reportDescription = testContext.getCurrentXmlTest().getParameter("reportDescription");
		ReportManager.startParentTest(reportDescription);
	}

	@BeforeMethod(alwaysRun=true)
	@Parameters({ "testDescription" })
	public void beforeMethod(final ITestContext testContext, Method method, ITestResult result,
                             @Optional String testDescription) {

		ReportManager.startChildTest(method.getName());

		this.logSpiraDescription(testDescription);
	}

	private void logSpiraDescription(String testDescription) {
		if (testDescription != null && !testDescription.isEmpty()) {
			ReportManager.writeResult(LogStatus.INFO, testDescription);
		}
	}

	@AfterMethod(alwaysRun=true)
	public void afterMethod(final ITestContext testContext, Method method, ITestResult result) {
		switch (result.getStatus()) {
		case ITestResult.FAILURE:
			ReportManager.writeResult(LogStatus.FAIL, "Test Case Failed is " + result.getName());
			ReportManager.writeResult(LogStatus.FAIL, "Test Case Failed is " + result.getThrowable());

			String img = ReportManager.addScreenCapture(
					"../" + ReportManager.getRelativeHtmlPath(ScreenshotCapture.getScreenCaptureFileName()));
			ReportManager.writeResult(LogStatus.FAIL, "Screenshot" + img);
			ReportManager.writeParentResult(LogStatus.FAIL, method.getName());

			// Al fallar debe skipear los restantes dentro del test.
			MyThreadLocal.get().setData(STATUS_TEST_CONTEXT_KEY, ITestResult.SKIP);
			break;

		case ITestResult.SKIP:
			ReportManager.writeResult(LogStatus.SKIP, "Test Case Skipped is " + result.getName());
			break;

		case ITestResult.SUCCESS:
			ReportManager.writeResult(LogStatus.PASS, method.getName() + " pass successful");
			break;
		}

		ReportManager.writeResult(LogStatus.INFO, "<a href='" + ReportManager.getScenariosHtmlRelativePath(GUIStoryRunnerV2.getStoryLogFileName()) + "'>Scenarios</a>");
		ReportManager.endTest();
	}

	@AfterTest(alwaysRun=true)
	public void afterTest() {
		WebDriverManager.dismissAll();
	}

	@AfterSuite(alwaysRun=true)
	public void afterSuite() {
		ReportManager.endReport();
		if (Boolean.valueOf(PropertyManager.getProperty("report.mail.available"))) {
			EmailUtil.sendReportEmail();;
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

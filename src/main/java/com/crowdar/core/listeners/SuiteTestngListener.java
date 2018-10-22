package com.crowdar.core.listeners;

import com.crowdar.core.Constants;
import com.crowdar.core.PropertyManager;
import com.crowdar.core.RetryManager;
import com.crowdar.email.EmailUtil;
import com.crowdar.web.BrowserConfiguration;
import com.crowdar.web.WebDriverManager;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SuiteTestngListener implements ISuiteListener {
    @Override
    public void onStart(ISuite iSuite) {
        setJbehaveRunInstanceProperty();
        setJbehaveFrameworkRootProperty();
        RetryManager.setRetryTests(iSuite.getAllMethods());
        System.setProperty("org.freemarker.loggerLibrary", "SLF4j");
        WebDriverManager.build(BrowserConfiguration.getBrowserConfiguration(PropertyManager.getProperty("crowdar.jbehave.browser")));


    }

    @Override
    public void onFinish(ISuite iSuite) {
        WebDriverManager.dismissAll();
        if (Boolean.valueOf(PropertyManager.getProperty("report.mail.available"))) {
            EmailUtil.sendReportEmail();
        }
    }

    private void setJbehaveRunInstanceProperty() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String runInstance = sdf.format(cal.getTime());
        System.setProperty(Constants.SYSTEM_PROPERTY_RUN_INSTANCE, runInstance);
    }

    private void setJbehaveFrameworkRootProperty() {
        String userDir = System.getProperty("user.dir");
        System.setProperty(Constants.SYSTEM_PROPERTY_FRAMEWORK_ROOT,
                userDir.substring(0, userDir.lastIndexOf(File.separator)));
    }

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite(ITestContext context) {
        RetryManager.setRetryTests(context);
        System.setProperty("org.freemarker.loggerLibrary", "SLF4j");
        WebDriverManager.build(BrowserConfiguration.getBrowserConfiguration(PropertyManager.getProperty("crowdar.jbehave.browser")));
    }
}

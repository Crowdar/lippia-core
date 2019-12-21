package com.crowdar.report;

import com.crowdar.core.Context;
import com.crowdar.core.MyThreadLocal;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.impl.Log4JLogger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;

public final class ScreenshotCapture {
    private static final Log4JLogger logger = new Log4JLogger(ScreenshotCapture.class.getSimpleName());


    public static void createScreenCapture(RemoteWebDriver driver) {
        try {
            File scrFile = driver.getScreenshotAs(OutputType.FILE);
            FileUtils.copyFile(scrFile, new File(getScreenCaptureFileLocation()));
        } catch (IOException ioe) {
            logger.error(ioe);
        }
    }

    public static String getScreenCaptureFileLocation() {
        return "target" + File.separator + getScreenCaptureFileName();
    }

    public static String getScreenCaptureFileName() {
        return MyThreadLocal.getData(Context.CONTEXT_TEST_NAME_KEY) + (String) MyThreadLocal.getData(Context.CONTEXT_TEST_ID_KEY) + ".png";
    }

}

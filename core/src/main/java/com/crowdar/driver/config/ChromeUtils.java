package com.crowdar.driver.config;


import com.crowdar.core.PropertyManager;
import com.crowdar.util.FileUtils;
import com.crowdar.util.ZipUtils;
import org.apache.http.HeaderElement;
import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

public class ChromeUtils {

    private static List<HeaderElement> HEADERS = new ArrayList<>();

    public static void enableBrowserLog(DesiredCapabilities caps) {
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable(LogType.BROWSER, Level.ALL);
        caps.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
    }

    public static void addHeaders(HeaderElement... headers) {
        HEADERS.addAll(Arrays.asList(headers));
    }

    public static void insertHeadersExtension(ChromeOptions options) {
        // define path to resources
        String unpackedExtensionPath = System.getProperty("user.dir") + "/src/main/resources/chrome_extension";
        File f = new File(unpackedExtensionPath + "/background.js");
        List<String> lines = FileUtils.readLines(f, "UTF-8");
        String user = PropertyManager.getProperty(System.getProperty("CURRENT_USER_TYPE"));
        FileUtils.modifyLine(lines, 0, String.format("var username = \"%s\";", user));
        FileUtils.writeLines(f, "UTF-8", lines);
        String crxExtensionPath = ZipUtils.packZipWithNameOfFolder(unpackedExtensionPath, "crx");
        // creating capability based on packed extension
        options.addExtensions(new File(crxExtensionPath));
        Logger.getLogger(ChromeUtils.class).info("Using Headers: " + HEADERS);
    }

    public static void addDisableNotifications(ChromeOptions options) {
        options.addArguments("--disable-notifications");
    }

    public static void addDisableInfoBars(ChromeOptions options) {
        options.addArguments("--disable-infobars");
    }

    public static void startMaximized(ChromeOptions options) {
        options.addArguments("--start-maximized");
    }
}


package com.crowdar.core;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.FluentWait;

public class DownloadManager {

    private static String getDownloadFolder() {
        return PropertyManager.getProperty("crowdar.download.folder");
    }

    protected static File getFile(String fileName) {
        return Paths.get(getDownloadFolder(), fileName).toFile();
    }

    public static void deleteAllFiles() throws IOException {
        FileUtils.cleanDirectory(Paths.get(getDownloadFolder()).toFile());
    }

    public static boolean isFileDownload(String fileName) {
        return getFile(fileName).exists();
    }

    public static boolean isFileDownload(String fileName, WebDriver driver, long timeToWait) {
        waitFileDownload(fileName, driver, timeToWait);
        return getFile(fileName).exists();
    }

    private static void waitFileDownload(String fileName, WebDriver driver, long timeToWait) {
        FluentWait<WebDriver> wait = new FluentWait(driver)
                .withTimeout(timeToWait, TimeUnit.SECONDS)
                .pollingEvery(200, TimeUnit.MILLISECONDS);
        File fileToCheck = Paths.get(getDownloadFolder())
                .resolve(fileName)
                .toFile();
        wait.until((WebDriver wd) -> fileToCheck.exists());
    }
}

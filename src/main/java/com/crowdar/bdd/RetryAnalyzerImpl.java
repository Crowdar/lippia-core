package com.crowdar.bdd;

import com.crowdar.core.PropertyManager;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzerImpl implements IRetryAnalyzer {

    public static int RETRY_COUNT = 0;
    private int maxRetryCount = Integer.valueOf(PropertyManager.getProperty("repeat.test.failure"));

    public boolean retry(ITestResult result) {
        if (RETRY_COUNT < maxRetryCount) {
            RETRY_COUNT++;
            return true;
        }
        return false;
    }
}
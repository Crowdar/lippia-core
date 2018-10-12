package com.crowdar.bdd;

import com.crowdar.core.Context;
import com.crowdar.core.MyThreadLocal;
import com.crowdar.core.PropertyManager;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzerImpl implements IRetryAnalyzer {

    public static int RETRY_COUNT = 0;
    private int maxRetryCount = Integer.valueOf(PropertyManager.getProperty("repeat.test.failure"));

    public boolean retry(ITestResult result) {
        if (RETRY_COUNT < maxRetryCount) {
            System.out.println(RETRY_COUNT);
            RETRY_COUNT++;
            MyThreadLocal.get().setData(Context.RETRY_COUNT, RETRY_COUNT);
            return true;
        }
        return false;
    }
}
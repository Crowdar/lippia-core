package com.crowdar.bdd;

import com.crowdar.core.Context;
import com.crowdar.core.MyThreadLocal;
import com.crowdar.core.PropertyManager;
import com.sun.org.apache.regexp.internal.RE;
import com.typesafe.config.ConfigException;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzerImpl implements IRetryAnalyzer {

    private int RETRY_COUNT = 0;
    private int maxRetryCount = Integer.valueOf(PropertyManager.getProperty("repeat.test.failure"));

    public boolean retry(ITestResult result) {
        int retryCount;
        try {
            retryCount = (Integer) MyThreadLocal.get().getData(Context.RETRY_COUNT);
        }catch (NullPointerException e){
            retryCount = RETRY_COUNT;
        }
        if (retryCount < maxRetryCount) {
            MyThreadLocal.get().setData(Context.RETRY_COUNT, retryCount);
            return true;
        }
        return false;
    }
}
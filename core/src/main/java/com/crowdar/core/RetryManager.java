package com.crowdar.core;

import java.util.List;

import org.testng.ITestContext;
import org.testng.ITestNGMethod;

import com.crowdar.bdd.RetryAnalyzerImpl;

public class RetryManager {

    public static void setRetryTests(ITestContext context) {
        for (ITestNGMethod method : context.getAllTestMethods()) {
            method.setRetryAnalyzer(new RetryAnalyzerImpl());
        }
        MyThreadLocal.setData(Context.RETRY_COUNT, 0);
    }

    public static void setRetryTests(List<ITestNGMethod> listMetods) {
        for (ITestNGMethod method : listMetods) {
            method.setRetryAnalyzer(new RetryAnalyzerImpl());
        }
    }
}

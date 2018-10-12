package com.crowdar.core;

import com.crowdar.bdd.RetryAnalyzerImpl;
import org.testng.ITestContext;
import org.testng.ITestNGMethod;

public class RetryManager {

    public static void setRetryTests(ITestContext context){
        for (ITestNGMethod method : context.getAllTestMethods()) {
            method.setRetryAnalyzer(new RetryAnalyzerImpl());
        }
        MyThreadLocal.get().setData(Context.RETRY_COUNT, 0);
    }
}

package com.crowdar.core;

import java.lang.reflect.Method;

import org.testng.ITestContext;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;

import com.crowdar.bdd.StoryRunner;

/**
 * @author jCarames
 */
@Listeners({com.crowdar.core.listeners.SuiteTestngListener.class})
public abstract class BaseTest {

    private static final String STATUS_TEST_CONTEXT_KEY = "status";

    public BaseTest() {
        super();
    }

    @BeforeTest(alwaysRun = true)
    public void startTest(final ITestContext testContext) {
        StoryRunner.setTestContextProperties(testContext.getName());
        testContext.setAttribute(STATUS_TEST_CONTEXT_KEY, null);
        MyThreadLocal.get().setData(STATUS_TEST_CONTEXT_KEY, null);
    }

    @BeforeMethod(alwaysRun = true)
    public void beforeMethod(Method method) {
        StoryRunner.setMethodContextProperties(method.getName());
    }
}
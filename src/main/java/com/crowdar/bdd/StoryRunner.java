package com.crowdar.bdd;

import com.crowdar.core.Context;
import com.crowdar.core.MyThreadLocal;
import com.crowdar.driver.DriverManager;
import com.crowdar.web.WebDriverManager;
import com.google.common.collect.Lists;
import org.jbehave.core.embedder.Embedder;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;

import java.io.File;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * StoryRunner class This class is used to run Jbehave stories
 *
 */
public class StoryRunner {

    public StoryRunner() {
    }

    public static void runGUIStories(String storyPath) {
        storyPath = storyPath.replace("/", File.separator);
        List<String> storyPaths = Lists.newArrayList(storyPath);

        boolean testResult = true;
        String failMessage = null;

        if (MyThreadLocal.get().getData("status") != null && ((Integer) MyThreadLocal.get().getData("status")).intValue() == ITestResult.SKIP) {
            throw new SkipException("");
        }

        try {
            setStoryRunnerProperties(storyPaths);

//            Embedder embedder = new GUIStories(WebDriverManager.getDriverInstance());
            Embedder embedder = new GUIStories(DriverManager.getDriverInstance());
            embedder.useMetaFilters(asList("-skip"));
            embedder.runStoriesAsPaths(StoryUtils.storyPaths(storyPaths));

        } catch (NullPointerException npe) {
            npe.printStackTrace();

        } catch (Exception e) {
            testResult = false;
            failMessage = e.getMessage();
        } finally {
            Assert.assertTrue(testResult, failMessage);
        }
    }

    public static void runAPIStories(String storyPath) {
        storyPath = storyPath.replace("/", File.separator);
        List<String> storyPaths = Lists.newArrayList(storyPath);
        boolean testResult = true;
        String failMessage = null;

        if (MyThreadLocal.get().getData("status") != null && ((Integer) MyThreadLocal.get().getData("status")).intValue() == ITestResult.SKIP) {
            throw new SkipException("");
        }

        try {
            setStoryRunnerProperties(storyPaths);

            Embedder embedder = new ApiStories();
            embedder.useMetaFilters(asList("-skip"));
            embedder.runStoriesAsPaths(StoryUtils.storyPaths(storyPaths));

        } catch (NullPointerException npe) {
            npe.printStackTrace();

        } catch (Exception e) {
            testResult = false;
            failMessage = e.getMessage();
        } finally {
            Assert.assertTrue(testResult, failMessage);
        }
    }

    public static void setStoryRunnerProperties(List<String> storyPath) {
        MyThreadLocal.get().setData(Context.CONTEXT_STORY_NAME_KEY, storyPath.get(0));
    }

    public static void setTestContextProperties(String testName) {
        MyThreadLocal.get().setData(Context.CONTEXT_TEST_ID_KEY, String.valueOf(Thread.currentThread().getId()));
        MyThreadLocal.get().setData(Context.CONTEXT_TEST_NAME_KEY, testName);
    }

    public static void setMethodContextProperties(String methodName) {
        MyThreadLocal.get().setData(Context.CONTEXT_METHOD_NAME_KEY, methodName);
    }

    public static String getStoryLogFileName() {
        return (String) MyThreadLocal.get().getData(Context.CONTEXT_STORY_NAME_KEY);
    }
}
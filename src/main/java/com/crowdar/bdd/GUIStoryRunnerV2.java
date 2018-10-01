package com.crowdar.bdd;

import com.crowdar.core.Context;
import com.crowdar.core.MyThreadLocal;
import com.crowdar.report.ScreenshotCapture;
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
 * @author Agustin Mascheroni
 *
 */
public final class GUIStoryRunnerV2 {

	private GUIStoryRunnerV2() {
	}

	public static void runStory(String storyPath) {
		storyPath = storyPath.replace("/", File.separator);
		runStories(Lists.newArrayList(storyPath));
	}

	private static void runStories(List<String> storyPath) {
		boolean testResult = true;
		String failMessage = null;

		if(MyThreadLocal.get().getData("status") != null && ((Integer)MyThreadLocal.get().getData("status")).intValue() == ITestResult.SKIP){
			throw new SkipException("");
		}

		try{
			setStoryRunnerProperties(storyPath);

			Embedder embedder = new GUIStories(WebDriverManager.getDriverInstance());
			embedder.useMetaFilters(asList("-skip"));
			embedder.runStoriesAsPaths(StoryUtils.storyPaths(storyPath));

		}catch (NullPointerException npe){
			npe.printStackTrace();

		}catch (Exception e) {
			testResult = false;
			failMessage = e.getMessage();
			ScreenshotCapture.createScreenCapture(WebDriverManager.getDriverInstance());
		} finally {
			Assert.assertTrue(testResult, failMessage);
		}
	}

	public static void setStoryRunnerProperties(List<String> storyPath) {
		MyThreadLocal.get().setData(Context.CONTEXT_STORY_NAME_KEY, storyPath.get(0));
	}

	public static void setTestContextProperties(String testName){
		MyThreadLocal.get().setData(Context.CONTEXT_TEST_ID_KEY, String.valueOf(Thread.currentThread().getId()));
		MyThreadLocal.get().setData(Context.CONTEXT_TEST_NAME_KEY, testName);
	}

	public static void setMethodContextProperties(String methodName){
		MyThreadLocal.get().setData(Context.CONTEXT_METHOD_NAME_KEY, methodName);
	}

	public static String getStoryLogFileName(){
		return (String)MyThreadLocal.get().getData(Context.CONTEXT_STORY_NAME_KEY);
	}
}
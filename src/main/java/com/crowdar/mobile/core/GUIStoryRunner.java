package com.crowdar.mobile.core;

import com.crowdar.bdd.GUIStories;
import com.crowdar.bdd.StoryUtils;
import com.google.common.collect.Lists;
import com.crowdar.core.Context;
import com.crowdar.core.MyThreadLocal;
import com.crowdar.mobile.AppiumDriverManager;
import org.jbehave.core.embedder.Embedder;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.SkipException;
import com.crowdar.report.ScreenshotCapture;

import java.io.File;
import java.util.List;

import static java.util.Arrays.asList;

/**
 * StoryRunner class This class is used to run Jbehave stories
 * 
 * @author Juan Manuel Spoleti
 *
 */
public final class GUIStoryRunner {
	
	private GUIStoryRunner() {
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
		
			Embedder embedder = new GUIStories(AppiumDriverManager.getDriverInstance());
			embedder.useMetaFilters(asList("-skip"));
			embedder.runStoriesAsPaths(StoryUtils.storyPaths(storyPath));

		}catch (NullPointerException npe){
			npe.printStackTrace();
			
		}catch (Exception e) {
				testResult = false;
				failMessage = e.getMessage();
				ScreenshotCapture.createScreenCapture(AppiumDriverManager.getDriverInstance());
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


	public static String getStoryLogFileName(){
		return (String)MyThreadLocal.get().getData(Context.CONTEXT_STORY_NAME_KEY);
	}
}
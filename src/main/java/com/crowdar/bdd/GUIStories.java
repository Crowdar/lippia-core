package com.crowdar.bdd;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.openqa.selenium.WebDriver;
import org.reflections.Reflections;

import com.crowdar.bdd.EmbedderBase;
import com.crowdar.core.PageSteps;
import com.google.common.collect.Lists;

public class GUIStories extends EmbedderBase {
	
	private WebDriver driver;
	
	public GUIStories(WebDriver driver) {
		this.driver = driver;
	}

	@Override
	public InjectableStepsFactory stepsFactory() {
		
		List<PageSteps> pageStepsImplementations = Lists.newArrayList();

		Reflections reflections = new Reflections("com.crowdar");
		Set<Class<? extends PageSteps>> subTypes = reflections.getSubTypesOf(PageSteps.class);
		
		
		for (Class<? extends PageSteps> currentClass : subTypes) {
			Constructor<?> constructor = null;
			
			try {
				constructor = currentClass.getDeclaredConstructor(WebDriver.class);// Gives constructor which takes in webDriver I assume
				pageStepsImplementations.add((PageSteps)constructor.newInstance(driver));
			
			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		return new InstanceStepsFactory(configuration(), pageStepsImplementations);
	}
}
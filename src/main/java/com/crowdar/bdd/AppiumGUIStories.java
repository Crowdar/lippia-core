package com.crowdar.bdd;

import com.crowdar.core.PropertyManager;
import com.crowdar.mobile.core.PageSteps;
import com.google.common.collect.Lists;
import io.appium.java_client.AppiumDriver;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

public class AppiumGUIStories extends EmbedderBase {

    private AppiumDriver driver;

    public AppiumGUIStories(AppiumDriver driver) {
        this.driver = driver;
    }

    @Override
    public InjectableStepsFactory stepsFactory() {

        List<PageSteps> pageStepsImplementations = Lists.newArrayList();


        Reflections reflections = new Reflections(PropertyManager.getProperty("stepsClass.location"));

        Set<Class<? extends PageSteps>> subTypes = reflections.getSubTypesOf(PageSteps.class);


        for (Class<? extends PageSteps> currentClass : subTypes) {
            Constructor<?> constructor = null;

            try {
                constructor = currentClass.getDeclaredConstructor(AppiumDriver.class);
                pageStepsImplementations.add((PageSteps)constructor.newInstance(driver));

            } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return new InstanceStepsFactory(configuration(), pageStepsImplementations);
    }
}
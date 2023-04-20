package io.lippia.api.lowcode.internal;


import cucumber.runtime.io.ResourceLoader;
import cucumber.runtime.model.CucumberFeature;
import cucumber.runtime.model.FeatureBuilder;
import cucumber.runtime.model.FeatureLoader;
import io.lippia.api.lowcode.internal.io.FileResourceLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import static io.lippia.api.lowcode.internal.URIParser.getFeaturePath;


public class CucumberInternal {

    private static final Logger log = LoggerFactory.getLogger(CucumberInternal.class);

    private static final FeatureBuilder featureBuilder = new FeatureBuilder();

    private CucumberInternal() {}

    private static Object getFileResourceLoader() {
        return new FileResourceLoader().load();
    }

    private static FeatureLoader getFeatureLoader() throws ClassNotFoundException, InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        return new FeatureLoader((ResourceLoader) getFileResourceLoader());
    }

    public static List<CucumberFeature> getCucumberFeatures(String featureName) {
        try {
            Method method = getFeatureLoader().getClass()
                    .getDeclaredMethod("loadFromFeaturePath", FeatureBuilder.class, URI.class);
            method.setAccessible(true);
            method.invoke(getFeatureLoader(), featureBuilder, getFeaturePath(featureName));
        } catch (NoSuchMethodException | IllegalAccessException |
                 InvocationTargetException | URISyntaxException |
                 ClassNotFoundException | InstantiationException e) {
            log.error(e.getMessage());
        }

        return featureBuilder.build();
    }

}
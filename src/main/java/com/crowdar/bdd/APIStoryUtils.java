package com.crowdar.bdd;

import java.util.Arrays;
import java.util.List;

import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;

/**
 * @author Agustin Mascheroni
 */
public class APIStoryUtils {

    /**
     * This method returns a list of stories located in the given path
     * 
     * @param String path
     * @return List<String>
     * 
     * @author Agustin Mascheroni
     */
    public List<String> storyPaths(String path) {
        StoryFinder finder = new StoryFinder();
        String codeLocation = CodeLocations.codeLocationFromClass(this.getClass()).getFile();
        String testLocation = codeLocation.replaceAll("classes", "test-classes");
        return finder.findPaths(testLocation, Arrays.asList(path), Arrays.asList(""));
    }
}
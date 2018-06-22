package com.crowdar.bdd.jbehave;

import java.util.Arrays;
import java.util.List;

import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;

/**
 * @author Agustin Mascheroni
 */
public class StoryUtils {

	/**
	 * Este metodo devuelve una lista de historias
	 * segun el path dado
	 * 
	 * @param String path (ubicacion de las historias)
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
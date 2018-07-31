package com.crowdar.bdd;

import java.io.File;
import java.util.List;

import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.StoryFinder;
import org.testng.collections.Lists;

/**
 * @author Agustin Mascheroni
 */
public abstract class StoryUtils {

	/**
	 * Este metodo devuelve una lista de historias
	 * segun el path dado
	 *
	 * @param incompletePaths<String>  path (ubicacion de las historias)
	 * @return List<String>
	 *
	 * @author Agustin Mascheroni
	 */
	public static List<String> storyPaths(List<String> incompletePaths) {
		List<String> completePaths= Lists.newArrayList();
		for (String incompletePath : incompletePaths) {
			completePaths.addAll(getPath(incompletePath));
		}

		return completePaths;
	}

	private static List<String> getPath(String path){
		StoryFinder finder = new StoryFinder();
		String codeLocation = CodeLocations.codeLocationFromPath(System.getProperty("user.dir") + File.separator + "target" + File.separator + "classes").getFile();
		String testLocation = codeLocation.replaceAll("classes", "test-classes");
		return finder.findPaths(testLocation, path, "");
	}

}
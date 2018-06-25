package com.crowdar.bdd;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jbehave.core.io.ResourceLoader;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.model.ExamplesTableFactory;

import com.crowdar.core.Context;
import com.crowdar.core.MyThreadLocal;

public class CustomExampleTableFactory extends ExamplesTableFactory {
	
	private final String EXAMPLES_TABLES_DIR = "examplesTables"; 
	
	
	public CustomExampleTableFactory(ResourceLoader resourceLoader) {
		super(resourceLoader);
	}

	@Override
	public ExamplesTable createExamplesTable(String input) {
		
		if (input.isEmpty()) {
			String exampleTableFileToLoad="";;
			try {
				exampleTableFileToLoad = getExamplesTableFileNameClass();
				input = readExamplesTableFile(Paths.get(ClassLoader.getSystemResource(exampleTableFileToLoad).toURI()));
			} catch (Exception e) {
				if(!exampleTableFileToLoad.isEmpty())
					System.out.println("Error loading Examples Table File: ".concat(exampleTableFileToLoad));
			}
		}
		return super.createExamplesTable(input);
	}
	
	private String readExamplesTableFile(Path examplesTableFilePath) throws IOException{
		return new String(Files.readAllBytes(examplesTableFilePath));
	}

	/**
	 * ExampleTable by testNg  "TESTNAME.table" 
	 * @return file location
	 */
	private String getExamplesTableFileNameClass(){
		String testName =(String)MyThreadLocal.get().getData(Context.CONTEXT_TEST_NAME_KEY); 
		return this.EXAMPLES_TABLES_DIR.concat(File.separator).concat(testName).concat(".table");
	}
	
//ExampleTable by "CLASS_METHOD.table"
//	private String getExamplesTableFileNameMethod(){
//		String className =(String)MyThreadLocal.get().getData(GUIStoryRunner.INVOKER_TEST_CLASS_NAME); 
//		String methodName =(String)MyThreadLocal.get().getData(GUIStoryRunner.INVOKER_TEST_METHOD_NAME); 
//		 return this.EXAMPLES_TABLES_DIR.concat(File.separator).concat(className).concat("_").concat(methodName).concat(".table");
//	}

//	ExampleTable by "CLASS.table"
//	private String getExamplesTableFileNameClass(){
//		String className =(String)MyThreadLocal.get().getData(GUIStoryRunner.INVOKER_TEST_CLASS_NAME); 
//		return this.EXAMPLES_TABLES_DIR.concat(File.separator).concat(className).concat(".table");
//	}

}

package com.crowdar.bdd;

import java.io.File;
import java.text.SimpleDateFormat;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.embedder.EmbedderControls;
import org.jbehave.core.embedder.StoryControls;
import org.jbehave.core.io.CodeLocations;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.parsers.RegexStoryParser;
import org.jbehave.core.reporters.CrossReference;
import org.jbehave.core.reporters.Format;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.ParameterConverters;
import org.jbehave.core.steps.ParameterConverters.DateConverter;
import org.jbehave.core.steps.SilentStepMonitor;

/**
 * @author Agustin Mascheroni
 */
public class EmbedderBase extends Embedder {

	/**
	 * This method overrides the embedderControls method, in order to
	 * manipulate the different timeouts and configuring the failure policy
	 * 
	 * @author Agustin Mascheroni
	 */
	@Override
    public EmbedderControls embedderControls() {
        return new EmbedderControls().doIgnoreFailureInStories(false).doIgnoreFailureInView(true)
        		.useStoryTimeouts("5000");
    }
	
	/**
	 * This method overrides the configuration method, in order to
	 * create new configurations
	 * 
	 * @author Agustin Mascheroni
	 */
	@Override
    public  Configuration configuration() {
		
		Class<? extends EmbedderBase> embedderClass = this.getClass();
		
        return new MostUsefulConfiguration()
        	.useStoryControls(new StoryControls().doResetStateBeforeScenario(false))
        	.useStoryLoader(new LoadFromClasspath(embedderClass.getClassLoader()))
        	.useStoryReporterBuilder(new StoryReporterBuilder()
                .withCodeLocation(CodeLocations.codeLocationFromClass(embedderClass))
                .withFormats(Format.STATS, Format.CONSOLE, Format.TXT, CustomHTMLReport.WEB_DRIVER_HTML).withFailureTrace(true)
				.withFailureTraceCompression(true)
                .withCrossReference(new CrossReference())
                .withRelativeDirectory(
            		"..".concat(File.separator)
            		.concat("reports").concat(File.separator)
            		.concat(System.getProperty("runInstance")).concat(File.separator)
            		.concat("jbehave").concat(File.separator)
            		.concat("view")))

//Examples parameters        	
        	.useStoryParser(new RegexStoryParser(new CustomExampleTableFactory(new LoadFromClasspath(this.getClass()))))
            .useParameterConverters(new ParameterConverters()
                    .addConverters(new DateConverter(new SimpleDateFormat("yyyy-MM-dd")))) // use custom date pattern

            .useStepMonitor(new SilentStepMonitor());
    }
}

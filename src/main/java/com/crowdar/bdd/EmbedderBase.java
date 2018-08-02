package com.crowdar.bdd;

import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;

import com.crowdar.core.PropertyManager;
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

import com.crowdar.report.ReportManager;

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
				.useStoryReporterBuilder(getStoryBuilder(embedderClass))
				.useStoryParser(new RegexStoryParser(new CustomExampleTableFactory(new LoadFromClasspath(this.getClass()))))
				.useParameterConverters(new ParameterConverters()
						.addConverters(new DateConverter(new SimpleDateFormat("yyyy-MM-dd")))) // use custom date pattern
				.useStepMonitor(new SilentStepMonitor());
	}

	public StoryReporterBuilder getStoryBuilder(Class<? extends EmbedderBase> embedderClass){

		return new StoryReporterBuilder()
				.withCodeLocation(CodeLocations.codeLocationFromPath(ReportManager.getReportPath()))
				.withFormats(Format.STATS, Format.CONSOLE, Format.TXT, CustomHTMLReport.WEB_DRIVER_HTML).withFailureTrace(true)
				.withFailureTraceCompression(true)
				.withCrossReference(new CrossReference())
				.withRelativeDirectory(ReportManager.getReportDirName()+File.separator+"jbehave"+File.separator+"view");
	}



}

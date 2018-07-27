package com.crowdar.bdd;

import java.io.PrintStream;
import java.util.Properties;

import com.crowdar.core.ReportManager;
import com.crowdar.core.ScreenshotCapture;
import org.jbehave.core.configuration.Keywords;
import org.jbehave.core.reporters.FilePrintStreamFactory;
import org.jbehave.core.reporters.HtmlOutput;
import org.jbehave.core.reporters.StoryReporter;
import org.jbehave.core.reporters.StoryReporterBuilder;

/**
 * This variation to HtmlOutput inlines a screenshot of the failure taken by WebDriver.
 */
public class CustomHTMLReport extends HtmlOutput {

    public static final org.jbehave.core.reporters.Format WEB_DRIVER_HTML = new WebDriverHtmlFormat();

    public CustomHTMLReport(PrintStream output) {
        super(output);
        changeALine();
    }

    public CustomHTMLReport(PrintStream output, Properties outputPatterns) {
        super(output, outputPatterns);
        changeALine();
    }

    public CustomHTMLReport(PrintStream output, Keywords keywords) {
        super(output, keywords);
        changeALine();
    }

    public CustomHTMLReport(PrintStream output, Properties outputPatterns, Keywords keywords) {
        super(output, outputPatterns, keywords);
        changeALine();
    }

    public CustomHTMLReport(PrintStream output, Properties outputPatterns, Keywords keywords, boolean reportFailureTrace) {
        super(output, outputPatterns, keywords, reportFailureTrace);
        changeALine();
    }

    private void changeALine() {

        String screenshotCaptureFile = ReportManager.getRelativeHtmlPath(ScreenshotCapture.getScreenCaptureFileName());

        super.overwritePattern("failed",
                "<div class=\"step failed\">{0} <span class=\"keyword failed\">({1})</span><br/><span class=\"message failed\">{2}</span>" +
                        "<br/><a color=\"black\" target=\"jb_scn_shot\" href=\"../../../" + screenshotCaptureFile +"\"><img src=\"../../../"+screenshotCaptureFile+"\" alt=\"failing screenshot\"/ height=\"120\" width=\"150\"></a></div>\n");
    }

    private static class WebDriverHtmlFormat extends org.jbehave.core.reporters.Format {

        public WebDriverHtmlFormat() {
            super("HTML");
        }

        @Override
        public StoryReporter createStoryReporter(FilePrintStreamFactory factory, StoryReporterBuilder storyReporterBuilder) {
            factory.useConfiguration(storyReporterBuilder.fileConfiguration("html"));
            return new CustomHTMLReport(factory.createPrintStream(), storyReporterBuilder.keywords())
                    .doReportFailureTrace(storyReporterBuilder.reportFailureTrace())
                    .doCompressFailureTrace(storyReporterBuilder.compressFailureTrace());
        }
    }
}

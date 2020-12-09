package io.lippia.web.reporter;

import io.lippia.reporter.cucumber4.adapter.ReportServerApiAdapter;

public class ReportServerApiReporter extends ReportServerApiAdapter{

	public ReportServerApiReporter(String arg) {
		super(arg);
	}

	@Override
	public String getBase64Image() {
		return  null;
	}

}

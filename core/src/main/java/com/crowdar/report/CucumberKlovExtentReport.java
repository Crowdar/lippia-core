package com.crowdar.report;

import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;
import cucumber.api.event.*;

public class CucumberKlovExtentReport extends ExtentCucumberAdapter {


    public CucumberKlovExtentReport(String arg) {
        super(arg);
    }
}

package com.crowdar.core;

import org.openqa.selenium.remote.RemoteWebDriver;

public class CucumberPageBase extends PageBase {

    protected Context context = Context.getInstance();

    public CucumberPageBase(RemoteWebDriver driver){
        super(driver);
    }
    public CucumberPageBase(){

    }
}

package com.crowdar.core.pageObjects;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.bdd.cukes.SharedDriver;
import com.crowdar.core.Context;

public abstract class CucumberPageBase extends PageBase {

    protected Context context = Context.getInstance();

    public CucumberPageBase() {
        super();
    }
    
    public CucumberPageBase(RemoteWebDriver driver) {
        super(driver);
    }

    public CucumberPageBase(SharedDriver driver) {
        super(driver.get());
    }
}

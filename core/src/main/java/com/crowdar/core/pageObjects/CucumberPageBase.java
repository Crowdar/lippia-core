package com.crowdar.core.pageObjects;

import com.crowdar.bdd.cukes.SharedDriver;
import com.crowdar.core.Context;
import org.openqa.selenium.remote.RemoteWebDriver;

public abstract class CucumberPageBase extends PageBase {

    protected Context context = Context.getInstance();

    public CucumberPageBase(RemoteWebDriver driver) {
        super(driver);
    }

    public CucumberPageBase(SharedDriver driver) {
        super(driver.get());
    }
}

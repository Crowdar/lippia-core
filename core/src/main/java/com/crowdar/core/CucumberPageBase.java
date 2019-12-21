package com.crowdar.core;

import com.crowdar.bdd.cukes.SharedDriver;
import org.openqa.selenium.remote.RemoteWebDriver;

public class CucumberPageBase extends PageBase {

    protected Context context = Context.getInstance();

    public CucumberPageBase(RemoteWebDriver driver) {
        super(driver);
    }

    public CucumberPageBase(SharedDriver driver) {
        super(driver.get());

    }
}

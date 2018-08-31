package com.crowdar.core;

import com.crowdar.bdd.cukes.SharedDriver;
import org.openqa.selenium.WebDriver;

public class CucumberPageBase extends PageBase {

    protected Context context = Context.getInstance();

    public CucumberPageBase(SharedDriver driver) {
        super((WebDriver)driver);
    }
    public CucumberPageBase(WebDriver driver){
        super(driver);
    }
    public CucumberPageBase(){

    }
}

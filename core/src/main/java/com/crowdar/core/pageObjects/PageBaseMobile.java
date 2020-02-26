package com.crowdar.core.pageObjects;

import com.crowdar.bdd.cukes.SharedDriver;
import com.crowdar.core.Utils;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

/**
 * This class represents the things in common between Mobile projects
 *
 * @author: Juan Manuel Spoleti
 */
public class PageBaseMobile extends CucumberPageBase {

    public PageBaseMobile(RemoteWebDriver driver) {
        super(driver);
    }

    public PageBaseMobile(SharedDriver driver) {
        super(driver);
    }

    /**
     * Completes the input field specific with a value specific and check if its empty, if its empty, clear the field
     * First: cleans the field if its not empty, Second: completes the field.
     *
     * @param element     to be completed
     * @param value       to write in the field
     * @param placeholder
     */
    public void completeField(WebElement element, String value, String placeholder) {
        if (!Utils.isTextFieldEmpty(element, placeholder)) {
            element.clear();
        }
        completeFieldWithoutClear(element, value);
    }

    public void completeField(By locator, String value, String placeholder) {
        WebElement element = getWebElement(locator);
        completeField(element, value, placeholder);
    }

    public void selectOptionSpinner(String option) {
        String uiSelector = "new UiSelector().textContains(\"" + option
                + "\")";

        String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView("
                + uiSelector + ");";

        WebElement element = driver.findElement(MobileBy.AndroidUIAutomator(command));

        this.clickElement(element);
    }

    public void scrollToElementByResourceId(String id) {
        String uiSelector = "new UiSelector().resourceId(\"" + id
                + "\")";

        String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView("
                + uiSelector + ");";

        driver.findElement(MobileBy.AndroidUIAutomator(command));
    }


    public void scrollToElementByAccessibilityId(String accessibilityId) {
        String uiSelector = "new UiSelector().description(\"" + accessibilityId
                + "\")";

        String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView("
                + uiSelector + ");";

        driver.findElement(MobileBy.AndroidUIAutomator(command));
    }

}
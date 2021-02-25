package com.crowdar.core.pageObjects;

import com.crowdar.core.Utils;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.HashMap;

/**
 * This class represents the things in common between Mobile projects
 *
 * @author: Juan Manuel Spoleti
 */
@Deprecated
public class PageBaseMobile extends CucumberPageBase {

    public PageBaseMobile(EventFiringWebDriver driver) {
        super(driver);
    }

    /**
     * Set the input field with a value specific and check if its empty, if true: clear the field
     * First: cleans the field if its not empty, Second: completes the field.
     * @deprecated use setInput(locatorName, value, placeholder)
     * @param element     to be completed
     * @param value       to write in the field
     * @param placeholder
     */
    @Deprecated
    public void completeField(WebElement element, String value, String placeholder) {
        if (!element.getText().equals(placeholder)) {
            element.clear();
        }
        completeFieldWithoutClear(element, value);
    }

    @Deprecated
    public void completeField(By locator, String value, String placeholder) {
        WebElement element = getWebElement(locator);
        completeField(element, value, placeholder);
    }

    public void setInput(String locatorName, String value, String placeholder){
        String elementText = getText(locatorName);
        boolean clear = !elementText.equals(placeholder);
        setInput(locatorName, value, true, clear);
    }

    public void clickOptionSpinner(String spinnerLocatorName, String option) {
        click(spinnerLocatorName);
        clickOptionSpinner(option);
    }

    public void clickOptionSpinner(String spinnerLocator, String option, String filterLocator, String filter) {
        click(spinnerLocator);
        setInput(filterLocator, filter, true, true);
        clickOptionSpinner(option);
    }

    public void clickOptionSpinner(String spinnerLocator, String option, String filterLocator) {
        clickOptionSpinner(spinnerLocator, option, filterLocator, option);
    }

    private void clickOptionSpinner(String option) {
        WebElement element = null;
        if (isAndroid()) {
            element = scrollAndroid("text", option, 0);

        } else if (isIos()) {
            element = getDriver().findElement(MobileBy.iOSNsPredicateString("label == '" + option + "'"));
            scrollIOS((IOSElement) element);
        }
       click(element);
    }

    private void clickOptionSpinner(String option, boolean startWith) {
       /* WebElement element = null;
        if (isAndroid()) {
            element = scrollAndroid("text", option, 0);

        } else if (isIos()) {
            element = getDriver().findElement(MobileBy.iOSNsPredicateString("label == '" + option + "'"));
            scrollIOS((IOSElement) element);
        }
        element.click();*/
    }

    private WebElement scrollAndroid(String locatorType, String locatorValue, int index) {
        String locator = String.format("new UiScrollable(new UiSelector().scrollable(true).instance(3)).scrollIntoView(new UiSelector().%s(\"%s\").instance(0).index(%d))", locatorType, locatorValue, index);
        return getDriver().findElement(MobileBy.AndroidUIAutomator(locator));
    }

    public void scrollIOS(String locatorName) {
        IOSElement element = (IOSElement) getElement(locatorName);
        scrollIOS(element);
    }

    private void scrollIOS(IOSElement element) {
        String elementID = element.getId();
        HashMap<String, String> scrollObject = new HashMap();
        scrollObject.put("element", elementID);
        scrollObject.put("direction", "down");
        driver.executeScript("mobile:scroll", scrollObject);
    }

    public boolean isAndroid() {
        return driver.getWrappedDriver() instanceof AndroidDriver;
    }

    public boolean isIos() {
        return driver.getWrappedDriver() instanceof IOSDriver;
    }
}
package com.crowdar.core.pageObjects;

import com.crowdar.core.Utils;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.HashMap;

/**
 * This class represents the things in common between Mobile projects
 *
 * @author: Juan Manuel Spoleti
 */
public class PageBaseMobile extends CucumberPageBase {

    public PageBaseMobile(RemoteWebDriver driver) {
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
        WebElement element = scrollAndroid("textContains", option, 0);
        this.clickElement(element);
    }

    private WebElement scrollAndroid(String locatorType, String locatorValue, int index){
        String locator = String.format("new UiScrollable(new UiSelector().scrollable(true).instance(3)).scrollIntoView(new UiSelector().%s(\"%s\").instance(0).index(%d))", locatorType, locatorValue, index);
        return ((AndroidDriver)driver).findElementByAndroidUIAutomator(locator);
    }

    public void scrollAndroidByResourceId(String id) {
        scrollAndroid("resourceId", id, 0);
    }

    public void scrollAndroidByText(String id) {
        scrollAndroid("text", id, 0);
    }

    public void scrollAndroidByAccessibilityId(String accessibilityId) {
        scrollAndroid("description", accessibilityId, 0);
    }

    public void scrollAndroidByClassName(String className, int index) {
        scrollAndroid("className", className, index);
    }

    public void scrollIOS(IOSElement element){
        String elementID = element.getId();
        HashMap<String, String> scrollObject = new HashMap();
        scrollObject.put("element", elementID);
        scrollObject.put("direction", "down");
        driver.executeScript("mobile:scroll", scrollObject);
    }
}
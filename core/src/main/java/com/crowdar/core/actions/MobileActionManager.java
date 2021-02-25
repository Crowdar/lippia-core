package com.crowdar.core.actions;

import com.crowdar.driver.DriverManager;
import io.appium.java_client.MobileBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.WebElement;

import java.util.HashMap;

/**
 * This class represents the things in common between Mobile projects
 *
 * @author: Juan Manuel Spoleti
 */
public class MobileActionManager extends ActionManager {

    public static void setInput(String locatorElement, String value, String placeholder) {
        String elementText = getText(locatorElement);
        boolean clear = !elementText.equals(placeholder);
        setInput(locatorElement, value, Boolean.TRUE, clear);
    }

    public static void clickOptionSpinner(String spinnerlocatorElement, String option) {
        click(spinnerlocatorElement);
        clickOptionSpinner(option, Boolean.FALSE);
    }

    public static void clickOptionSpinner(String spinnerlocatorElement, String option, boolean startWith) {
        click(spinnerlocatorElement);
        clickOptionSpinner(option, startWith);
    }

    public static void clickOptionSpinner(String spinnerLocator, String option, String filterLocator, String filter) {
        click(spinnerLocator);
        setInput(filterLocator, filter, Boolean.TRUE, Boolean.TRUE);
        clickOptionSpinner(option, Boolean.FALSE);
    }

    public static void clickOptionSpinner(String spinnerLocator, String option, String filterLocator, String filter, boolean startWith) {
        click(spinnerLocator);
        setInput(filterLocator, filter, Boolean.TRUE, Boolean.TRUE);
        clickOptionSpinner(option, startWith);
    }

    public static void clickOptionSpinner(String spinnerLocator, String option, String filterLocator) {
        clickOptionSpinner(spinnerLocator, option, filterLocator, option);
    }

    private static void clickOptionSpinner(String option, boolean startWith) {
        WebElement element = null;
        String findTypeAndroid = "text";
        String findTypeIos = "==";
        if(startWith){
            findTypeAndroid = "textContains";
            findTypeIos = "BEGINSWITH";
        }
        if (isAndroid()) {
            element = scrollAndroid(findTypeAndroid, option, 0);
        } else if (isIos()) {
            element =  DriverManager.getDriverInstance().findElement(MobileBy.iOSNsPredicateString("label " + findTypeIos + " '" + option + "'"));
            scrollIOS((IOSElement) element);
        }
        element.click();
    }

    private static WebElement scrollAndroid(String locatorType, String locatorValue, int index) {
        String locator = String.format("new UiScrollable(new UiSelector().scrollable(true).instance(3)).scrollIntoView(new UiSelector().%s(\"%s\").instance(0).index(%d))", locatorType, locatorValue, index);
        return DriverManager.getDriverInstance().findElement(MobileBy.AndroidUIAutomator(locator));
    }

    public static void scrollIOS(String locatorElement) {
        IOSElement element = (IOSElement) getElement(locatorElement);
        scrollIOS(element);
    }

    private static void scrollIOS(IOSElement element) {
        String elementID = element.getId();
        HashMap<String, String> scrollObject = new HashMap();
        scrollObject.put("element", elementID);
        scrollObject.put("direction", "down");
        DriverManager.getDriverInstance().executeScript("mobile:scroll", scrollObject);
    }

    public static boolean isAndroid() {
        return DriverManager.getDriverInstance().getWrappedDriver() instanceof AndroidDriver;
    }

    public static boolean isIos() {
        return DriverManager.getDriverInstance().getWrappedDriver() instanceof IOSDriver;
    }
}
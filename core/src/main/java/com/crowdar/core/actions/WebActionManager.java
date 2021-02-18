package com.crowdar.core.actions;

import com.crowdar.core.Constants;
import com.crowdar.driver.DriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/**
 * This class represents the things in common between Web projects
 *
 * @author: Juan Manuel Spoleti
 */
public class WebActionManager extends ActionManager {
    
    public static void click(String locatorElement, String ... locatorReplacementValue) {
    	click(locatorElement, true, locatorReplacementValue);
    }

    public static void click(String locatorElement, Boolean scroll, String ... locatorReplacementValue){
    	WebElement element = waitVisibility(locatorElement, locatorReplacementValue);
    	click(element, scroll);
    }

    protected static void click(WebElement element) {
        click(element, true);
    }

    protected static void click(WebElement element, Boolean scroll) {
    	if(scroll){
    		scroll(element);
    	}
    	ActionManager.click(element);
    }

    private static void scroll(WebElement element) {
        JavascriptExecutor jse = DriverManager.getDriverInstance();
        jse.executeScript("arguments[0].scrollIntoView()", element);
    }

    public static void maximizeWindow() {
        DriverManager.getDriverInstance().manage().window().maximize();
    }

    /**
     * Method is used to navigated to certain page
     *
     * @param url to go. Example: https://crowdar.com.ar
     */
    public static void navigateTo(String url) {
        DriverManager.getDriverInstance().get(url);
    }

    /**
     * switch to the last tab
     */
    public static void switchToLastTab() {
        String lastHandle = "";
        for (String winHandle : DriverManager.getDriverInstance().getWindowHandles()) {
            lastHandle = winHandle;
        }
        DriverManager.getDriverInstance().switchTo().window(lastHandle);
    }

    /**
     * Wait to appear more than one tab
     */
    public static void waitTabOpening() throws InterruptedException {
        int attempts = 0;
        WebDriver wd = DriverManager.getDriverInstance();
        long wait = Constants.getWaitScriptTimeout() * 2;
        while ((wd.getWindowHandles().size() == 1) && attempts <= wait) {
            Thread.sleep(500);
            attempts++;
        }
    }

    private static Select getSelect(WebElement element) {
        return new Select(element);
    }

    protected static void setDropdownByText(WebElement element, String text) {
        Select dropdown = getSelect(element);
        dropdown.selectByVisibleText(text);
    }

    public static void setDropdownByText(String locatorElement, String text, String ... locatorReplacementValue) {
        WebElement element = waitVisibility(locatorElement, locatorReplacementValue);
        setDropdownByText(element, text);
    }

    public static void setDropdownByValue(String locatorElement, String text, String ... locatorReplacementValue) {
        WebElement element = waitVisibility(locatorElement, locatorReplacementValue);
        setDropdownByValue(element, text);
    }

    protected static void setDropdownByValue(WebElement element, String value) {
        Select dropdown = getSelect(element);
        dropdown.selectByValue(value);
    }

    public static void setDropdownByVisibleText(String locatorElement, String text, String ... locatorReplacementValue) {
        WebElement element = waitVisibility(locatorElement, locatorReplacementValue);
        setDropdownByVisibleText(element, text);
    }

    protected static void setDropdownByVisibleText(WebElement element, String value) {
        Select dropdown = getSelect(element);
        dropdown.selectByVisibleText(value);
    }

    public static void setDropdownByIndex(String locatorElement, Integer index, String ... locatorReplacementValue) {
        WebElement element = waitVisibility(locatorElement, locatorReplacementValue);
        setDropdownByIndex(element, index);
    }

    protected static void setDropdownByIndex(WebElement element, Integer index) {
        Select dropdown = getSelect(element);
        dropdown.selectByIndex(index);
    }

    public static WebElement getDropdownSelectedOption(String locatorElement, String ... locatorReplacementValue) {
        WebElement element = waitVisibility(locatorElement, locatorReplacementValue);
        return getDropdownSelectedOption(element);
    }

    protected static WebElement getDropdownSelectedOption(WebElement element) {
        Select dropdown = getSelect(element);
        return dropdown.getFirstSelectedOption();
    }

    public static List<WebElement> getDropdownAllSelectedOptions(String locatorElement, String ... locatorReplacementValue) {
        WebElement element = waitVisibility(locatorElement, locatorReplacementValue);
        return getDropdownAllSelectedOptions(element);
    }

    protected static List<WebElement> getDropdownAllSelectedOptions(WebElement element) {
        Select dropdown = getSelect(element);
        return dropdown.getAllSelectedOptions();
    }

    protected static void deselectDropdownAll(WebElement element) {
        Select dropdown = getSelect(element);
        dropdown.deselectAll();
    }

    public static void deselectDropdownAll(String locatorElement, String ... locatorReplacementValue) {
        WebElement element = waitVisibility(locatorElement, locatorReplacementValue);
        deselectDropdownAll(element);
    }

    protected static void deselectDropdownByValue(WebElement element, String value) {
        Select dropdown = getSelect(element);
        dropdown.deselectByValue(value);
    }

    public static void deselectDropdownByValue(String locatorElement, String value, String ... locatorReplacementValue) {
        WebElement element = waitVisibility(locatorElement, locatorReplacementValue);
        deselectDropdownByValue(element, value);
    }

    protected static void deselectDropdownByText(WebElement element, String text) {
        Select dropdown = getSelect(element);
        dropdown.deselectByValue(text);
    }

    public static void deselectDropdownByText(String locatorElement, String value, String ... locatorReplacementValue) {
        WebElement element = waitVisibility(locatorElement, locatorReplacementValue);
        deselectDropdownByText(element, value);
    }

    protected static void deselectDropdownByIndex(WebElement element, int index) {
        Select dropdown = getSelect(element);
        dropdown.deselectByIndex(index);
    }

    public static void deselectDropdownByIndex(String locatorElement, int index, String ... locatorReplacementValue) {
        WebElement element = waitVisibility(locatorElement, locatorReplacementValue);
        deselectDropdownByIndex(element, index);
    }

    /**
     * Method that drag and drop some element over other element
     *
     * @param elementToDrag
     * @param elementToReplace
     */
    protected static void dragAndDrop(WebElement elementToDrag, WebElement elementToReplace) {
        JavascriptExecutor jse = DriverManager.getDriverInstance();
        jse.executeScript("arguments[0].scrollIntoView()", elementToReplace);

        new Actions(DriverManager.getDriverInstance()).dragAndDrop(elementToDrag, elementToReplace).perform();
    }

    public static void dragAndDrop(String locatorToDrag, String locatorToReplace) {
        WebElement elementToDrag = waitVisibility(locatorToDrag);
        WebElement elementToReplace = waitVisibility(locatorToReplace);
        dragAndDrop(elementToDrag, elementToReplace);
    }
}
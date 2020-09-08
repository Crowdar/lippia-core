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
    /**
     * This is the Base url for all system to be tested
     */
    public static String BASE_URL;
    /**
     * This is the url that correspond to this child page and should be initialized in  child constructor child page
     */
    protected static String url;

    public static void click(String locatorName) {
        click(locatorName, true);
    }

    protected static void click(WebElement element) {
        click(element, true);
    }

    protected static void click(WebElement element, boolean scroll) {
        if(scroll){
            scroll(element);
        }
        ActionManager.click(element);
    }

    public static void click(String locatorName, boolean scroll){
        WebElement element = waitVisibility(locatorName);
        click(element, scroll);
    }

    private static void scroll(WebElement element) {
        JavascriptExecutor jse = DriverManager.getDriverInstance();
        jse.executeScript("arguments[0].scrollIntoView()", element);
    }

    public static void maximizeWindow() {
        DriverManager.getDriverInstance().manage().window().maximize();
    }

    /**
     * Method that returns the complet url to  the page
     * BASE_URL + url
     *
     * @return String complete
     */
    public static String getCompleteURL() {
        return BASE_URL + url;
    }

    /**
     * Method is used to navigated to page complete url
     * BASE_URL + url
     */
    public static void navigateToCompleteURL() {
        DriverManager.getDriverInstance().get(getCompleteURL());
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

    public static void setDropdownByText(String locatorName, String text) {
        WebElement element = waitVisibility(locatorName);
        setDropdownByText(element, text);
    }

    public static void setDropdownByValue(String locatorName, String text) {
        WebElement element = waitVisibility(locatorName);
        setDropdownByValue(element, text);
    }

    protected static void setDropdownByValue(WebElement element, String value) {
        Select dropdown = getSelect(element);
        dropdown.selectByValue(value);
    }

    public static void setDropdownByVisibleText(String locatorName, String text) {
        WebElement element = waitVisibility(locatorName);
        setDropdownByVisibleText(element, text);
    }

    protected static void setDropdownByVisibleText(WebElement element, String value) {
        Select dropdown = getSelect(element);
        dropdown.selectByVisibleText(value);
    }

    public static void setDropdownByIndex(String locatorName, int index) {
        WebElement element = waitVisibility(locatorName);
        setDropdownByIndex(element, index);
    }

    protected static void setDropdownByIndex(WebElement element, int index) {
        Select dropdown = getSelect(element);
        dropdown.selectByIndex(index);
    }

    public static WebElement getDropdownSelectedOption(String locatorName) {
        WebElement element = waitVisibility(locatorName);
        return getDropdownSelectedOption(element);
    }

    protected static WebElement getDropdownSelectedOption(WebElement element) {
        Select dropdown = getSelect(element);
        return dropdown.getFirstSelectedOption();
    }

    public static List<WebElement> getDropdownAllSelectedOptions(String locatorName) {
        WebElement element = waitVisibility(locatorName);
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

    public static void deselectDropdownAll(String locatorName) {
        WebElement element = waitVisibility(locatorName);
        deselectDropdownAll(element);
    }

    protected static void deselectDropdownByValue(WebElement element, String value) {
        Select dropdown = getSelect(element);
        dropdown.deselectByValue(value);
    }

    public static void deselectDropdownByValue(String locatorName, String value) {
        WebElement element = waitVisibility(locatorName);
        deselectDropdownByValue(element, value);
    }

    protected static void deselectDropdownByText(WebElement element, String text) {
        Select dropdown = getSelect(element);
        dropdown.deselectByValue(text);
    }

    public static void deselectDropdownByText(String locatorName, String value) {
        WebElement element = waitVisibility(locatorName);
        deselectDropdownByText(element, value);
    }

    protected static void deselectDropdownByIndex(WebElement element, int index) {
        Select dropdown = getSelect(element);
        dropdown.deselectByIndex(index);
    }

    public static void deselectDropdownByIndex(String locatorName, int index) {
        WebElement element = waitVisibility(locatorName);
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
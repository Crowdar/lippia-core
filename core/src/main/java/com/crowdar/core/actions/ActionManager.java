package com.crowdar.core.actions;

import com.crowdar.core.Constants;
import com.crowdar.core.LocatorManager;
import com.crowdar.driver.DriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class ActionManager {

    private static WebDriverWait wait;
    private static FluentWait<RemoteWebDriver> fluentWait;

    /**
     * Method that returns the default wait in our framework
     *
     * @return web driver wait
     */
    public static WebDriverWait getWait() {
        if (wait == null) {
            wait = new WebDriverWait(DriverManager.getDriverInstance(), Constants.getWaitForElementTimeout());
        }
        return wait;
    }

    /**
     * Method that returns the default fluent wait in our framework
     *
     * @return wait
     */
    public static Wait<RemoteWebDriver> getFluentWait() {
        if (fluentWait == null) {
            fluentWait = new FluentWait<>(DriverManager.getDriverInstance()).withTimeout(Duration.ofSeconds(Constants.getFluentWaitTimeoutInSeconds()))
                    .pollingEvery(Duration.ofMillis(Constants.getFluentWaitRequestFrequencyInMillis())).ignoring(NoSuchElementException.class);
        }
        return fluentWait;
    }

    /**
     * Click the element provided by the locator name
     *
     * @param locatorName
     */
    public static void click(String locatorName) {
        WebElement element = waitClickable(locatorName);
        click(element);
    }

    protected static void click(WebElement element) {
        element.click();
    }

    /**
     * Set element input with a value provided by the locator name
     * Default: not click and not clear the input element
     *
     * @param locatorName
     * @param value
     */
    public static void setInput(String locatorName, String value) {
        setInput(locatorName, value, false, false);
    }

    /**
     * Set element input with a value provided by the locator name
     *
     * @param locatorName
     * @param value
     * @param clickAndClear true: click and clear the input element, false: don't click and don't clear the input element
     */
    public static void setInput(String locatorName, String value, boolean clickAndClear) {
        setInput(locatorName, value, clickAndClear, clickAndClear);
    }

    /**
     * Set element input with a value provided by the locator name
     *
     * @param locatorName
     * @param value
     * @param click       true: click the input element, false: don't click the input element
     * @param clear       true: clear the input element, false: don't clear the input element
     */
    public static void setInput(String locatorName, String value, boolean click, boolean clear) {
        WebElement element = waitVisibility(locatorName);
        setInput(element, value, click, clear);
    }

    protected static void setInput(WebElement element, String value, boolean click, boolean clear) {
        if (click) {
            element.click();
        }
        if (clear) {
            element.clear();
        }
        element.sendKeys(value);
    }

    /**
     * Returns element text
     *
     * @param locatorName
     * @return element text
     */
    public static String getText(String locatorName) {
        WebElement element = waitPresence(locatorName);
        return element.getText();
    }

    /**
     * Returns element attribute selected
     *
     * @param locatorName
     * @param attribute
     * @return attribute value
     */
    public static String getAttribute(String locatorName, String attribute) {
        WebElement element = waitPresence(locatorName);
        return element.getAttribute(attribute);
    }

    private static WebElement getElement(By locator) {
        return DriverManager.getDriverInstance().findElement(locator);
    }

    public static List<WebElement> getElements(String locatorName) {
        By locator = LocatorManager.getLocator(locatorName);
        return getElements(locator);
    }

    private static List<WebElement> getElements(By locator) {
        return DriverManager.getDriverInstance().findElements(locator);
    }

    private static WebElement getElement(WebElement parent, By locator) {
        return parent.findElement(locator);
    }

    private static List<WebElement> getElements(WebElement parent, By locator) {
        return parent.findElements(locator);
    }

    /**
     * Return WebElement with the locator name provided
     *
     * @param locatorName
     * @return
     */
    public static WebElement getElement(String locatorName) {
        By locator = LocatorManager.getLocator(locatorName);
        return getElement(locator);
    }

    public static WebElement getElement(String locatorParent, String locatorName) {
        By locator = LocatorManager.getLocator(locatorName);
        WebElement parent = getElement(locatorParent);
        return getElement(parent, locator);
    }

    public static List<WebElement> getElements(String locatorParent, String locatorName) {
        By locator = LocatorManager.getLocator(locatorName);
        WebElement parent = getElement(locatorParent);
        return getElements(parent, locator);
    }

    /**
     * Wait until the element is visible
     *
     * @param locatorName
     * @return web element
     */
    public static WebElement waitVisibility(String locatorName) {
        By locator = LocatorManager.getLocator(locatorName);
        return getFluentWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public static List<WebElement> waitVisibilities(String locatorName) {
        By locator = LocatorManager.getLocator(locatorName);
        return getFluentWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Wait until the element is present
     *
     * @param locatorName
     * @return web element
     */
    public static WebElement waitPresence(String locatorName) {
        By locator = LocatorManager.getLocator(locatorName);
        return getFluentWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public static List<WebElement> waitPresences(String locatorName) {
        By locator = LocatorManager.getLocator(locatorName);
        return getFluentWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * Wait until the element is clickable
     *
     * @param locatorName
     * @return web element
     */
    public static WebElement waitClickable(String locatorName) {
        By locator = LocatorManager.getLocator(locatorName);
        return getFluentWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait until the element is invisible
     *
     * @param locatorName
     */
    public static void waitInvisibility(String locatorName) {
        By locator = LocatorManager.getLocator(locatorName);
        getFluentWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public static void waitInvisibilities(String locatorName) {
        List<WebElement> elements = getElements(locatorName);
        getFluentWait().until(ExpectedConditions.invisibilityOfAllElements(elements));
    }

    public static boolean isVisible(String locatorName) {
        return getElement(locatorName).isDisplayed();
    }

    public static boolean isEnabled(String locatorName) {
        return getElement(locatorName).isEnabled();
    }

    public static boolean isSelected(String locatorName) {
        return getElement(locatorName).isSelected();
    }

    /**
     * Method that verifies if the locator specific is present
     *
     * @param locatorName
     * @return
     */
    public static boolean isPresent(String locatorName) {
        DriverManager.getDriverInstance().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            getElement(locatorName);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } finally {
            DriverManager.getDriverInstance().manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
        }
    }

    /**
     * Select or deselect checkbox
     *
     * @param locatorName
     * @param check
     */
    public static void setCheckbox(String locatorName, boolean check) {
        WebElement checkbox = waitClickable(locatorName);
        boolean isSelected = checkbox.isSelected();
        if (isSelected && !check) {
            checkbox.click();
        } else if (isSelected && check) {
            checkbox.click();
        }
    }
}

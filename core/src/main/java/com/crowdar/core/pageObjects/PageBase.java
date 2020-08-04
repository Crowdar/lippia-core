package com.crowdar.core.pageObjects;

import com.crowdar.core.Constants;
import com.crowdar.driver.DriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * This class represents the things in common between Windows, Web and Mobile projects
 *
 * @author: Juan Manuel Spoleti
 */
abstract public class PageBase {
    /**
     * This is the Base url for all system to be tested
     */
    public static String BASE_URL;
    /**
     * This is the url that corespond to this child page and should be initialized in  child contructor child page
     */
    protected String url;

    protected RemoteWebDriver driver;
    protected WebDriverWait wait;
    protected FluentWait<RemoteWebDriver> fluentWait;

    protected Logger logger;

    public PageBase() {
    }

    public PageBase(RemoteWebDriver driver) {
      logger = Logger.getLogger(this.getClass());
      initialize(driver);
    }
    
    private void initialize(RemoteWebDriver driver) {
    	this.driver = driver;
        this.wait = new WebDriverWait(driver, Constants.getWaitForElementTimeout());
        this.fluentWait = new FluentWait<>(driver).withTimeout(Duration.ofSeconds(Constants.getWaitForElementTimeout()))
                .pollingEvery(Duration.ofMillis(10)).ignoring(NoSuchElementException.class);
    }

    /**
     * Method that returns the web driver
     *
     * @return web driver
     */
    public RemoteWebDriver getDriver() {
    	
    	if(driver == null) {
    		initialize(DriverManager.getDriverInstance());
    	}
    	return driver;
    }

    /**
     * Method that returns the default wait in our framework
     *
     * @return web driver wait
     */
    public WebDriverWait getWait() {
        return wait;
    }

    /**
     * Method that returns the default fluent wait in our framework
     *
     * @return wait
     */
    public Wait<RemoteWebDriver> getFluentWait() {
        return fluentWait;
    }

    /**
     * Method that obtains the element specific
     *
     * @param locator of the element; could be by xpath, id, name, etc
     * @return web element
     */
    public WebElement getWebElement(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> getWebElements(By locator) {
        return getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * Method that clicks the element specific
     *
     * @param locator of the element to be clickable
     */
    public void clickElement(By locator) {
        WebElement element = getWebElement(locator);
        clickElement(element);
    }

    /**
     * Method that clicks the element specific
     *
     * @param element to be clickable
     */
    public void clickElement(WebElement element) {
        element.click();
    }

    /**
     * Method that completes the input field specific with a value specific
     * First: obtains the element, Second: clean the field, Third: complete the
     * field.
     *
     * @param locator of the element to be completed
     * @param value   that i want to write in the field
     */
    public void completeField(By locator, String value) {
        WebElement element = getWebElement(locator);
        completeField(element, value);
    }

    public void completeField(WebElement element, String value) {
        clickElement(element);
        element.clear();
        completeFieldWithoutClick(element, value);
    }

    /**
     * Complete field without doing clear of the element.
     * Only clicks the element and complete the field with the value.
     *
     * @param locator of the element to be completed
     * @param value   that i want to write in the field
     */
    public void completeFieldWithoutClear(By locator, String value) {
        WebElement element = getWebElement(locator);
        clickElement(element);
        completeFieldWithoutClick(element, value);
    }

    /**
     * Complete field without clicking it.
     * Only clear the element and complete the field with the value.
     *
     * @param locator of the element to be completed
     * @param value   that i want to write in the field
     */
    public void completeFieldWithoutClick(By locator, String value) {
        WebElement element = getWebElement(locator);
        completeFieldWithoutClick(element, value);
    }

    public void completeFieldWithoutClear(WebElement element, String value) {
        clickElement(element);
        element.sendKeys(value);
    }

    public void completeFieldWithoutClick(WebElement element, String value) {
        element.clear();
        element.sendKeys(value);
    }

    /**
     * Method that get the text of a element.
     *
     * @param locator of the element to be completed
     */
    public String getElementText(By locator) {
        WebElement element = getWebElement(locator);
        return getElementText(element);
    }

    public String getElementText(WebElement element) {
        return element.getText();
    }

    /**
     * Method that get the attribute 'value' of a element, usually an input.
     *
     * @param locator of the element to be completed
     */
    public String getInputValue(By locator) {
        WebElement element = getWebElement(locator);
        return getInputValue(element);
    }

    public String getInputValue(WebElement element) {
        return element.getAttribute("value");
    }

    /**
     * Method that checks the option specific if it is not selected
     *
     * @param locator of the checkbox
     */
    public void selectCheckbox(By locator) {
        WebElement checkbox = driver.findElement(locator);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
    }

    /**
     * Method that un checks the option specific if it is not unselected
     *
     * @param locator of the checkbox
     */
    public void deselectCheckbox(By locator) {
        WebElement checkbox = driver.findElement(locator);
        if (checkbox.isSelected()) {
            checkbox.click();
        }
    }

    /**
     * Method that verifies if the element specific is present in the window
     *
     * @param locator of the element specific
     * @return true if the element is present, false otherwise
     */
    public boolean isElementPresent(By locator) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            getDriver().findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
        }
    }

    public boolean isElementVisible(WebElement element) {
        return element.isDisplayed();
    }

    public boolean isElementVisible(By locator) {
        return isElementVisible(getWebElement(locator));
    }

    /**
     * Method that verifies if the element specific is present in the window
     *
     * @param locator of the element specific
     * @return true if the element is present, false otherwise
     */
    public boolean waitAndCheckElementPresent(By locator) {
        try {
            getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    /**
     * Method that verifies if the locator specific is present in the element
     *
     * @param element
     * @param locator
     * @return true if is present, false otherwise
     */
    public boolean isElementPresent(WebElement element, By locator) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            element.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
        }
    }

    /**
     * Method that verifies if the input is not empty
     *
     * @param inputLocator
     * @return true if the input is empty, false otherwise
     */
    public boolean isInputElementEmpty(By inputLocator) {
        return isInputElementEmpty(getWebElement(inputLocator));
    }

    public boolean isInputElementEmpty(WebElement element) {
        return element.getAttribute("value").isEmpty();
    }

    /**
     * Method that verifies if the element is not empty
     *
     * @param locator
     * @return true if the element is empty, false otherwise
     */
    public boolean isElementEmpty(By locator) {
        return isElementEmpty(getWebElement(locator));
    }

    public boolean isElementEmpty(WebElement element) {
        return element.getText().isEmpty();
    }

    /**
     * Wait until an element disappear
     */
    public void waitForElementDisappears(By locator) {
        getFluentWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait until an element is visible
     */
    public void waitForElementVisibility(By locator) {
        getFluentWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait until an element is not visible
     */
    public void waitForElementInvisibility(By locator) {
        getFluentWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait until an element is clickable
     */
    public void waitForElementClickable(By locator) {
        getFluentWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait until an element is presence
     */
    public void waitForElementPresence(By locator) {
        getFluentWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Put a text 'value' on the clipboard
     *
     * @param value String of text that is required put on th clipboard
     */
    public void setTextToClipboard(String value) {
        StringSelection stringSelection = new StringSelection(value);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    /**
     * Wait to element dissappear
     *
     * @param locator By element that it has to disappear
     */
    public void waitUntilElementDissappear(By locator) {
        if (isElementPresent(locator)) {
            getWait().until((Function<? super WebDriver, ? extends Object>) ExpectedConditions.invisibilityOfElementLocated(locator));
        }
    }

    public boolean isElementEnabled(WebElement element) {
        return element.isEnabled();
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            getLogger().error("Error in sleep: ".concat(e.getMessage()));
            e.printStackTrace();
        }
    }

    public Logger getLogger() {
        return logger;
    }
}
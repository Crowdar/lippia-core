package com.crowdar.core.pageObjects;

import com.crowdar.core.Constants;
import com.crowdar.core.LocatorManager;
import com.crowdar.driver.DriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

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
     * This is the url that correspond to this child page and should be initialized in  child constructor child page
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
        this.loadLocators();
    }

    private void loadLocators() {
        LocatorManager.loadProperties(getLocatorClass().getName());
    }

    private By getLocator(String locatorName) {
        Class locatorClass = getLocatorClass();
        String[] locatorProperty = new String[2];
        locatorProperty = getLocatorProperty(locatorName, locatorClass, locatorProperty);
        return getLocatorInEnum(locatorProperty);

    }

    private Class<? extends PageBase> getLocatorClass() {
        return this.getClass().asSubclass(this.getClass());
    }

    private String[] getLocatorProperty(String locatorName, Class locatorClass, String[] locatorProperty) {
        try {
            locatorProperty = LocatorManager.getProperty(locatorName, locatorClass.getName()).split(":");
        } catch (NullPointerException e) {
            Logger.getRootLogger().error(e.getMessage());
            Assert.fail(String.format("Locator property %s was not found in: %s", locatorName, locatorClass.getSimpleName()));
        }
        return locatorProperty;
    }

    private By getLocatorInEnum(String[] locatorProperty) {
        String type = null;
        String value = null;
        try {
            type = locatorProperty[0].toUpperCase();
            value = locatorProperty[1];
        } catch (IndexOutOfBoundsException e) {
            Logger.getRootLogger().error(e.getMessage());
            Assert.fail("Locator property format is invalid. Example: css:#loginButton");
        }
        return LocatorTypesEnum.get(type).getLocator(value);
    }

    /**
     * Click the element provided by the locator name
     *
     * @param locatorName
     */
    public void click(String locatorName) {
        WebElement element = waitClickable(locatorName);
        click(element);
    }

    protected void click(WebElement element) {
        element.click();
    }

    /**
     * Set element input with a value provided by the locator name
     * Default: not click and not clear the input element
     *
     * @param locatorName
     * @param value
     */
    public void setInput(String locatorName, String value) {
        setInput(locatorName, value, false, false);
    }

    /**
     * Set element input with a value provided by the locator name
     *
     * @param locatorName
     * @param value
     * @param clickAndClear true: click and clear the input element, false: don't click and don't clear the input element
     */
    public void setInput(String locatorName, String value, boolean clickAndClear) {
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
    public void setInput(String locatorName, String value, boolean click, boolean clear) {
        WebElement element = waitVisibility(locatorName);
        setInput(element, value, click, clear);
    }

    protected void setInput(WebElement element, String value, boolean click, boolean clear) {
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
    public String getText(String locatorName) {
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
    public String getAttribute(String locatorName, String attribute) {
        WebElement element = waitPresence(locatorName);
        return element.getAttribute(attribute);
    }

    private WebElement getElement(By locator) {
        return getDriver().findElement(locator);
    }

    private List<WebElement> getElements(By locator) {
        return getDriver().findElements(locator);
    }

    /**
     * Return WebElement with the locator name provided
     *
     * @param locatorName
     * @return
     */
    public WebElement getElement(String locatorName) {
        By locator = getLocator(locatorName);
        return getElement(locator);
    }

    public List<WebElement> getElements(String locatorName) {
        By locator = getLocator(locatorName);
        return getElements(locator);
    }

    /**
     * Wait until the element is visible
     *
     * @param locatorName
     * @return web element
     */
    public WebElement waitVisibility(String locatorName) {
        By locator = getLocator(locatorName);
        return getFluentWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public List<WebElement> waitVisibilities(String locatorName) {
        By locator = getLocator(locatorName);
        return getFluentWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
    }

    /**
     * Wait until the element is present
     *
     * @param locatorName
     * @return web element
     */
    public WebElement waitPresence(String locatorName) {
        By locator = getLocator(locatorName);
        return getFluentWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    public List<WebElement> waitPresences(String locatorName) {
        By locator = getLocator(locatorName);
        return getFluentWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * Wait until the element is clickable
     *
     * @param locatorName
     * @return web element
     */
    public WebElement waitClickable(String locatorName) {
        By locator = getLocator(locatorName);
        return getFluentWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait until the element is invisible
     *
     * @param locatorName
     */
    public void waitInvisibility(String locatorName) {
        By locator = getLocator(locatorName);
        getFluentWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    public void waitInvisibilities(String locatorName) {
        List<WebElement> elements = getElements(locatorName);
        getFluentWait().until(ExpectedConditions.invisibilityOfAllElements(elements));
    }

    public boolean isVisible(String locatorName) {
        return getElement(locatorName).isDisplayed();
    }

    public boolean isEnabled(String locatorName) {
        return getElement(locatorName).isEnabled();
    }

    public boolean isSelected(String locatorName) {
        return getElement(locatorName).isSelected();
    }

    /**
     * Method that verifies if the locator specific is present
     *
     * @param locatorName
     * @return
     */
    public boolean isPresent(String locatorName) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            getElement(locatorName);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
        }
    }

    /**
     * Select or deselect checkbox
     *
     * @param locatorName
     * @param check
     */
    public void setCheckbox(String locatorName, boolean check) {
        WebElement checkbox = waitClickable(locatorName);
        boolean isSelected = checkbox.isSelected();
        if (isSelected && !check) {
            checkbox.click();
        } else if (isSelected && check) {
            checkbox.click();
        }
    }

    /**
     * Method that returns the web driver
     *
     * @return web driver
     */
    public RemoteWebDriver getDriver() {
        if (driver == null) {
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


    //>>>>> DEPRECATED

    /**
     * Method that obtains the element specific
     *
     * @param locator of the element; could be by xpath, id, name, etc
     * @return web element
     * @Deprecated use waitPresence(locatorName)
     */
    @Deprecated
    public WebElement getWebElement(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    @Deprecated
    public List<WebElement> getWebElements(By locator) {
        return getWait().until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
    }

    /**
     * Method that clicks the element specific
     *
     * @param locator of the element to be clickable
     * @Deprecated use click(String locatorName) method
     */
    @Deprecated
    public void clickElement(By locator) {
        WebElement element = getWebElement(locator);
        clickElement(element);
    }

    /**
     * Method that clicks the element specific
     *
     * @param element to be clickable
     * @Deprecated use click(String locatorName) method
     */
    @Deprecated
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
     * @Deprecated use setInput(String locatorName, String value) method
     */
    @Deprecated
    public void completeField(By locator, String value) {
        WebElement element = getWebElement(locator);
        completeField(element, value);
    }

    @Deprecated
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
     * @Deprecated use setInput(String locatorName, String value) method
     */
    @Deprecated
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
     * @Deprecated use setInput(String locatorName, String value) method
     */
    @Deprecated
    public void completeFieldWithoutClick(By locator, String value) {
        WebElement element = getWebElement(locator);
        completeFieldWithoutClick(element, value);
    }

    @Deprecated
    public void completeFieldWithoutClear(WebElement element, String value) {
        clickElement(element);
        element.sendKeys(value);
    }

    @Deprecated
    public void completeFieldWithoutClick(WebElement element, String value) {
        element.clear();
        element.sendKeys(value);
    }

    /**
     * Method that get the text of a element.
     *
     * @param locator of the element to be completed
     * @Deprecated use getText(String locatorName)
     */
    @Deprecated
    public String getElementText(By locator) {
        WebElement element = getWebElement(locator);
        return getElementText(element);
    }

    @Deprecated
    public String getElementText(WebElement element) {
        return element.getText();
    }

    /**
     * Method that get the attribute 'value' of a element, usually an input.
     *
     * @param locator of the element to be completed
     * @Deprecated use getAttribute(String locatorName, "value")
     */
    @Deprecated
    public String getInputValue(By locator) {
        WebElement element = getWebElement(locator);
        return getInputValue(element);
    }

    @Deprecated
    public String getInputValue(WebElement element) {
        return element.getAttribute("value");
    }

    /**
     * Method that checks the option specific if it is not selected
     *
     * @param locator of the checkbox
     * @Deprecated use setCheckbox(locator, true)
     */
    @Deprecated
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
     * @Deprecated use setCheckbox(locator, false)
     */
    @Deprecated
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
     * @Deprecated use isPresent(locatorName)
     */
    @Deprecated
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

    @Deprecated
    public boolean isElementVisible(WebElement element) {
        return element.isDisplayed();
    }

    @Deprecated
    public boolean isElementVisible(By locator) {
        return isElementVisible(getWebElement(locator));
    }

    /**
     * Method that verifies if the element specific is present in the window
     *
     * @param locator of the element specific
     * @return true if the element is present, false otherwise
     * @Deprecated use waitPresence(locatorName) and the isPresent(locatorName)
     */
    @Deprecated
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
     * @Deprecated use getAttribute("value").isEmpty()
     */
    @Deprecated
    public boolean isInputElementEmpty(By inputLocator) {
        return isInputElementEmpty(getWebElement(inputLocator));
    }

    @Deprecated
    public boolean isInputElementEmpty(WebElement element) {
        return element.getAttribute("value").isEmpty();
    }

    /**
     * Method that verifies if the element is not empty
     *
     * @param locator
     * @return true if the element is empty, false otherwise
     * @Deprecated use getText(locatorName).isEmpty()
     */
    @Deprecated
    public boolean isElementEmpty(By locator) {
        return isElementEmpty(getWebElement(locator));
    }

    @Deprecated
    public boolean isElementEmpty(WebElement element) {
        return element.getText().isEmpty();
    }

    /**
     * Wait until an element disappear
     *
     * @Deprecated use waitInvisibility
     */
    @Deprecated
    public void waitForElementDisappears(By locator) {
        getFluentWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait until an element is visible
     *
     * @Deprecated use waitInvisibility
     */
    @Deprecated
    public void waitForElementVisibility(By locator) {
        getFluentWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    /**
     * Wait until an element is not visible
     *
     * @Deprecated use waitInvisibility
     */
    @Deprecated
    public void waitForElementInvisibility(By locator) {
        getFluentWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Wait until an element is clickable
     *
     * @Deprecated use waitInvisibility
     */
    @Deprecated
    public void waitForElementClickable(By locator) {
        getFluentWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    /**
     * Wait until an element is presence
     *
     * @Deprecated use waitInvisibility
     */
    @Deprecated
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

    @Deprecated
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
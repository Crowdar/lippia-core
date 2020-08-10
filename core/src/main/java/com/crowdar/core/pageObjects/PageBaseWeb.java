package com.crowdar.core.pageObjects;

import com.crowdar.core.Constants;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class represents the things in common between Web projects
 *
 * @author: Juan Manuel Spoleti
 */
public class PageBaseWeb extends CucumberPageBase {

    protected NgWebDriver ngWebDriver;

    public PageBaseWeb() {
        super();
    }

    public PageBaseWeb(RemoteWebDriver driver) {
        super(driver);
        initConstructor();
    }

    @Deprecated
    @Override
    public void clickElement(WebElement element) {
        scroll(element);
        element.click();
    }

    @Deprecated
    @Override
    public void clickElement(By locator) {
        WebElement element = getWait().until(ExpectedConditions.elementToBeClickable(locator));
        clickElement(element);
    }

    @Override
    public void click(String locatorName) {
        this.click(locatorName, true);
    }

    @Override
    protected void click(WebElement element) {
        this.click(element, true);
    }

    protected void click(WebElement element, boolean scroll) {
        if(scroll){
            scroll(element);
        }
        super.click(element);
    }

    public void click(String locatorName, boolean scroll){
        WebElement element = waitVisibility(locatorName);
        this.click(element, scroll);
    }

    private void scroll(WebElement element) {
        JavascriptExecutor jse = driver;
        jse.executeScript("arguments[0].scrollIntoView()", element);
    }

    public void clickElementWithoutScroll(By locator) {
        super.clickElement(locator);
    }

    public void clickElementWithoutScroll(WebElement element) {
        super.clickElement(element);
    }

    private void initConstructor() {
        this.ngWebDriver = new NgWebDriver((JavascriptExecutor) driver);
    }

    public void maximizeWindow() {
        driver.manage().window().maximize();
    }

    /**
     * Method that returns the complet url to  the page
     * BASE_URL + url
     *
     * @return String complete
     */
    public String getCompleteURL() {
        return BASE_URL + url;
    }

    /**
     * Method is used to navigated to page complete url
     * BASE_URL + url
     *
     * @return String complete
     */
    public void navigateToCompleteURL() {
        driver.get(getCompleteURL());
    }

    /**
     * Method is used to navigated to certain page
     *
     * @param url to go. Example: https://crowdar.com.ar
     * @return String complete
     */
    public void navigateTo(String url) {
        driver.get(url);
    }

    /**
     * Method that returns the NG web driver for operations with angular
     *
     * @return NG web driver
     */
    public NgWebDriver getNgWebDriver() {
        return ngWebDriver;
    }


    /**
     * Method that produce a dynamic wait time waiting to finish an angular
     * request
     */
    public void angularWait() {
        getNgWebDriver().waitForAngularRequestsToFinish();
    }

    /**
     * switch to the last tab
     */
    public void switchToLastTab() {
        String lastHandle = "";
        for (String winHandle : getDriver().getWindowHandles()) {
            lastHandle = winHandle;
        }
        getDriver().switchTo().window(lastHandle);
    }

    /**
     * Wait to appear more than one tab
     */
    public void waitTabOpening() {
        int attempts = 0;
        WebDriver wd = getDriver();
        long wait = Constants.getWaitScriptTimeout() * 2;
        while ((wd.getWindowHandles().size() == 1) && attempts <= wait) {
            sleep(500);
            attempts++;
        }
    }

    private Select getSelect(WebElement element) {
        return new Select(element);
    }

    protected void setDropdownByText(WebElement element, String text) {
        Select dropdown = getSelect(element);
        dropdown.selectByVisibleText(text);
    }

    public void setDropdownByText(String locatorName, String text) {
        WebElement element = waitVisibility(locatorName);
        setDropdownByText(element, text);
    }

    @Deprecated
    public void selectOptionDropdownByText(By locator, String text) {
        setDropdownByText(getWebElement(locator), text);
    }

    public void setDropdownByValue(String locatorName, String text) {
        WebElement element = waitVisibility(locatorName);
        setDropdownByValue(element, text);
    }

    protected void setDropdownByValue(WebElement element, String value) {
        Select dropdown = getSelect(element);
        dropdown.selectByValue(value);
    }

    public void setDropdownByVisibleText(String locatorName, String text) {
        WebElement element = waitVisibility(locatorName);
        setDropdownByVisibleText(element, text);
    }

    protected void setDropdownByVisibleText(WebElement element, String value) {
        Select dropdown = getSelect(element);
        dropdown.selectByVisibleText(value);
    }

    public void setDropdownByIndex(String locatorName, int index) {
        WebElement element = waitVisibility(locatorName);
        setDropdownByIndex(element, index);
    }

    protected void setDropdownByIndex(WebElement element, int index) {
        Select dropdown = getSelect(element);
        dropdown.selectByIndex(index);
    }

    public WebElement getDropdownSelectedOption(String locatorName) {
        WebElement element = waitVisibility(locatorName);
        return getDropdownSelectedOption(element);
    }

    protected WebElement getDropdownSelectedOption(WebElement element) {
        Select dropdown = getSelect(element);
        return dropdown.getFirstSelectedOption();
    }

    public List<WebElement> getDropdownAllSelectedOptions(String locatorName) {
        WebElement element = waitVisibility(locatorName);
        return getDropdownAllSelectedOptions(element);
    }

    protected List<WebElement> getDropdownAllSelectedOptions(WebElement element) {
        Select dropdown = getSelect(element);
        return dropdown.getAllSelectedOptions();
    }

    @Deprecated
    public void selectOptionDropdownByValue(By locator, String value) {
        setDropdownByValue(getWebElement(locator), value);
    }

    protected void deselectDropdownAll(WebElement element) {
        Select dropdown = getSelect(element);
        dropdown.deselectAll();
    }

    public void deselectDropdownAll(String locatorName) {
        WebElement element = waitVisibility(locatorName);
        deselectDropdownAll(element);
    }

    @Deprecated
    public void deselectAllOptionsDropdown(By locator) {
        deselectDropdownAll(getWebElement(locator));
    }

    protected void deselectDropdownByValue(WebElement element, String value) {
        Select dropdown = getSelect(element);
        dropdown.deselectByValue(value);
    }

    public void deselectDropdownByValue(String locatorName, String value) {
        WebElement element = waitVisibility(locatorName);
        deselectDropdownByValue(element, value);
    }

    protected void deselectDropdownByText(WebElement element, String text) {
        Select dropdown = getSelect(element);
        dropdown.deselectByValue(text);
    }

    public void deselectDropdownByText(String locatorName, String value) {
        WebElement element = waitVisibility(locatorName);
        deselectDropdownByText(element, value);
    }

    protected void deselectDropdownByIndex(WebElement element, int index) {
        Select dropdown = getSelect(element);
        dropdown.deselectByIndex(index);
    }

    public void deselectDropdownByIndex(String locatorName, int index) {
        WebElement element = waitVisibility(locatorName);
        deselectDropdownByIndex(element, index);
    }

    @Deprecated
    public void deselectOptionDropdownByValue(By locator, String value) {
        deselectDropdownByValue(getWebElement(locator), value);
    }

    @Deprecated
    public void deselectOptionDropdownByText(By locator, String text) {
        deselectDropdownByText(getWebElement(locator), text);
    }

    /**
     * Similar to does element exist, but also verifies that only one such
     * element exists and that it is displayed
     *
     * @param by By statement locating the element.
     * @return boolean if one and only one element matching the locator is
     * found, and if it is displayed and enabled, F otherwise.
     */
    protected boolean isElementPresentAndDisplayed(By by) {
        boolean isPresent = false;
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            List<WebElement> elements = driver.findElements(by);
            isPresent = (elements.size() == 1) && elements.get(0).isDisplayed();
        } finally {
            driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
        }

        return isPresent;
    }

    /**
     * Method that drag and drop some element over other element
     *
     * @param elementToDrag
     * @param elementToReplace
     */
    protected void dragAndDrop(WebElement elementToDrag, WebElement elementToReplace) {
        JavascriptExecutor jse = driver;
        jse.executeScript("arguments[0].scrollIntoView()", elementToReplace);

        new Actions(driver).dragAndDrop(elementToDrag, elementToReplace).perform();
    }

    public void dragAndDrop(String locatorToDrag, String locatorToReplace) {
        WebElement elementToDrag = waitVisibility(locatorToDrag);
        WebElement elementToReplace = waitVisibility(locatorToReplace);
        dragAndDrop(elementToDrag, elementToReplace);
    }
}
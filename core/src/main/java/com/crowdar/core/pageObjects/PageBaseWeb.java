package com.crowdar.core.pageObjects;

import com.crowdar.core.Constants;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

/**
 * This class represents the things in common between Web projects
 *
 * @author: Juan Manuel Spoleti
 */
abstract public class PageBaseWeb extends PageBase {

    protected NgWebDriver ngWebDriver;

    public PageBaseWeb(RemoteWebDriver driver) {
        super(driver);
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
     * Method that clicks the element specific
     *
     * @param locator of the element to be clickable
     */
    public void clickElement(By locator) {

        WebElement element = getWait().until(ExpectedConditions.elementToBeClickable(locator));

        JavascriptExecutor jse = (JavascriptExecutor) driver;

        jse.executeScript("arguments[0].scrollIntoView()", element);
        element.click();
    }

    /**
     * Method that clicks the element specific
     *
     * @param element to be clickable
     */
    public void clickElement(WebElement element) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;

        jse.executeScript("arguments[0].scrollIntoView()", element);
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
        element.clear();
        element.sendKeys(value);
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

    public void selectOptionDropdownByText(WebElement element, String text) {
        Select dropdown = getSelect(element);
        dropdown.selectByVisibleText(text);
    }

    public void selectOptionDropdownByText(By locator, String text) {
        selectOptionDropdownByText(getWebElement(locator), text);
    }

    public void selectOptionDropdownByValue(WebElement element, String value) {
        Select dropdown = getSelect(element);
        dropdown.selectByValue(value);
    }

    public void selectOptionDropdownByValue(By locator, String value) {
        selectOptionDropdownByValue(getWebElement(locator), value);
    }

    public void deselectAllOptionsDropdown(WebElement element) {
        Select dropdown = getSelect(element);
        dropdown.deselectAll();
    }

    public void deselectAllOptionsDropdown(By locator) {
        deselectAllOptionsDropdown(getWebElement(locator));
    }

    public void deselectOptionDropdownByValue(WebElement element, String value) {
        Select dropdown = getSelect(element);
        dropdown.deselectByValue(value);
    }

    public void deselectOptionDropdownByText(WebElement element, String text) {
        Select dropdown = getSelect(element);
        dropdown.deselectByValue(text);
    }

    public void deselectOptionDropdownByValue(By locator, String value) {
        deselectOptionDropdownByValue(getWebElement(locator), value);
    }

    public void deselectOptionDropdownByText(By locator, String text) {
        deselectOptionDropdownByText(getWebElement(locator), text);
    }
}
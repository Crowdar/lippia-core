package com.crowdar.core.pageObjects;

import com.crowdar.bdd.cukes.SharedDriver;
import com.crowdar.core.Constants;
import com.paulhammant.ngwebdriver.NgWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
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

    public PageBaseWeb(RemoteWebDriver driver) {
        super(driver);
        initConstructor();
    }

    public PageBaseWeb(SharedDriver driver) {
        super(driver);
        initConstructor();
    }

    @Override
    public void clickElement(WebElement element) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView()", element);
        element.click();
    }

    @Override
    public void clickElement(By locator) {
        WebElement element = getWait().until(ExpectedConditions.elementToBeClickable(locator));
        clickElement(element);
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
        // Temporarily set the implicit timeout to zero
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            // Check to see if there are any elements in the found list
            List<WebElement> elements = driver.findElements(by);
            isPresent = (elements.size() == 1) && elements.get(0).isDisplayed();
            // && elements.get(0).isEnabled();
        } finally {
            // Return to the original implicit timeout value
            driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
        }

        return isPresent;
    }

}
package com.crowdar.core.pageObjects;

import com.crowdar.bdd.cukes.SharedDriver;
import com.crowdar.core.Constants;
import com.crowdar.core.Utils;
import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * This class represents the things in common between Mobile projects
 *
 * @author: Juan Manuel Spoleti
 */
public class PageBaseMobile extends CucumberPageBase {

    public PageBaseMobile(RemoteWebDriver driver) {
        super(driver);
    }

    public PageBaseMobile(SharedDriver driver) {
        super(driver);
    }

    public void scrollToElement(String accessibilityId) {
        String uiSelector = "new UiSelector().description(\"" + accessibilityId
                + "\")";

        String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView("
                + uiSelector + ");";

        driver.findElement(MobileBy.AndroidUIAutomator(command));
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
        String uiSelector = "new UiSelector().textContains(\"" + option
                + "\")";

        String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView("
                + uiSelector + ");";

        WebElement element = driver.findElement(MobileBy.AndroidUIAutomator(command));

        this.clickElement(element);
    }

    protected void scrollToElementId(String id) {
        String uiSelector = "new UiSelector().resourceId(\"" + id
                + "\")";

        String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView("
                + uiSelector + ");";

        driver.findElement(MobileBy.AndroidUIAutomator(command));
    }

    @Override
    public void clickElement(By locator) {
        WebElement element = getWebElement(locator);
        clickElement(element);
    }

    @Override
    public void clickElement(WebElement element) {
        element.click();
    }

    @Override
    public boolean isElementVisible(WebElement element) {
        return element.isEnabled();
    }

    @Override
    public boolean isElementVisible(By locator) {
        return isElementVisible(getWebElement(locator));
    }

    @Override
    public void waitForElementVisibility(By locator) {
        getFluentWait().until(driver -> driver.findElement(locator).isEnabled());
    }

    @Override
    public void waitForElementInvisibility(By locator) {
        getDriver().manage().timeouts().implicitlyWait(0L, SECONDS);
        try {
            boolean isPresent = true;
            long attempts = Constants.getWaitForElementTimeout() * 2;
            while (isPresent) {
                try {
                    getDriver().findElement(locator);
                    isPresent = true;
                } catch (NoSuchElementException var7) {
                    System.out.println(var7.getMessage());
                    isPresent = false;
                }
                if (attempts == 0) {
                    System.out.println("Element is still visible.");
                    isPresent = false;
                }
                sleep(500);
                attempts--;
            }
        } finally {
            getDriver().manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), SECONDS);
        }
    }
}
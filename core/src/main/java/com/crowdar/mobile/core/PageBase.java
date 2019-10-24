package com.crowdar.mobile.core;

import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.crowdar.bdd.cukes.SharedDriver;
import com.crowdar.core.Constants;
import com.crowdar.core.CucumberPageBase;
import com.crowdar.core.Utils;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.PerformsTouchActions;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.touch.offset.PointOption;

/**
 * This class represents the things in common between the other classes of
 * pages.
 *
 * @author: Juan Manuel Spoleti
 */
abstract public class PageBase extends CucumberPageBase  {

    protected RemoteWebDriver driver;

    public PageBase(SharedDriver driver) {
        super(driver);
    }

    public PageBase(RemoteWebDriver driver){super(driver); }

    /**
     * Returns the mobile driver
     *
     * @return mobile driver
     */
    public RemoteWebDriver getDriver() {
        return super.getDriver();
    }

    /**
     * Returns the default wait in our framework
     *
     * @return mobile driver wait
     */
    public WebDriverWait getWait() {
        return wait;
    }

    /**
     * Obtains the element with the required accessibilityId
     *
     * @param locator of the element
     * @return mobile element
     */
    public WebElement getMobileElement(By locator) {
        return (WebElement) getWait().until((Function<? super WebDriver, ? extends Object>) ExpectedConditions.presenceOfElementLocated(locator));
    }

    /**
     * Clicks the element specific
     *
     * @param locator of the element to be clicked
     */
    public void clickElement(By locator) {
        // this.scrollToElement(locator);
        this.waitForElementClickeable(locator);
        WebElement element = getMobileElement(locator);
        this.clickElement(element);
    }

    /**
     * Clicks the coordinates x and y
     *
     * @param x
     * @param y
     */
    public void clickElement(int x, int y) {
        TouchAction touchAction = new TouchAction((PerformsTouchActions) driver);
        touchAction.tap(PointOption.point(x,y)).perform();
        sleep(1000);
    }

    /**
     * Clicks the element specific
     *
     * @param element to be clicked
     */
    public void clickElement(WebElement element) {
        element.click();
    }

    public void scrollToElement(String accessibilityId) {
        String uiSelector = "new UiSelector().description(\"" + accessibilityId
                + "\")";

        String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView("
                + uiSelector + ");";

        driver.findElement(MobileBy.AndroidUIAutomator(command));
    }

    /**
     * Completes the input field specific with a value specific
     * First: obtains the element, Second: cleans the field, Third: completes the
     * field.
     *
     * @param locator of the element to be completed
     * @param value   to write in the field
     */
    public void completeField(By locator, String value) {
        WebElement element = getMobileElement(locator);
        this.completeField(element, value);
    }


    /**
     * Completes the input field specific with a value specific doing click in the field with coordinates x and y
     * First: obtains the element, Second: cleans the field, Third: completes the
     * field.
     *
     * @param coordinatesX coords x of element to complete
     * @param coordinatesY coords y of element to complete
     * @param value        to write in the field
     */
    public void completeField(int coordinatesX, int coordinatesY, String value) {
        this.clickElement(coordinatesX, coordinatesY);
        driver.getKeyboard().sendKeys(value);

        if (driver.getCapabilities().getPlatform().is(Platform.ANDROID)) {
            ((AndroidDriver) driver).pressKey(new KeyEvent(AndroidKey.ENTER));
        }
        sleep(1000);
    }

    /**
     * Completes the input field specific with a value specific
     * First: obtains the element, Second: cleans the field, Third: completes the
     * field.
     *
     * @param locator     of the element to be completed
     * @param value       to write in the field
     * @param placeholder
     */
    public void completeField(By locator, String value, String placeholder) {
        //this.scrollToElement(locator);
        WebElement element = getMobileElement(locator);
        this.completeField(element, value, placeholder);
    }

    /**
     * Completes the input field specific with a value specific
     * First: cleans the field if its not empty, Second: completes the field.
     *
     * @param element to be completed
     * @param value   to write in the field
     */
    public void completeField(WebElement element, String value) {
        if (!element.getText().isEmpty()) {
            element.clear();
        }
        element.sendKeys(value);
        ((AppiumDriver) driver).hideKeyboard();

    }

    public void completeFieldWithoutClear(By locator, String value) {
        WebElement element = getMobileElement(locator);
        this.completeFieldWithoutClear(element, value);
    }

    public void completeFieldWithoutClear(WebElement element, String value) {
        element.sendKeys(value);
        ((AppiumDriver) driver).hideKeyboard();
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
        ((MobileElement)element).setValue(value);
        ((AppiumDriver) driver).hideKeyboard();
    }

    /**
     * Checks the option specific if it is not selected
     *
     * @param accessibilityId of input node of the checkbox
     * @throws RuntimeException if checkbox is not enabled to be operated
     */
    public void selectCheckbox(String accessibilityId) {
        WebElement checkbox = (WebElement) ((AppiumDriver) driver).findElementByAccessibilityId(accessibilityId);
        if (checkbox.isEnabled()) {
            if (!checkbox.isSelected()) {
                checkbox.click();
            }
        } else {
            throw new RuntimeException(String.format("Checkbox not enabled: %s", accessibilityId));
        }
    }

    /**
     * Unchecks the option specific if it is not unselected
     *
     * @param accessibilityId of input node of the checkbox
     * @throws RuntimeException if checkbox is not enabled to be operated
     */
    public void deselectCheckbox(String accessibilityId) {
        WebElement checkbox = (WebElement) ((AppiumDriver) driver).findElementByAccessibilityId(accessibilityId);
        if (checkbox.isEnabled()) {
            if (checkbox.isSelected()) {
                checkbox.click();
            }
        } else {
            throw new RuntimeException(String.format("Checkbox not enabled: %s", accessibilityId));
        }
    }

    /**
     * Verifies if the element specific is present in the window
     *
     * @param accessibilityId of the element node
     * @return <b>true</b> if the element is present, <b>false</b> otherwise
     */
    public boolean isElementPresent(String accessibilityId) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            ((AppiumDriver) driver).findElementByAccessibilityId(accessibilityId);
            return true;
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
        }
    }

    /**
     * Verifies if the element specific is present in the window
     *
     * @param locator of the element node
     * @return <b>true</b> if the element is present, <b>false</b> otherwise
     */
    public boolean isElementPresent(By locator) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        try {
            WebElement element = (WebElement) getDriver().findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
        }
    }

    /**
     * Wait a little time and verifies if the element specific is present in the window
     *
     * @param locator    of the element node
     * @param timeToWait time we wait in seconds
     * @return <b>true</b> if the element is present, <b>false</b> otherwise
     */
    public boolean waitAndCheckIfIsElementPresent(By locator, long timeToWait) {
        driver.manage().timeouts().implicitlyWait(timeToWait, TimeUnit.SECONDS);
        try {
            getDriver().findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
        }
    }

    /**
     * Waits for one elements to be visible
     *
     * @param locator
     */
    public void waitForElementVisibility(By locator) {
        WebElement element = (WebElement) getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public boolean isElementVisible(By locator) {
        try {
            waitForElementVisibility(locator);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Waits for one or more elements to be visible
     *
     * @param accessibilityId
     */
    public void waitForElementsVisibility(String accessibilityId) {
        getWait().until(ExpectedConditions.visibilityOfAllElementsLocatedBy(MobileBy.ByAccessibilityId.AccessibilityId(accessibilityId)));
    }

    /**
     * Waits for one element to be invisible
     *
     * @param locator
     */
    public void waitForElementInvisibility(By locator) {
        getFluentWait().until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    /**
     * Waits for one elements to be clickeable
     *
     * @param accessibilityId
     */
    public void waitForElementClickeable(String accessibilityId) {
        getWait().until(ExpectedConditions.elementToBeClickable(MobileBy.ByAccessibilityId.AccessibilityId(accessibilityId)));
    }

    /**
     * Waits for one element to be clickeable
     *
     * @param element
     */
    public void waitForElementClickeable(WebElement element) {
        getWait().until(ExpectedConditions.elementToBeClickable(element));
    }

    /**
     * Waits for one element to be clickeable
     *
     * @param locator
     */
    public void waitForElementClickeable(By locator) {
        getWait().until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitForElementEnabled(By locator) {
        WebElement element = getMobileElement(locator);
        boolean isEnabled = element.isEnabled();
        int interval = 0;
        while (!isEnabled) {
            interval++;
            sleep(500);
            isEnabled = element.isEnabled();
            if (Constants.getWaitForElementTimeout() < interval) {
                System.out.println("Element " + element.getText() + " is not enabled");
            }
        }
    }

    public void scrollToEnd() {
        Dimension size = driver.manage().window().getSize();
        int y_start = (int) (size.height * 0.60);
        int y_end = (int) (size.height * 0.30);
        int x = size.width / 2;
        //((AppiumDriver) driver).swipe(x, y_start, x, y_end, 4000);
        throw new UnsupportedOperationException("this is not supported yet, guys should implement swipe in java-client 7.0 for appium");
    }

    public void scroll(int timesToScroll) {
        for (int i = 0; timesToScroll > i; i++) {
            Dimension size = driver.manage().window().getSize();
            int y_start = (int) (size.height * 0.60);
            int y_end = (int) (size.height * 0.30);
            int x = size.width / 2;
            //(AppiumDriver) driver).swipe(x, y_start, x, y_end, 4000);
            throw new UnsupportedOperationException("this is not supported yet, guys should implement swipe in java-client 7.0 for appium");
        }
        sleep(1000);
    }

    public void selectOptionSpinner(String option) {
        String uiSelector = "new UiSelector().textContains(\"" + option
                + "\")";

        String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView("
                + uiSelector + ");";

        WebElement element = (WebElement) driver.findElement(MobileBy.AndroidUIAutomator(command));

        this.clickElement(element);
    }

    public void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            System.out.println("Sleep error: " + e);
        }
    }

    protected void scrollToElementId(String id) {
        String uiSelector = "new UiSelector().resourceId(\"" + id
                + "\")";

        String command = "new UiScrollable(new UiSelector().scrollable(true).instance(0)).scrollIntoView("
                + uiSelector + ");";

        driver.findElement(MobileBy.AndroidUIAutomator(command));
    }
}
package com.crowdar.web.webDriver;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import com.crowdar.core.Constants;

import ru.stqa.selenium.factory.SingleWebDriverPool;

public final class WebDriverManager {

    private static WebDriver driver;
    private static Wait<WebDriver> wait;
    private static Enum<BrowserConfiguration> browserConfiguration;

    private WebDriverManager() {
    }

    public static WebDriver getDriverInstance() {
        if (driver == null || ((RemoteWebDriver) driver).getSessionId() == null) {
            driver = getDriver();
        }
        return driver;
    }

    public static void build(Enum<BrowserConfiguration> browserConfig) {
    	driver = null;
        browserConfiguration = browserConfig;
    }

    private static WebDriver getDriver() {
        driver = ((BrowserConfiguration) browserConfiguration).getDriver();

        wait = new FluentWait<WebDriver>(driver)
				.withTimeout(Constants.WAIT_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
				.pollingEvery(Constants.FLUENT_WAIT_REQUEST_FREQUENCY_IN_MILLIS, TimeUnit.MILLISECONDS)
				.ignoring(NoSuchElementException.class);
        
        driver.manage().timeouts().setScriptTimeout(Constants.WAIT_SCRIPT_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(Constants.WAIT_IMPLICIT_TIMEOUT, TimeUnit.SECONDS);

        return driver;
    }
   
    public static Wait<WebDriver> getFluentnWait(){
    	return wait;
    }

    public static void dismissAll() {
        SingleWebDriverPool.DEFAULT.dismissAll();
        
    }
}
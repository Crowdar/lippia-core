package com.crowdar.web;

import com.crowdar.core.Constants;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import ru.stqa.selenium.factory.WebDriverPool;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public final class WebDriverManager {

    private static WebDriver driver;
    private static Enum<BrowserConfiguration> browserConfiguration = null;

    private WebDriverManager() {
    }

    public static WebDriver getDriverInstance() {
        if (driver == null || ((RemoteWebDriver) driver).getSessionId() == null) {
            driver = getDriver();
        }
        return driver;
    }

    @Deprecated
    public static WebDriver getNewDriverInstance() {
        return getDriver();
    }

    public static void build(Enum<BrowserConfiguration> browserConfig) {
        browserConfiguration = browserConfig;
    }

    private static WebDriver getDriver() {
        driver = ((BrowserConfiguration) browserConfiguration).getDriver();

        driver.manage().timeouts().setScriptTimeout(Constants.WAIT_SCRIPT_TIMEOUT, TimeUnit.SECONDS);
        driver.manage().timeouts().implicitlyWait(Constants.WAIT_IMPLICIT_TIMEOUT, TimeUnit.SECONDS);

        return driver;
    }

    public static void dismissAll() {
        WebDriverPool.DEFAULT.dismissAll();
    }

    public static String getLocalStorageItemValue(String key, String item) {
        String strItemValue = "";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String jsonAuth = js.executeScript(String.format("return window.localStorage.getItem('%s');", key)).toString();
        if(!jsonAuth.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();

            try {
                Map<String, Object> authData = mapper.readValue(jsonAuth, new TypeReference<Map<String, Object>>(){});
                strItemValue = authData.get(item).toString();
            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return strItemValue;
    }
}
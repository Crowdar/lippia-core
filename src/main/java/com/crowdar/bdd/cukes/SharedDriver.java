package com.crowdar.bdd.cukes;

import com.crowdar.core.PropertyManager;
import com.crowdar.web.BrowserConfiguration;
import com.crowdar.web.WebDriverManager;
import cucumber.api.Scenario;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Example of a WebDriver implementation that has delegates all methods to a static instance (REAL_DRIVER) that is only
 * created once for the duration of the JVM. The REAL_DRIVER is automatically closed when the JVM exits. This makes
 * scenarios a lot faster since opening and closing a browser for each scenario is pretty slow.
 * To prevent browser state from leaking between scenarios, cookies are automatically deleted before every scenario.
 * </p>
 * <p>
 * A new instance of SharedDriver is created for each Scenario and passed to yor Stepdef classes via Dependency Injection
 * </p>
 * <p>
 * As a bonus, screenshots are embedded into the report for each scenario. (This only works
 * if you're also using the HTML formatter).
 * </p>
 * <p>
 * A new instance of the SharedDriver is created for each Scenario and then passed to the Step Definition classes'
 * constructor. They all receive a reference to the same instance. However, the REAL_DRIVER is the same instance throughout
 * the life of the JVM.
 * </p>
 */
public class SharedDriver extends EventFiringWebDriver {

    private static final Map<Long, WebDriver> threadDriversMap = new HashMap<>();

    private static WebDriver get(){

        Long id = Thread.currentThread().getId();
        if(threadDriversMap.containsKey(id)){
            return  threadDriversMap.get(id);
        }else{
            WebDriverManager.build(BrowserConfiguration.getBrowserConfiguration(PropertyManager.getProperty("crowdar.cucumber.browser")));
            WebDriver webDriver =  WebDriverManager.getNewDriverInstance();
            threadDriversMap.put(id,webDriver);
            addShutDownHook(webDriver);
            return webDriver;

        }

    }

    private static void addShutDownHook(WebDriver driver){
        Thread close = new Thread(){
            @Override
            public void run() {
                driver.quit();
            }
        };
        Runtime.getRuntime().addShutdownHook(close);
    }

    public SharedDriver() {
        super(get());
    }

    public void deleteAllCookies() {
        manage().deleteAllCookies();
    }


    public void embedScreenshot(Scenario scenario) {
        try {
            byte[] screenshot = getScreenshotAs(OutputType.BYTES);
            scenario.embed(screenshot, "image/png");
        } catch (WebDriverException somePlatformsDontSupportScreenshots) {
            System.err.println(somePlatformsDontSupportScreenshots.getMessage());
        }
    }
}

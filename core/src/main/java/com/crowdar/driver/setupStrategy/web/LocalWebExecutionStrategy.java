package com.crowdar.driver.setupStrategy.web;

import java.io.File;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.ProjectTypeEnum;
import com.crowdar.driver.setupStrategy.SetupStrategy;

@Deprecated
public class LocalWebExecutionStrategy implements SetupStrategy {

    @Override
    public void beforeDriverStartSetup(ProjectTypeEnum configuration) {

//    	En este caso se toma el ejecutable del driver desde el projecto, no se usa por el grado de mantenibilidad que requiere 	
    	
//        switch ((BrowserConfiguration) contextConfiguration) {
//            case FIREFOX:
//                System.setProperty("webdriver.gecko.driver", getWebDriverPath().concat("geckodriver.exe"));
//                break;
//            case CHROME:
//                System.setProperty("webdriver.chrome.driver", getWebDriverPath().concat("chromedriver2.37.exe"));
//                break;
//            case EDGE:
//                System.setProperty("webdriver.edge.driver", getWebDriverPath().concat("MicrosoftWebDriver.exe"));
//                break;
//            case IE:
//                System.setProperty("webdriver.ie.driver", getWebDriverPath().concat("IEDriverServer.exe"));
//        }

    }

    @Override
    public void afterDriverStartSetup(RemoteWebDriver driver) {
        // TODO Auto-generated method stub

    }

    private static String getWebDriverPath() {
        return System.getProperty("user.dir").concat(File.separator).concat("webDrivers").concat(File.separator);
    }

}

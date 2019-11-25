package com.crowdar.driver;



import com.crowdar.core.PropertyManager;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.util.Assert;
import org.testng.IObjectFactory;
import org.testng.annotations.ObjectFactory;
import org.testng.annotations.Test;
import static org.mockito.Mockito.when;

@PrepareForTest(PropertyManager.class)
@PowerMockIgnore({"javax.net.ssl.*","org.apache.log4j.*","org.apache.xerces.*","org.w3c.*", "javax.xml.*", "org.xml.*", "org.apache.*", "org.w3c.dom.*", "org.apache.cxf.*"})
public class DriverManagerTest extends PowerMockTestCase {

    @Test
    public void CHROMEDYNAMIC_Test()
    {
        PowerMockito.mockStatic(PropertyManager.class);
        when(PropertyManager.getProperty("crowdar.cucumber.browser")).thenReturn("CHROMEDYNAMIC");
        when(PropertyManager.getProperty("crowdar.projectType")).thenReturn("WEB_CHROME");
        when(PropertyManager.getProperty("crowdar.setupStrategy")).thenReturn("web.DownloadLatestStrategy");
        RemoteWebDriver remoteWebDriver = DriverManager.getDriverInstance();
        Assert.notNull(remoteWebDriver, "Error creating Dynamic web Driver in DriverManager");
        DriverManager.dismissCurrentDriver();
    }

    @Test
    public void QuitDriver_Test()
    {
        PowerMockito.mockStatic(PropertyManager.class);
        when(PropertyManager.getProperty("crowdar.cucumber.browser")).thenReturn("CHROMEDYNAMIC");
        when(PropertyManager.getProperty("crowdar.projectType")).thenReturn("WEB_CHROME");
        when(PropertyManager.getProperty("crowdar.setupStrategy")).thenReturn("web.DownloadLatestStrategy");
        RemoteWebDriver remoteWebDriver = DriverManager.getDriverInstance();
        Assert.notNull(remoteWebDriver, "Error creating Dynamic web Driver in DriverManager");
        remoteWebDriver.quit();
    }

}
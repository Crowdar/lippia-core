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
@PowerMockIgnore("javax.net.ssl.*")
public class DriverManagerTest {


    @Test
    public void WebConfiguration()
    {

        PowerMockito.mockStatic(PropertyManager.class);
        when(PropertyManager.getProperty("crowdar.cucumber.browser")).thenReturn("CHROMEDYNAMIC");
        when(PropertyManager.getProperty("crowdar.projectType")).thenReturn("WEB_CHROME");
        when(PropertyManager.getProperty("crowdar.setupStrategy")).thenReturn("web.DownloadLatestStrategy");


        RemoteWebDriver remoteWebDriver = DriverManager.getDriverInstance();
        Assert.notNull(remoteWebDriver, "Error creating Dynamic web Driver in DriverManager");


    }

}
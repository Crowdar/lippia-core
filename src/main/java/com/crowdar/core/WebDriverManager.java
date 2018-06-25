package com.crowdar.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;



import ru.stqa.selenium.factory.WebDriverPool;

public final class WebDriverManager {

//	private static final String DRIVER_MODE_GRID_VALUE = "grid";
//	private static final String DRIVER_MODE_KEY = "driver.mode";
	private static WebDriver driver;
	
	private WebDriverManager() {
	}
	
	public static WebDriver getDriver(){
		
		if(driver == null || ((RemoteWebDriver)driver).getSessionId()==null)
			driver = getChromeDriver();
		return driver;
	}
	
	@Deprecated
	public static WebDriver getNewWebDriver(){
		return getChromeDriver();
	}
	
	// cuando sea necesario se implementara un strategy para cada navegador.
	private static WebDriver getChromeDriver(){
		
		ChromeOptions options = new ChromeOptions();
		options.addArguments("disable-infobars");
		options.addArguments("start-maximized");
		
		DesiredCapabilities customCapabilities = DesiredCapabilities.chrome();
		customCapabilities.setCapability(ChromeOptions.CAPABILITY, options);
		
		WebDriver driver = getDriver(customCapabilities);
		
		driver.manage().timeouts().setScriptTimeout(Constants.WAIT_SCRIPT_TIMEOUT, TimeUnit.SECONDS);
		driver.manage().timeouts().implicitlyWait(Constants.WAIT_IMPLICIT_TIMEOUT, TimeUnit.SECONDS);
		
		return driver;
	}

	
	private static WebDriver getDriver(DesiredCapabilities customCapabilities) {
		WebDriver driver = null;
		
		if(isGridConfiguration()){
			try{
				System.out.println("# # # # # # # # # # # # # Driver mode: Grid");
				driver = WebDriverPool.DEFAULT.getDriver(new URL("http://server:4444/wd/hub"), customCapabilities);
				driver.manage().window().setSize(new Dimension(1280, 1024));
			}catch (MalformedURLException  e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("# # # # # # # # # # # # # Driver mode: Default");
			driver =  new ChromeDriver(customCapabilities);
		}
		return driver;
	}
	
	private static boolean isGridConfiguration(){
//		String driverMode = System.getProperty(DRIVER_MODE_KEY);
//		return driverMode!=null && driverMode.equals(DRIVER_MODE_GRID_VALUE);
			
		String os = System.getProperty("os.name");
		return os.contains("inux");
	}
	
	public static void dismissAll(){
		driver.close();
		driver = null;
	}
}

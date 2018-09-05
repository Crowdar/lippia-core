package com.crowdar.web;

import com.crowdar.core.PropertyManager;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import org.apache.log4j.Logger;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.stqa.selenium.factory.SingleWebDriverPool;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public enum BrowserConfiguration {
	FIREFOX {
		@Override
		public void localSetup() {
			System.setProperty("webdriver.gecko.driver",getWebDriverPath().concat("geckodriver.exe"));
		}

		@Override
		public DesiredCapabilities getDesiredCapabilities() {
			DesiredCapabilities capabilities = DesiredCapabilities.firefox();
			capabilities.setBrowserName("firefox");
			return capabilities;
		}
	},
	CHROME {
		@Override
		public void localSetup() {
			System.setProperty("webdriver.chrome.driver",getWebDriverPath().concat("chromedriver2.37.exe"));
		}

		@Override
		public DesiredCapabilities getDesiredCapabilities() {

			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setPlatform(Platform.LINUX);
			capabilities.setBrowserName(capabilities.getBrowserName());

			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-infobars");
			options.addArguments("start-maximized");
//			options.addArguments("screenshot");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);

			return capabilities;
		}
	},
	CHROMEDYNAMIC{

		@Override
		public void localSetup() {
			ChromeDriverManager.getInstance().setup();
		}
		public WebDriver getDynamicWebDriver(){
			return new ChromeDriver(getDesiredCapabilities());
		}

		@Override
		public DesiredCapabilities getDesiredCapabilities() {
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			//BrowserMobProxy proxy = new BrowserMobProxyServer(); TODO configure proxy to re-write http headers and cookies
			//proxy.start(0);
			// get the Selenium proxy object
			//Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
		//	capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
			capabilities.setBrowserName(capabilities.getBrowserName());
			ChromeOptions options = new ChromeOptions();
			options.addArguments("disable-infobars");
			options.addArguments("start-maximized");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			return capabilities;
		}},
	  CHROMEEXTENCION{

		@Override
		public void localSetup() {
			ChromeDriverManager.getInstance().setup();
		}
		public WebDriver getDynamicWebDriver(){
			return new ChromeDriver(getDesiredCapabilities());
		}

		@Override
		public DesiredCapabilities getDesiredCapabilities() {
			DesiredCapabilities capabilities = DesiredCapabilities.chrome();
			capabilities.setBrowserName(capabilities.getBrowserName());
			ChromeOptions options = new ChromeOptions();
			ChromeUtils.insertHeadersExtension(options);
			options.addArguments("disable-infobars");
			options.addArguments("start-maximized");
			capabilities.setCapability(ChromeOptions.CAPABILITY, options);
			return capabilities;
		}},
	EDGE {
		@Override
		public void localSetup() {
			System.setProperty("webdriver.edge.driver",getWebDriverPath().concat("MicrosoftWebDriver.exe"));
		}

		@Override
		public DesiredCapabilities getDesiredCapabilities() {

//			System.setProperty("webdriver.ie.driver.loglevel","DEBUG");

			DesiredCapabilities edgeCapabilities = DesiredCapabilities.edge();
			edgeCapabilities.setPlatform(Platform.WIN10);
			edgeCapabilities.setBrowserName(edgeCapabilities.getBrowserName());
			return edgeCapabilities;
		}
	},
	IE {
		@Override
		public void localSetup() {
			System.setProperty("webdriver.ie.driver",getWebDriverPath().concat("IEDriverServer.exe"));
		}

		@Override
		public DesiredCapabilities getDesiredCapabilities() {

//			System.setProperty("webdriver.ie.driver.loglevel","DEBUG");

			DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();
			capabilities.setPlatform(Platform.VISTA);
			capabilities.setBrowserName(capabilities.getBrowserName());
			return capabilities;
		}
	},
	SAFARI {
		@Override
		public void localSetup() {
		}

		@Override
		public DesiredCapabilities getDesiredCapabilities() {
			DesiredCapabilities capabilities = DesiredCapabilities.safari();
			capabilities.setPlatform(Platform.MAC);
			capabilities.setBrowserName("safari");
			return capabilities;
		}
	};

	private Logger logger = Logger.getLogger(BrowserConfiguration.class);

	public static final String BROWSER_KEY = "browser";

	public static BrowserConfiguration getBrowserConfiguration(String key) {

		for (BrowserConfiguration current : values()) {
			if (current.name().equalsIgnoreCase(key)) {
				return current;
			}
		}
		return null;
	}

	private final String DRIVER_GRID_HUB_KEY = "gridHub";

	public abstract void localSetup();
	public abstract DesiredCapabilities getDesiredCapabilities();

	public WebDriver getDynamicWebDriver(){
		return null;
	}


	public org.openqa.selenium.WebDriver getDriver() {
		org.openqa.selenium.WebDriver driver = null;

		if (isGridConfiguration()) {
			try {
				logger.info("############################################ WebDriver mode: Grid");
				driver = SingleWebDriverPool.DEFAULT.getDriver(new URL(System.getProperty(DRIVER_GRID_HUB_KEY)), getDesiredCapabilities());
				driver.manage().window().setSize(new Dimension(1280, 1024));
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		} else{
			logger.info("############################################ WebDriver mode: Default");
			localSetup();
			driver = SingleWebDriverPool.DEFAULT.getDriver(getDesiredCapabilities());
		}
		return driver;
	}


	private boolean isGridConfiguration() {
		String driverHub = System.getProperty(DRIVER_GRID_HUB_KEY);
		return driverHub != null && !driverHub.isEmpty();
	}

	private boolean isDynamic(){
			return PropertyManager.getProperty("crowdar.dinamic.browser") != null ;
	}

	private static String getWebDriverPath() {
		return System.getProperty("user.dir").concat(File.separator).concat("webDrivers").concat(File.separator);
	}
}

package com.crowdar.core;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.paulhammant.ngwebdriver.NgWebDriver;


/**
 * This class represents the things in common between the other classes of
 * pages.
 * 
 * @author: Juan Manuel Spoleti
 */
abstract public class PageBase{
    

	public static  String BASE_URL; // this is the Base url for all system to be tested
	protected String url;// this is the url that corespond to this child page and should be initialized in  child contructor child page

	
	protected WebDriver driver;	
	protected NgWebDriver ngWebDriver;
	protected WebDriverWait wait;
	protected Wait<WebDriver> fluentWait;

	protected Logger logger;

	public PageBase(WebDriver driver) {
		logger = Logger.getLogger(this.getClass());
		this.driver = driver;
		this.ngWebDriver = new NgWebDriver((JavascriptExecutor) driver);
		this.wait = new WebDriverWait(driver, Constants.getWaitForElementTimeout());
		this.fluentWait = new FluentWait<WebDriver>(driver).withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(10, TimeUnit.MILLISECONDS).ignoring(NoSuchElementException.class);
	}

	public PageBase(){

	}
	/**
	 * Method that returns the complet url to  the page
	 *  BASE_URL + url
	 * @return String complete
	 */
	public String getCompleteURL(){
		return BASE_URL+url;
	}

	/**
	 * Method is used to navigated to page complete url
	 *  BASE_URL + url
	 * @return String complete
	 */
	public void navigateToIt(){
		driver.get(getCompleteURL());
	}

	/**
	 * Method that returns the web driver
	 * 
	 * @return web driver
	 */
	public WebDriver getDriver() {
		return driver;
	}

	/**
	 * Method that returns the NG web driver for operations with angular
	 * 
	 * @return NG web driver
	 */
	public NgWebDriver getNgWebDriver() {
		return ngWebDriver;
	}

	/**
	 * Method that returns the default wait in our framework
	 * 
	 * @return web driver wait
	 */
	public WebDriverWait getWait() {
		return wait;
	}

	/**
	 * Method that returns the default fluent wait in our framework
	 * 
	 * @return wait
	 */
	public Wait<WebDriver> getFluentWait() {
		return fluentWait;
	}

	/**
	 * Method that obtains the element specific
	 * 
	 * @param locator
	 *            of the element; could be by xpath, id, name, etc
	 * @return web element
	 */
	public WebElement getWebElement(By locator) {

		return getWait().until((Function<? super WebDriver, WebElement>) ExpectedConditions.visibilityOfElementLocated(locator));
	}

	public List<WebElement> getWebElements(By locator) {
		return getWait().until((Function<? super WebDriver, List<WebElement>>) ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	}

	/**
	 * Method that clicks the element specific
	 * 
	 * @param locator
	 *            of the element to be clickable
	 */
	public void clickElement(By locator) {

		WebElement element = getWait().until((Function<? super WebDriver, WebElement>) ExpectedConditions.elementToBeClickable(locator));

		JavascriptExecutor jse = (JavascriptExecutor) driver;

		jse.executeScript("arguments[0].scrollIntoView()", element);
		element.click();
	}

	/**
	 * Method that clicks the element specific
	 * 
	 * @param webelement
	 *            to be clickable
	 */
	public void clickElement(WebElement element) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		jse.executeScript("arguments[0].scrollIntoView()", element);
		element.click();
	}

	/**
	 * Method that completes the input field specific with a value specific
	 * First: obtains the element, Second: clean the field, Third: complete the
	 * field.
	 * 
	 * @param locator
	 *            of the element to be completed
	 * @param value
	 *            that i want to write in the field
	 */
	public void completeField(By locator, String value) {
		WebElement element = getWebElement(locator);
		element.clear();
		element.sendKeys(value);
	}

	/**
	 * Method that get value of field s First: obtains the element, Second: get
	 * the value
	 * 
	 * @param locator
	 *            of the element to be completed
	 * @param value
	 *            that i want to write in the field
	 */
	public String getValueField(By locator) {
		WebElement element = getWebElement(locator);
		return element.getText();
	}

	/**
	 * Method that select the option specific from the dropdown First: open the
	 * dropdown, Second: complete the dropdown input, Third: select the option
	 * that provides the filter. NOTE: this does not work with dropdown menus
	 * that are not auto complete
	 * 
	 * @param dropdown
	 *            locator that i want to open
	 * @param value
	 *            of the option that i want to choose
	 */
	public void selectOptionDropdown(By dropdown, String value) {
		selectOptionDropdown(getWebElement(dropdown), value);
	}

	public void selectOptionDropdown(WebElement dropdownContainer, String value) {
		clickElement(dropdownContainer);

		getNgWebDriver().waitForAngularRequestsToFinish();

		WebElement inputLocator = getWait()
				.until((Function<? super WebDriver, WebElement>) ExpectedConditions.presenceOfNestedElementLocatedBy(dropdownContainer, By.tagName("input")));
		inputLocator.sendKeys(value);

		getNgWebDriver().waitForAngularRequestsToFinish();

		WebElement select = getWait().until((Function<? super WebDriver, WebElement>) ExpectedConditions.presenceOfNestedElementLocatedBy(dropdownContainer,
				By.cssSelector("div[role='option']")));
		select.click();
	}

	/**
	 * Method that select the option specific from the dropdown from the label,
	 * look inside the dom tree for the dropdown, the input, and the options,
	 * filter and select the desired option NOTE: this does not work with
	 * dropdown menus that are not auto complete
	 * 
	 * @param dropdownTitleTranslateKey
	 *            the of the title label
	 * @param value
	 *            of the option that i want to choose
	 */
	public void selectOptionDropdown(String dropdownTitleTranslateKey, String value) {

		// navegate to correct div "combo-container"
		WebElement comboContainer = getWebElement(
				By.cssSelector("span[translate=\"" + dropdownTitleTranslateKey + "\"]")).findElement(By.xpath("../.."));

		// click combo
		WebElement caret = comboContainer.findElement(By.cssSelector(".caret"));
		WebElement clickableCaretParent = caret.findElement(By.xpath("../../.."));
		clickableCaretParent.click();

		getNgWebDriver().waitForAngularRequestsToFinish();

		// sendKeys to search input
		comboContainer.findElement(By.cssSelector("input[type=search]")).sendKeys(value);

		getNgWebDriver().waitForAngularRequestsToFinish();

		// select first occurrence
		WebElement optionElement = comboContainer.findElement(By.cssSelector("div[role='option']"));
		optionElement.click();
	}

	/**
	 * Method that checks the option specific if it is not selected
	 * 
	 * @param locator
	 *            of the checkbox
	 */
	public void selectCheckbox(By locator) {
		WebElement checkbox = driver.findElement(locator);
		if (!checkbox.isSelected()) {
			checkbox.click();
		}
	}

	/**
	 * Method that unchecks the option specific if it is not unselected
	 * 
	 * @param locator
	 *            of the checkbox
	 */
	public void deselectCheckbox(By locator) {
		WebElement checkbox = driver.findElement(locator);
		if (checkbox.isSelected()) {
			checkbox.click();
		}
	}


	

	/**
	 * Method that verifies if the element specific is present in the window
	 * 
	 * @param locator
	 *            of the element specific
	 * @return true if the element is present, false otherwise
	 */
	public boolean isElementPresent(By locator) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			getDriver().findElement(locator);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
		}
	}
	
	/**
	 * Method that verifies if the element specific is present in the window
	 * 
	 * @param locator
	 *            of the element specific
	 * @return true if the element is present, false otherwise
	 */
	public boolean waitAndCheckElementPresent(By locator) {
		try {
			getWait().until((Function<? super WebDriver, ? extends Object>) ExpectedConditions.presenceOfElementLocated(locator));
			return true;
		} catch (TimeoutException e) {
			return false;
		}
	}
	
	/**
	 * Similar to does element exist, but also verifies that only one such
	 * element exists and that it is displayed
	 * 
	 * @param by
	 *            By statement locating the element.
	 * @return boolean if one and only one element matching the locator is
	 *         found, and if it is displayed and enabled, F otherwise.
	 */
	protected boolean isElementPresentAndDisplayed(By by) {
		boolean isPresent = false;
		// Temporarily set the implicit timeout to zero
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			// Check to see if there are any elements in the found list
			List<WebElement> elements = driver.findElements(by);
			isPresent = (elements.size() == 1) && elements.get(0).isDisplayed();
			// && elements.get(0).isEnabled();
		} finally {
			// Return to the original implicit timeout value
			driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
		}

		return isPresent;
	}

	/**
	 * Method that verifies if the locator specific is present in the element
	 * 
	 * @param element
	 * @param locator
	 * @return true if is present, false otherwise
	 */
	public boolean isElementPresent(WebElement element, By locator) {
		driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
		try {
			element.findElement(locator);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		} finally {
			driver.manage().timeouts().implicitlyWait(Constants.getWaitImlicitTimeout(), TimeUnit.SECONDS);
		}
	}

	/**
	 * Method that verifies if the input is not empty
	 * 
	 * @param inputLocator
	 * @return true if the input is not empty, false otherwise
	 */
	public boolean hasNotEmptyValue(By inputLocator) {
		return !getWebElement(inputLocator).getAttribute("value").isEmpty();
	}

	/**
	 * Method that produce a dynamic wait time waiting to finish an angular
	 * request
	 */
	public void angularWait() {
		this.ngWebDriver.waitForAngularRequestsToFinish();
	}

	/**
	 * Method that produce a dynamic wait time waiting to finish an angular
	 * request
	 * 
	 * @param WebDriver
	 *            for Angular
	 */
	public void angularWait(NgWebDriver ngDrv) {
		ngDrv.waitForAngularRequestsToFinish();
	}

	/**
	 * Method that drag and drop some element over other element
	 * 
	 * @param elementToDrag
	 * @param elementToReplace
	 */
	public void dragAndDrop(WebElement elementToDrag, WebElement elementToReplace) {
		JavascriptExecutor jse = (JavascriptExecutor) driver;

		jse.executeScript("arguments[0].scrollIntoView()", elementToReplace);

		new Actions(driver).dragAndDrop(elementToDrag, elementToReplace).perform();
	}

	/**
	 * Method thats click over option at spicified select or dropdown without
	 * suggest
	 * 
	 * @param selectContainerLocator
	 * @param optionText
	 */
	public void setOptionAtSelect(By selectContainerLocator, String optionText, Boolean searchByStartWithCriteria) {
		// first obtain context of select
		WebElement comboContainer = getWebElement(selectContainerLocator);

		JavascriptExecutor jse = (JavascriptExecutor) driver;

		// scrolling until the combo object is displayed
		jse.executeScript("arguments[0].scrollIntoView()", comboContainer);

		// click select and load initial values
		WebElement comboObject = comboContainer.findElement(By.cssSelector(".ui-select-match.ng-scope"));
		comboObject.click();
		angularWait();

		// scrolling and find inside the combo the correct options
		List<WebElement> options = getWebElements(By.cssSelector(".ui-select-choices-row-inner"));
		findOption(optionText, options, searchByStartWithCriteria).click();

	}
	
	public void setOptionAtSelect(By selectContainerLocator, String optionText) {
		setOptionAtSelect(selectContainerLocator, optionText, false);
	}
	
		
	/**
	 *
	 * @param optionText
	 * @param options
	 * @param searchCriteria can be "equals" or "startWith"
	 * @return
	 */
	private WebElement findOption(String optionText, List<WebElement> options, Boolean searchByStartWithCriteria) {
		int comboSize = 0;
		WebElement option = null;

		while (option == null || comboSize != options.size()) {
			comboSize = options.size();
			if (searchByStartWithCriteria) {
				option = Utils.searchElementStartWithText(options, optionText);
			}
			else{
				option = Utils.searchElementByText(options, optionText);
			}
			if (option == null) {
				Actions actions = new Actions(driver);
				actions.moveToElement(options.get(options.size() - 1));
				actions.perform();
				angularWait();
				options = getWebElements(By.cssSelector(".ui-select-choices-row-inner"));
			}
		}
		return option;
	}

	/**
	 * An expectation for checking that an element is either invisible or not
	 * present on the DOM.
	 *
	 * @param locator
	 *            used to find the element
	 */

	public static ExpectedCondition<Boolean> invisibilityOfElementLocated(final By locator) {
		return new ExpectedCondition<Boolean>() {
			@Override
			public Boolean apply(WebDriver driver) {
				try {

					return !(driver.findElement(locator).isDisplayed());

				} catch (TimeoutException e) {
					// Returns true because the element is not present in DOM.
					// The
					// try block checks if the element is present but is
					// invisible.
					return true;
				} catch (StaleElementReferenceException e) {
					// Returns true because stale element reference implies that
					// element
					// is no longer visible.
					return true;
				} catch (NoSuchElementException e) {
					// Returns false because it was reached the time and the
					// element
					// is visible.
					return false;
				}

			}
		};
	}

	/**
	 * Wait until an element disappear
	 */
	public void waitForElementDisappears(final By locator) {
		if (isElementPresent(locator)) {
			new WebDriverWait(driver, 20).until((Function<? super WebDriver, ? extends Object>) ExpectedConditions.invisibilityOfElementLocated(locator));
		}
	}

	/**
	 * Put a text 'value' on the clipboard
	 * 
	 * @param value String of text that is required put on th clipboard
	 */
	public void setTexttoClipboard(String value) {
		StringSelection stringSelection = new StringSelection(value);
		Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
		clpbrd.setContents(stringSelection, null);
	}


	public void openFileDialogSystemAndSelectFile(String filePath, By selectFileId){
		WebElement element = driver.findElement(selectFileId);
		element.sendKeys(filePath);
		try {
			Robot r = new Robot();

			r.delay(500);
			r.keyPress(KeyEvent.VK_ESCAPE);
			r.keyRelease(KeyEvent.VK_ESCAPE);

		} catch (AWTException e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * Wait to element dissappear
	 * 
	 * @param locator By element that it has to disappear
	 */
	public void waitUntilElementDissappear(By locator){
		if(isElementPresent(locator)){
			getWait().until((Function<? super WebDriver, ? extends Object>) ExpectedConditions.invisibilityOfElementLocated(locator));
		}
	}
	
	/**
	 * switch to the last tab
	 */
	public void switchToLastTab(){
		String lastHandle = "";
		for(String winHandle : getDriver().getWindowHandles()){
            lastHandle = winHandle;
        }
		getDriver().switchTo().window(lastHandle);
	}
	
	/**
	 * Wait to appear more than one tab
	 */
	public void waitTabOpening(){
    	int attemps = 0;
    	WebDriver wd =  getDriver();
    	while((wd.getWindowHandles().size() == 1) && attemps <= 30){
    		try {
    				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		attemps++;
    	}
    }
	
	/**
	 * Make a wait time informing the wait time.
	 * 
	 * @param waitSecond Seconds of the wait
	 * @param pInf period of time to show a message
	 * @param msg Message to show
	 */
	public void waitAndInformWaiting(int waitSeconds, int pInf, String msg) {
		LocalDateTime stime = LocalDateTime.now();
		LocalDateTime etime = null;
		int infMsg = 0;
		long goTime = 0;
    	while(goTime<waitSeconds){
    		etime = LocalDateTime.now();
    		goTime = stime.until(etime,  ChronoUnit.SECONDS);
    		try {
    				Thread.sleep(999);
    				if(infMsg==pInf) {
    					System.out.print(String.format(msg+" [%d]\n", goTime));
    					infMsg = 0;
    				} else {
    					infMsg++;
    				}
    				
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
    		
    	}
		
	}
	
	public boolean isElementEnabled(WebElement element){
		return element.isEnabled();
	}
	
	public void scrollToTop(){ 
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("scroll(0, -250);");
	}
	
	public void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Logger getLogger() {
		return logger;
	}
}
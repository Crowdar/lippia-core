package com.crowdar.util;

import org.openqa.selenium.WebDriver;

import com.crowdar.driver.DriverManager;

import io.github.sridharbandi.AccessibilityRunner;
import io.github.sridharbandi.util.Standard;

public class AccessibilityUtils {
	
    private static ThreadLocal<AccessibilityRunner> accessibilityRunner = new InheritableThreadLocal<>();
    private static ThreadLocal<String> name = new InheritableThreadLocal<>();

	public static void initialize(String _name) {
		WebDriver driver = DriverManager.getDriverInstance();
		initialize(driver, Standard.WCAG2AA, _name);
	}
	
	public static void initialize(WebDriver driver, Standard standard, String _name) {
		AccessibilityRunner _accessibilityRunner = new AccessibilityRunner(driver);
		_accessibilityRunner.setStandard(standard);
		accessibilityRunner.set(_accessibilityRunner);
		name.set(_name);
	}
	
	public static void excecute() {
		accessibilityRunner.get().execute(name.get());
	}

	public static void report() {
		 accessibilityRunner.get().generateHtmlReport();
	}
	
	
}

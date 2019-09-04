package com.crowdar.bdd.cukes;


import cucumber.api.testng.AbstractTestNGCucumberTests;
import org.openqa.selenium.WebDriver;


public class TestNgRunner extends AbstractTestNGCucumberTests {
    protected WebDriver driver;
    protected String BASE_URL = "http://crowdar.co.uk/";


}
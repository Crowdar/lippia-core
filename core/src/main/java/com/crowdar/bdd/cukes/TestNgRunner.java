package com.crowdar.bdd.cukes;


import org.openqa.selenium.WebDriver;

import cucumber.api.testng.AbstractTestNGCucumberTests;


public class TestNgRunner extends AbstractTestNGCucumberTests {
    protected WebDriver driver;
    protected String BASE_URL = "http://crowdar.co.uk/";


}
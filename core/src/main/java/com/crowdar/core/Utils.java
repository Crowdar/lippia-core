package com.crowdar.core;

import org.apache.commons.lang.RandomStringUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * this class represent all sorts of utility functions i need
 *
 * @author Juan Manuel Spoleti
 */
public class Utils {

    /**
     * Method that generates a random number
     *
     * @param amount of random that i want
     * @return random number
     */
    public static String getRandomNumber(int amount) {
        return RandomStringUtils.randomNumeric(amount);
    }

    public static String getRandomLetters(int amount) {
        return RandomStringUtils.randomAlphabetic(amount);
    }

    public static String getRandom(int amount, boolean letters, boolean numbers) {
        return RandomStringUtils.random(amount, letters, numbers);
    }

    public static String removeFirstAndLastChar(String text) {
        return text.substring(0, text.length() - 1).substring(1);
    }

    /**
     * Method that replace a string containing the dollar (US$) symbol and some spaces from the keyboard
     *
     * @param value
     * @return new value of the string in BigDecimal
     */
    public static BigDecimal replaceDollarWithNumber(String value) {
        String newValue = value.replaceAll(" ", "").replace("US$", "");
        return new BigDecimal(newValue);
    }

    /**
     * @param date
     * @param dateFormat
     * @return LocalDate parsed to String
     */
    public static String dateToString(LocalDate date, String dateFormat) {
        return date.format(DateTimeFormatter.ofPattern(dateFormat));
    }

    /**
     * @param date
     * @param dateFormat
     * @return String parsed to LocalDate
     */
    public static LocalDate stringToDate(String date, String dateFormat) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(dateFormat));
    }

    public static String convertDateToFormatMMDDYYYY(Date date) {
        DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

        return formatter.format(date);
    }

    /**
     * Author jcarames... me hago cargo de esto!
     *
     * @param driver
     * @param inputId
     * @param valueToSet
     */
    public static void setValueToNonEditableInput(WebDriver driver, String inputId, String valueToSet) {

        if (driver instanceof JavascriptExecutor) {
            ((JavascriptExecutor) driver)
                    .executeScript("document.getElementById('" + inputId + "').value='" + valueToSet + "'");
        }
    }

    public static int getOnlyNumbersFromString(String stringToReplace) {
        return Integer.parseInt(stringToReplace.replaceAll("\\D+", ""));
    }

    public static void deleteFileIfExists(String filePath) {
        try {
            Files.deleteIfExists(Paths.get(filePath));
        } catch (NoSuchFileException e) {
            System.out.println("No such file/directory exists");
        } catch (DirectoryNotEmptyException e) {
            System.out.println("Directory is not empty.");
        } catch (IOException e) {
            System.out.println("Invalid permissions.");
        }

        System.out.println("Deletion successful.");
    }

    public static boolean isTextFieldEmpty(WebElement element, String placeholder) {
        return element.getText().equals(placeholder);
    }

    public static List<Object> parseArrayToList(Object[] array) {
        return Arrays.asList(array);
    }

}
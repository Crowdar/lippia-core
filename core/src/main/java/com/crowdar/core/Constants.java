package com.crowdar.core;

public class Constants {

    private static final long FLUENT_WAIT_REQUEST_FREQUENCY_IN_MILLIS = 500;
    private static final long FLUENT_WAIT_SECONDS_TIMEOUT = 30;
    private static final long WAIT_TIMEOUT_IN_SECONDS = 20;
    private static final long WAIT_SCRIPT_TIMEOUT = 55;
    private static final long WAIT_IMPLICIT_TIMEOUT = 2;
    private static final long WAIT_FOR_ELEMENT = 30;
    private static final long WAIT_FOR_APP_START = 70;
    private static final long WAIT_FOR_FILE_DOWNLOAD = 10;

    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String COMPLETE_DATE_PATTERN = "MM/dd/yyyy hh:mm aa";

    public static long getFluentWaitRequestFrequencyInMillis() {
        String override = PropertyManager.getProperty("crowdar.wait.fluent.frecuency");
        return (override != null && !override.isEmpty()) ? Long.valueOf(override) : FLUENT_WAIT_REQUEST_FREQUENCY_IN_MILLIS;
    }

    public static long getWaitForFileDownloadInSeconds() {
        String override = PropertyManager.getProperty("crowdar.wait.file.download.timeout");
        return (override != null && !override.isEmpty()) ? Long.valueOf(override) : WAIT_FOR_FILE_DOWNLOAD;
    }

    public static long getFluentWaitTimeoutInSeconds() {
        String override = PropertyManager.getProperty("crowdar.wait.fluent.timeout");
        return (override != null && !override.isEmpty()) ? Long.valueOf(override) : FLUENT_WAIT_SECONDS_TIMEOUT;
    }

    public static long getWaitTimeoutInSeconds() {
        String override = PropertyManager.getProperty("crowdar.wait.timeout");
        return (override != null && !override.isEmpty()) ? Long.valueOf(override) : WAIT_TIMEOUT_IN_SECONDS;
    }

    public static long getWaitScriptTimeout() {
        String override = PropertyManager.getProperty("crowdar.wait.script.timeout");
        return (override != null && !override.isEmpty()) ? Long.valueOf(override) : WAIT_SCRIPT_TIMEOUT;
    }

    public static long getWaitImlicitTimeout() {
        String override = PropertyManager.getProperty("crowdar.wait.impicit.timeout");
        return (override != null && !override.isEmpty()) ? Long.valueOf(override) : WAIT_IMPLICIT_TIMEOUT;
    }

    public static long getWaitForElementTimeout() {
        String override = PropertyManager.getProperty("crowdar.wait.element.timeout");
        return (override != null && !override.isEmpty()) ? Long.valueOf(override) : WAIT_FOR_ELEMENT;
    }

    public static long getWaitForAppStart() {
        String override = PropertyManager.getProperty("crowdar.wait.appStart.timeout");
        return (override != null && !override.isEmpty()) ? Long.valueOf(override) : WAIT_FOR_APP_START;
    }

    public static String getSimpleDatePattern() {
        String override = PropertyManager.getProperty("crowdar.pattern.simpleDate");
        return (override != null && !override.isEmpty()) ? override : SIMPLE_DATE_FORMAT;
    }

    public static String getCompleteDatePattern() {
        String override = PropertyManager.getProperty("crowdar.pattern.completeDate");
        return (override != null && !override.isEmpty()) ? override : COMPLETE_DATE_PATTERN;
    }

}

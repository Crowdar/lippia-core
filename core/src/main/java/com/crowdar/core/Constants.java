package com.crowdar.core;

public class Constants {

    // WAITS
    private static final long FLUENT_WAIT_REQUEST_FREQUENCY_IN_MILLIS = 500;
    private static final long FLUENT_WAIT_SECONDS_TIMEOUT = 60;
    private static final long WAIT_TIMEOUT_IN_SECONDS = 20;
    private static final long WAIT_SCRIPT_TIMEOUT = 55;
    private static final long WAIT_IMPLICIT_TIMEOUT = 2;
    private static final long WAIT_FOR_ELEMENT = 30;
    private static final long WAIT_FOR_APP_START = 70;
    private static final long WAIT_FOR_FILE_DOWNLOAD = 10;

    // PATTERN
    private static final String SIMPLE_DATE_FORMAT = "MM/dd/yyyy";
    private static final String COMPLETE_DATE_PATTERN = "MM/dd/yyyy hh:mm aa";

    // KEYS
    public static final String SYSTEM_PROPERTY_REPORT_DIRNAME = "report.dirName";
    public static final String SYSTEM_PROPERTY_RUN_INSTANCE = "runInstance";
    public static final String SYSTEM_PROPERTY_FRAMEWORK_ROOT = "frameworkRoot";


    public static long getFluentWaitRequestFrequencyInMillis() {
        String overrided = PropertyManager.getProperty("crowdar.wait.fluent.frecuency");
        return (overrided != null && !overrided.isEmpty()) ? Long.valueOf(overrided) : FLUENT_WAIT_REQUEST_FREQUENCY_IN_MILLIS;
    }

    public static long getWaitForFileDownloadInSeconds() {
        String overrided = PropertyManager.getProperty("crowdar.wait.file.download.timeout");
        return (overrided != null && !overrided.isEmpty()) ? Long.valueOf(overrided) : WAIT_FOR_FILE_DOWNLOAD;
    }

    public static long getFluentWaitTimeoutInSeconds() {
        String overrided = PropertyManager.getProperty("crowdar.wait.fluent.timeout");
        return (overrided != null && !overrided.isEmpty()) ? Long.valueOf(overrided) : FLUENT_WAIT_SECONDS_TIMEOUT;
    }

    public static long getWaitTimeoutInSeconds() {
        String overrided = PropertyManager.getProperty("crowdar.wait.timeout");
        return (overrided != null && !overrided.isEmpty()) ? Long.valueOf(overrided) : WAIT_TIMEOUT_IN_SECONDS;
    }

    public static long getWaitScriptTimeout() {
        String overrided = PropertyManager.getProperty("crowdar.wait.script.timeout");
        return (overrided != null && !overrided.isEmpty()) ? Long.valueOf(overrided) : WAIT_SCRIPT_TIMEOUT;
    }

    public static long getWaitImlicitTimeout() {
        String overrided = PropertyManager.getProperty("crowdar.wait.impicit.timeout");
        return (overrided != null && !overrided.isEmpty()) ? Long.valueOf(overrided) : WAIT_IMPLICIT_TIMEOUT;
    }

    public static long getWaitForElementTimeout() {
        String overrided = PropertyManager.getProperty("crowdar.wait.element.timeout");
        return (overrided != null && !overrided.isEmpty()) ? Long.valueOf(overrided) : WAIT_FOR_ELEMENT;
    }

    public static long getWaitForAppStart() {
        String overrided = PropertyManager.getProperty("crowdar.wait.appStart.timeout");
        return (overrided != null && !overrided.isEmpty()) ? Long.valueOf(overrided) : WAIT_FOR_APP_START;
    }

    public static String getSimpleDatePattern() {
        String overrided = PropertyManager.getProperty("crowdar.pattern.simpleDate");
        return (overrided != null && !overrided.isEmpty()) ? overrided : SIMPLE_DATE_FORMAT;
    }

    public static String getCompleteDatePattern() {
        String overrided = PropertyManager.getProperty("crowdar.pattern.completeDate");
        return (overrided != null && !overrided.isEmpty()) ? overrided : COMPLETE_DATE_PATTERN;
    }

}

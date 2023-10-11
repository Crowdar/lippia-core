package io.lippia.api.utils;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The Polling class provides utility methods for polling and waiting for a condition to be met
 * within a specified timeout.
 */
public class Polling {

    /**
     * Polls a condition until it succeeds or a timeout is reached.
     *
     * @param supplier         A supplier that provides the value to be checked.
     * @param checkSuccess     A predicate that checks whether the condition is met.
     * @param timeout          The maximum time to wait for the condition to succeed, in milliseconds.
     * @param ignoreExceptions An optional array of exception types to be ignored during polling.
     * @param <T>              The type of the value to be checked.
     * @return The value that satisfies the condition.
     * @throws TimeoutException If the condition is not met within the specified timeout.
     */
    @SafeVarargs
    public static <T> T poll(Supplier<T> supplier,
                             Predicate<T> checkSuccess,
                             int timeout,
                             Class<? extends Exception>... ignoreExceptions) throws TimeoutException {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + timeout;

        while (System.currentTimeMillis() < endTime) {
            try {
                T response = supplier.get();
                if (checkSuccess.test(response)) {
                    return response;
                }
            } catch (Exception ex) {
                if (!isIgnoredException(ex, ignoreExceptions)) {
                    throw ex;
                }
            }
        }

        throw new TimeoutException("Timeout exceeded");
    }

    /**
     * Checks if an exception should be ignored during polling.
     *
     * @param ex                The exception to check.
     * @param ignoredExceptions An array of exception types to be ignored.
     * @return True if the exception should be ignored, false otherwise.
     */
    private static boolean isIgnoredException(Exception ex, Class<? extends Exception>[] ignoredExceptions) {
        for (Class<? extends Exception> ignoredException : ignoredExceptions) {
            if (ignoredException.isInstance(ex)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Custom exception class for indicating a timeout during polling.
     */
    public static class TimeoutException extends Exception {
        /**
         * Constructs a new TimeoutException with the specified detail message.
         *
         * @param message The detail message.
         */
        public TimeoutException(String message) {
            super(message);
        }
    }
}
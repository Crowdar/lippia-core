package com.crowdar.util;

import org.apache.commons.lang.WordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ValidateUtils {
    private static final String GETTER_NAME = "get";
    private static final String SORT_NAME_METHOD = "sort";

    private static final String ERROR_LIST_SIZES = "Size of the lists is not the same.";
    private static final String ERROR_INVALID_KEY = "%s field is invalid. Class: %s";
    private static final String ERROR_INVALID_FORMAT = "%s format is invalid. Class: %s";
    private static final String ERROR_METHOD_NOT_FOUND = "Method %s was not found in: %s";

    private static String methodName;

    private static String getMethodName() {
        return methodName;
    }

    private static void setMethodName(String methodName) {
        ValidateUtils.methodName = methodName;
    }

    public static <T> void validateFields(List<T> actualList, List<T> expectedList) throws Exception {
        Class expectedClass = expectedList.iterator().next().getClass();
        Class actualClass = actualList.iterator().next().getClass();
        sort(expectedList, expectedClass);
        sort(actualList, actualClass);
        FileUtils.writeListOutputs(actualList, expectedList);
        Assert.assertEquals(actualList.size(), expectedList.size(), ERROR_LIST_SIZES);
        validateListFields(actualList, expectedList);
    }

    private static <T> void sort(List<T> expectedList, Class expectedClass) throws IllegalAccessException, InvocationTargetException {
        try {
            expectedClass.getMethod(SORT_NAME_METHOD, List.class).invoke("", expectedList);
        } catch (NoSuchMethodException e) {
            LogManager.getLogger(ValidateUtils.class).warn(">>> 'sort' method was not found in ".concat(expectedClass.toGenericString()).concat(". Proceeding without sorting the list."));
        }
    }

    private static <T> void validateListFields(List<T> actualList, List<T> expectedList) throws Exception {
        int counter = 0;
        for (Object expected : expectedList) {
            Map<String, Object> expectedMap = MapUtils.convertObjectToMap(expected);
            Object actual = actualList.get(counter);
            validateFields(expectedMap, actual);
            counter++;
        }
    }

    public static void validateFields(Object expected, Object actual) throws Exception {
        Iterator<Map.Entry> expectedItr = MapUtils.getIterator(MapUtils.convertObjectToMap(expected));
        Class actualClass = actual.getClass();
        while (expectedItr.hasNext()) {
            Map.Entry<String, Object> entry = expectedItr.next();

            String expectedKey = entry.getKey();
            Object expectedValue = entry.getValue();

            setMethodName(GETTER_NAME.concat(getBasicKey(expectedKey)));
            Object actualValue = tryGetMethodValue(actual, actualClass, expectedKey);
            boolean isModel = isModelClass(actualValue);
            boolean isListModel = isListModelClass(actualValue);
            validateFieldsWhileModelList(expectedValue, actualValue, isListModel);
            validateFieldsWhileModel(expectedValue, actualValue, isModel);
            if (!isModel && !isListModel) {
                doAssertions(actualClass, expectedKey, expectedValue, actualValue);
            }
        }
    }

    private static void doAssertions(Class actualClass, String expectedKey, Object expectedValue, Object actualValue) {
        if (expectedValue != null && actualValue != null) {
            Assert.assertEquals(actualValue.getClass(), expectedValue.getClass(), String.format(ERROR_INVALID_FORMAT, expectedKey.toUpperCase(), actualClass.getSimpleName()));
        }
        Assert.assertEquals(actualValue, expectedValue, String.format(ERROR_INVALID_KEY, expectedKey.toUpperCase(), actualClass.getSimpleName()));
    }

    private static void validateFieldsWhileModel(Object expectedValue, Object actualValue, boolean isModel) throws Exception {
        while (isModel) {
            validateFields(expectedValue, actualValue);
            break;
        }
    }

    private static void validateFieldsWhileModelList(Object expectedValue, Object actualValue, boolean isListModel) throws Exception {
        while (isListModel) {
            List<Object> actualList = (List<Object>) actualValue;
            List<Object> expectedList = (List<Object>) expectedValue;
            validateFields(actualList, expectedList);
            break;
        }
    }

    private static boolean isModelClass(Object object) {
        return !(object == null || object instanceof String || object instanceof Integer || object instanceof Double || object instanceof Boolean || object instanceof Map || object instanceof Float || object instanceof BigDecimal || object instanceof List);
    }

    private static boolean isListModelClass(Object object) {
        if (object instanceof List) {
            List<Object> objects = (List<Object>) object;
            return isModelClass(objects.iterator().next());
        }
        return false;
    }

    private static Object tryGetMethodValue(Object actual, Class actualClass, String expectedKey) throws IllegalAccessException, InvocationTargetException {
        try {
            return getMethodValue(actual, actualClass, getMethodName());
        } catch (NoSuchMethodException e) {
            LogManager.getLogger(ValidateUtils.class).warn(">>> Method ".concat(getMethodName()).concat(" was not found. Trying with capitalize."));
            setMethodName(GETTER_NAME.concat(getFullKey(expectedKey)));
            return tryWithCapitalizeAndReplace(actual, actualClass);
        }
    }

    private static Object tryWithCapitalizeAndReplace(Object actual, Class actualClass) throws IllegalAccessException, InvocationTargetException {
        Object value = null;
        try {
            value = getMethodValue(actual, actualClass, getMethodName());
        } catch (NoSuchMethodException e1) {
            Assert.assertTrue(false, String.format(ERROR_METHOD_NOT_FOUND, getMethodName(), actualClass.toGenericString()));
        }
        return value;
    }

    private static String getFullKey(String expectedKey) {
        return WordUtils.capitalizeFully(expectedKey, new char[]{' ', '_', '.'}).replaceAll("\\s", "").replaceAll("_", "").replaceAll("\\.", "");
    }

    private static String getBasicKey(String expectedKey) {
        return WordUtils.capitalize(expectedKey).replaceAll("\\s", "");
    }

    private static Object getMethodValue(Object actual, Class actualClass, String methodName) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return actualClass.getMethod(methodName).invoke(actual);
    }
}

package com.crowdar.core;

import java.util.HashMap;
import java.util.Map;

/**
 * this class represents the way to save data and use them
 * throughout the test life cycle
 */
public class Context {

    public static final String CONTEXT_TEST_ID_KEY = "testid";
    public static final String CONTEXT_TEST_NAME_KEY = "testName";
    public static final String CONTEXT_STORY_NAME_KEY = "storyName";
    public static final String CONTEXT_METHOD_NAME_KEY = "CONTEXT_METHOD_NAME_KEY";
    public static String RETRY_COUNT = "RETRY_COUNT";

    private Context() {
    }

    private static Context instance;

    private Map<String, Object> data = new HashMap<>();

    public static Context getInstance() {
        if (instance == null)
            instance = new Context();
        return instance;
    }

    /**
     * Method that obtains the value from the key
     *
     * @param key of the value that i want
     * @return value of the key
     */
    public Object getData(String key) {
        return data.get(key);
    }

    /**
     * Method that stores a value with its respective key
     *
     * @param key   for the value
     * @param value that i want to store
     */
    public void setData(String key, Object value) {
        data.put(key, value);
    }


}

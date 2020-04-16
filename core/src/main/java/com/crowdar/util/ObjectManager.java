package com.crowdar.util;

import com.crowdar.core.MyThreadLocal;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ObjectManager {

    private static final String OBJECTS = "ObjectManager.Objects";

    private static <T> List<T> getObjects() {
        List<T> querysResult = (List<T>) MyThreadLocal.getData(OBJECTS);
        if (querysResult == null) {
            querysResult = new ArrayList();
            MyThreadLocal.setData(OBJECTS, querysResult);
        }
        return querysResult;
    }

    /**
     * Get Object in the position of the list specified
     *
     * @param position
     * @return Object
     */
    public static Object getObject(int position) {
        return getObjects().get(position);
    }

    /**
     * Get the first Object that returns the filter or return null
     *
     * @param object
     * @return First Object find or null
     */
    public static Object getObject(Class object) {
        return getObjects().stream()
                .filter(query -> query.getClass().equals(object))
                .findFirst().orElse(null);
    }

    /**
     * Gets all the Objects that match with the .class
     *
     * @param object .class
     * @return List of Objects
     */
    public static List<Object> getObjects(Class object) {
        return getObjects().stream()
                .filter(query -> query.getClass().equals(object))
                .collect(Collectors.toList());
    }

    /**
     * Add a new Object to the list.
     *
     * @param object
     */
    public static void addObject(Object object) {
        getObjects().add(object);
    }

    /**
     * Set Objects List to null. Recommend to use in After or Before methods.
     */
    public static void cleanObjects() {
        MyThreadLocal.setData(OBJECTS, null);
    }
}

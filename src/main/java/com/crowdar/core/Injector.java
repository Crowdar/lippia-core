package com.crowdar.core;

import com.crowdar.driver.DriverManager;
import com.crowdar.core.PageBase;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Injector {

    private static Map<Class, Object> cache = new HashMap<>();

    public static <T> T _page(Class<T> page) {
        try {
            PageBase pageBase = (PageBase) cache.get(page);
            if (pageBase == null || pageBase.getDriver().getSessionId() == null) {
                Constructor<?> constructor = page.getConstructor(RemoteWebDriver.class);
                Object o = constructor.newInstance(DriverManager.getDriverInstance());
                cache.put(page, o);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return (T) cache.get(page);
    }
}
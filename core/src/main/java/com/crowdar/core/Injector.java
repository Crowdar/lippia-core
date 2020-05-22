package com.crowdar.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.core.pageObjects.PageBase;
import com.crowdar.driver.DriverManager;

public class Injector {

    private static ThreadLocal<Map<Class, Object>> cache = new ThreadLocal<Map<Class, Object>>();
    
    public static <T> T _page(Class<T> page) {
    	if(cache.get()==null) {
    		cache.set(new HashMap<Class, Object>());
    	}
    	
        try {
            PageBase pageBase = (PageBase) cache.get().get(page);

            if (pageBase == null || pageBase.getDriver().getSessionId() == null) {
            	
                Constructor<?> constructor = page.getConstructor(RemoteWebDriver.class);
                Object o = constructor.newInstance(DriverManager.getDriverInstance());
                cache.get().put(page, o);
            }

//            String logTemplate = "######  %s - Thread id %s - objId: %s";
//            System.out.println(logTemplate.format(logTemplate, "INJECTOR",Thread.currentThread().getId(), cache.get().get(page).toString()));
            
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

        return (T) cache.get().get(page);
    }
    public static void cleanThreadCache() {
    	cache.remove();
    }
}
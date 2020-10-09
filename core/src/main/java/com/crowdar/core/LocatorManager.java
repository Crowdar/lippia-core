//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.crowdar.core;

import com.crowdar.core.pageObjects.LocatorTypesEnum;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class LocatorManager {
    private static Map<String, Properties> properties;

    private static Map<String, Properties> getProperties() {
        if (properties == null) {
            loadProperties();
        }
        return properties;
    }

    private static String getProperty(String property) {
        String[] split = property.split("\\.");
        String viewName = split[0].toUpperCase();
        String propertyKey = split[1];
        return getProperties().get(viewName).getProperty(propertyKey);
    }

    public static void loadProperties(String locatorsFolderPath) {
        properties = new HashMap<>();
        File dir = new File(locatorsFolderPath);
        String[] extensions = {"properties"};
        try {
            Collection<File> files = FileUtils.listFiles(dir, extensions, true);
            putProperties(files);
        } catch (IOException e) {
            Logger.getLogger(LocatorManager.class).error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private static void loadProperties() {
        loadProperties(Paths.get("src", "main", "resources", "locators").toString());
    }

    public static void loadProperties(Path locatorsFolderPath) {
        loadProperties(locatorsFolderPath.toString());
    }

    private static void putProperties(Collection<File> files) throws IOException {
        for (File file : files) {
            Properties newProperties = new Properties();
            InputStream inputStream = new FileInputStream(file);

            newProperties.load(inputStream);
            getProperties().put(file.getName().replace(".properties", "").toUpperCase(), newProperties);
        }
    }

    public static By getLocator(String locatorName) {
        try {
            String[] locatorProperty = getProperty(locatorName).split(":");
            int length = locatorProperty.length;
            if ( length>= 3) {
                for (int i = 1; i < length - 1;i++) {
                    locatorProperty[1] = locatorProperty[1].concat(":".concat(locatorProperty[i+1]));
                }
            }
            return getLocatorInEnum(locatorProperty);
        } catch (NullPointerException e){
            Logger.getLogger(LocatorManager.class).error(e.getMessage());
            throw new RuntimeException(String.format("Locator property %s was not found", locatorName));
        }
    }

    private static By getLocatorInEnum(String[] locatorProperty) {
        try {
            String type = locatorProperty[0].toUpperCase();
            String value = locatorProperty[1];
            return LocatorTypesEnum.get(type).getLocator(value);
        } catch (IndexOutOfBoundsException e) {
            Logger.getLogger(LocatorManager.class).error(e.getMessage());
            throw new RuntimeException("Locator property format is invalid. Example: css:#loginButton");
        }
    }
}

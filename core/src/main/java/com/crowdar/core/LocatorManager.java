//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.crowdar.core;

import com.crowdar.util.LoggerService;
import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.testng.Assert;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class LocatorManager {
    private static final String LOCATORS_FOLDER = "locators";
    private static Properties properties;
    private static String actualLocatorLoaded;

    private LocatorManager() {
    }

    private static Properties getProperties() {
        return properties;
    }

    public static String getProperty(String propertyKey, String locatorPath) {
        if(!actualLocatorLoaded.equals(locatorPath)){
            loadProperties(locatorPath);
        }
        return getProperty(propertyKey);
    }

    public static String getProperty(String propertyKey) {
        return getProperties().getProperty(propertyKey);
    }

    public static void loadProperties(String locatorPath) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        properties = new EncryptableProperties(encryptor);
        String[] locatorPathSplit = locatorPath.split("\\.");
        String locatorFilename = locatorPathSplit[locatorPathSplit.length - 1].concat(".properties");
        try {
            InputStream inputStream = getLocatorResourceAsStream(Paths.get(LOCATORS_FOLDER, locatorFilename));
            if (inputStream == null) {
                inputStream = getLocatorResourceAsStream(Paths.get(LOCATORS_FOLDER, locatorPathSplit[locatorPathSplit.length - 2], locatorFilename));
            }
            properties.load(inputStream);
            actualLocatorLoaded = locatorPath;
        } catch (IOException e) {
            LoggerService.getLogger(LocatorManager.class).error(e.getMessage());
            Assert.fail(e.getMessage());
        } catch (NullPointerException e){
            LoggerService.getLogger(LocatorManager.class).error(e.getMessage());
            Assert.fail(String.format("Locator file %s was not found: ", locatorFilename));
        }
    }

    private static InputStream getLocatorResourceAsStream(Path path) {
        return LocatorManager.class.getClassLoader().getResourceAsStream(path.toString());
    }
}

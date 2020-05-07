//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.crowdar.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

public class LocatorManager {
    private static final String LOCATORS_PROPERTY_KEY = "crowdar.locatorsProperties";
    private static Properties properties;

    private LocatorManager() {
    }

    private static Properties getProperties() {
        if (properties == null) {
            try {
                loadProperties();
            } catch (IOException var1) {
                var1.printStackTrace();
            }
        }

        return properties;
    }

    public static String getProperty(String propertyKey) {
        return getProperties().getProperty(propertyKey);
    }

    public static boolean isPropertyPresentAndNotEmpty(String propertyKey) {
        return getProperties().containsKey(propertyKey) && !getProperties().getProperty(propertyKey).isEmpty();
    }

    private static void loadProperties() throws IOException {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        properties = new EncryptableProperties(encryptor);
        InputStream inputStream = LocatorManager.class.getClassLoader().getResourceAsStream(PropertyManager.getProperty(LOCATORS_PROPERTY_KEY));
        properties.load(inputStream);
    }
}

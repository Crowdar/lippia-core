//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.crowdar.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.crowdar.driver.ProjectTypeEnum;

public class PropertyManager {
    private static Logger logger = Logger.getLogger(PropertyManager.class);
    private static final String PROPERTY_FILE_NAME = "config.properties";
    
    private static ThreadLocal<Properties> properties = new ThreadLocal<>();

    private PropertyManager() {
    }

    private static Properties getProperties() {
        if (properties.get() == null) {
            try {
                loadProperties();
            } catch (IOException var1) {
                var1.printStackTrace();
            }
        }

        return properties.get();
    }

    public static String getProperty(String propertyKey) {
        String value = getProperties().getProperty(propertyKey);
        return value;
    }

    public static boolean isPropertyPresentAndNotEmpty(String propertyKey) {
        return getProperties().containsKey(propertyKey) && !getProperties().getProperty(propertyKey).isEmpty();
    }

    private static void loadProperties() throws IOException {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        
        Properties clientProperties = new EncryptableProperties(encryptor);
        InputStream inputStream = PropertyManager.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
        clientProperties.load(inputStream);

        properties.set(getProjectTypeProperties(clientProperties));
        properties.get().putAll(clientProperties);
        
    }
	

	private static Properties getProjectTypeProperties(Properties clientProperties){
    	return ProjectTypeEnum.get(clientProperties.getProperty(ProjectTypeEnum.PROJECT_TYPE_KEY)).getProperties();
    } 
}

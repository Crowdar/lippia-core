//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.crowdar.core;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {
	private static Properties properties;
	private static final String PROPERTY_FILE_NAME = "config.properties";

	private PropertyManager() {
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

	private static void loadProperties() throws IOException {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		properties = new EncryptableProperties(encryptor);
		InputStream inputStream = PropertyManager.class.getClassLoader().getResourceAsStream(PROPERTY_FILE_NAME);
		properties.load(inputStream);
	}
}

package com.crowdar.core;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;

/**
 * this class is in charge of managing the configuration properties of the system 
 * @author jCarames	
 *
 */
public class PropertyManager {

	private static Properties properties;
	private static final String PROPERTY_FILE_NAME = "config.properties";
	
	private PropertyManager() {
	}
	
	/**
	 * 
	 * @return properties loaded
	 * @throws IOException
	 */
	private static Properties getProperties(){
		if(properties == null){
			try {
				loadProperties();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return properties;
	}
	
	/**
	 * 
	 * @param propertyKey
	 * @return property value
	 */
	public static String getProperty(String propertyKey){
		return getProperties().getProperty(propertyKey);
	}
	
	private static void loadProperties() throws IOException{
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		properties = new EncryptableProperties(encryptor);
		
		InputStream inputStream = new FileInputStream("src/main/resources/"+PROPERTY_FILE_NAME);
		properties.load(inputStream);
	}
}

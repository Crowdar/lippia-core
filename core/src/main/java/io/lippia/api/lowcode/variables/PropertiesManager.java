package io.lippia.api.lowcode.variables;


import com.crowdar.core.PropertyManager;
import io.lippia.api.lowcode.exception.LippiaException;

public class PropertiesManager {

    private PropertiesManager() {}

    public static String getProperty(String capture) {
        return propertyResolver(capture);
    }

    public static String propertyResolver(String property) {
        String propertyValue = PropertyManager.getProperty(property);
        if (propertyValue == null) {
            throw new LippiaException("Property ${" + property + "} does not exist in config.properties");
        }

        return propertyValue;
    }

}

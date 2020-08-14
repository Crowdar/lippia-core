package com.crowdar.email;

import com.crowdar.core.JsonUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

public enum EmailPropertiesEnum {

    SMTP {
        @Override
        public Properties getProperties(Properties properties) {
            setProperties("smtp", properties);
            return properties;
        }
    },
    POP3S {
        @Override
        public Properties getProperties(Properties properties) {
            setProperties("pop3s", properties);
            return properties;
        }
    },
    IMAP {
        @Override
        public Properties getProperties(Properties properties) {
            setProperties("imap", properties);
            return properties;
        }
    };

    protected void setProperties(String protocolName, Properties properties) {
        JSONObject propertiesJson = getPropertiesJson(protocolName);
        Set<String> propertyKeys = propertiesJson.keySet();
        for (String propertyKey : propertyKeys) {
            properties.put(propertyKey, propertiesJson.get(propertyKey));
        }
        properties.put("mail.store.protocol", protocolName);
    }

    protected JSONObject getPropertiesJson(String jsonName) {
        String json = null;
        Path path = Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "email", jsonName.concat(".json"));
        try {
            json = JsonUtils.getJSON(path);
        } catch (IOException e) {
            Logger.getLogger(this.getClass()).error(e.getMessage());
            Assert.fail(e.getMessage());
        }
        return new JSONObject(json);
    }

    public static EmailPropertiesEnum get(String key) {
        try {
            return Enum.valueOf(EmailPropertiesEnum.class, key);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid value for enum ProjectTypeEnum : " + key);
        }
    }

    public abstract Properties getProperties(Properties properties);
}

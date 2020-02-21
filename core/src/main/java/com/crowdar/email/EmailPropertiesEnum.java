package com.crowdar.email;

import com.crowdar.core.PropertyManager;

import java.util.Properties;

public enum EmailPropertiesEnum {

    SMTP {
        @Override
        public Properties getProperties(Properties properties) {
            basicConfiguration(properties);
            properties.put("mail.smtp.host", PropertyManager.getProperty("email.host"));
            properties.put("mail.smtp.port", PropertyManager.getProperty("email.port"));
            return properties;
        }
    },
    POP3S {
        @Override
        public Properties getProperties(Properties properties) {
            basicConfiguration(properties);
            properties.put("mail.pop3s.host", PropertyManager.getProperty("email.host"));
            properties.put("mail.pop3s.port", PropertyManager.getProperty("email.port"));
            return properties;
        }
    },
    IMAP {
        @Override
        public Properties getProperties(Properties properties) {
            basicConfiguration(properties);
            properties.put("mail.imap.host", PropertyManager.getProperty("email.host"));
            properties.put("mail.imap.port", PropertyManager.getProperty("email.port"));
            return properties;
        }
    };

    protected void basicConfiguration(Properties properties) {
        properties.put("mail.store.protocol", PropertyManager.getProperty("email.protocol"));
        properties.put("mail.imap.ssl.trust", "*");
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

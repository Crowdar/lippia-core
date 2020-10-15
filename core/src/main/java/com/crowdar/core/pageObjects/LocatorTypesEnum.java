package com.crowdar.core.pageObjects;

import io.appium.java_client.MobileBy;
import org.openqa.selenium.By;

public enum LocatorTypesEnum {

    CSS {
        @Override
        public By getLocator(String value) {
            return By.cssSelector(value);
        }
    },
    XPATH {
        @Override
        public By getLocator(String value) {
            return By.xpath(value);
        }
    },
    ID {
        @Override
        public By getLocator(String value) {
            return By.id(value);
        }
    },
    NAME {
        @Override
        public By getLocator(String value) {
            return By.name(value);
        }
    },
    CLASS {
        @Override
        public By getLocator(String value) {
            return By.className(value);
        }
    },
    TAG {
        @Override
        public By getLocator(String value) {
            return By.tagName(value);
        }
    },
    ACCESSIBILITY_ID {
        @Override
        public By getLocator(String value) {
            return MobileBy.AccessibilityId(value);
        }
    },
    ANDROID_UI_AUTOMATOR {
        @Override
        public By getLocator(String value) {
            return MobileBy.AndroidUIAutomator(value);
        }
    },
    IOS_NS_PREDICATE {
        @Override
        public By getLocator(String value) {
            return MobileBy.iOSNsPredicateString(value);
        }
    },
    CUSTOM {
        @Override
        public By getLocator(String value) {
            return MobileBy.custom(value);
        }
    },
    IMAGE {
        @Override
        public By getLocator(String value) {
            return MobileBy.image(value);
        }
    };

    public abstract By getLocator(String value);

    public static LocatorTypesEnum get(String key) {
        try {
            return Enum.valueOf(LocatorTypesEnum.class, key);

        } catch (IllegalArgumentException e) {
            throw new RuntimeException(String.format("Invalid value for enum LocatorTypes: %s", key));
        }
    }
}
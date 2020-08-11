package com.crowdar.util;

import org.apache.log4j.Logger;

public class LoggerService {

    public static Logger getLogger(Class className) {
        return Logger.getLogger(className);
    }
}

package com.crowdar.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.repackaged.com.google.common.base.Splitter;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class MapUtils {

    public static TreeMap<String, Object> sortMap(Object object) {
        return sortMap(convertObjectToMap(object));
    }

    public static TreeMap<String, Object> sortMap(Map map) {
        return new TreeMap<>(map);
    }

    public static Iterator<Map.Entry> getIterator(Map map) {
        return map.entrySet().iterator();
    }

    public static Map<String, Object> convertObjectToMap(Object object) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(object, Map.class);
    }

    public static Map<String, String> splitIntoMap(String inputParameters, String splitter, String keyValueSeparator) {
        Map<String, String> parameters = null;
        if (!inputParameters.isEmpty()) {
            parameters = Splitter.on(splitter).withKeyValueSeparator(keyValueSeparator).split(inputParameters);
        }
        return parameters;
    }
}

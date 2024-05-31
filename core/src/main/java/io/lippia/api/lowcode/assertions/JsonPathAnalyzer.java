package io.lippia.api.lowcode.assertions;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import java.util.LinkedList;

public class JsonPathAnalyzer {
    private JsonPathAnalyzer() {
    }

    private static final Configuration configuration;

    static {
        configuration = Configuration.builder()
                .options(Option.REQUIRE_PROPERTIES)
                .mappingProvider(new JacksonMappingProvider())
                .jsonProvider(new JacksonJsonProvider())
                .build();
    }

    public static String set(String jsonString, String jsonPath, String key, Object value) {
        DocumentContext context = JsonPath.using(configuration)
                .parse(jsonString);

        try {
            context.set(jsonPath + "." + key, value);
        } catch (PathNotFoundException ex) {
            return retry(context, jsonPath, key, value);
        }

        return context.jsonString();
    }

    private static String retry
            (DocumentContext context, String jsonPath, String key, Object value) {
        if (key.matches("^.*\\[\\d]$")) {
            String nKey = key.substring(0, key.length() - 3);
            context = context.put(jsonPath, nKey, new LinkedList<>());
            return context.add(jsonPath + "." + nKey, value).jsonString();
        } else {
            return context.put(jsonPath, key, value).jsonString();
        }
    }

    public static Object read(String jsonString, String jsonPath) {
        return JsonPath.using(configuration)
                .parse(jsonString)
                .read(jsonPath);
    }

    public static String delete(String jsonString, String jsonPath) {
        return JsonPath.using(configuration)
                .parse(jsonString)
                .delete(jsonPath)
                .jsonString();
    }
}


package io.lippia.api.lowcode.assertions;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

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

    public static <T> T read(String jsonString, String jsonPath, Class<T> classType) {
        return JsonPath.using(configuration)
                .parse(jsonString)
                .read(jsonPath, classType);
    }

    public static <T> T read(String jsonString, String jsonPath) {
        return JsonPath.using(configuration)
                .parse(jsonString)
                .<T>read(jsonPath);
    }
}


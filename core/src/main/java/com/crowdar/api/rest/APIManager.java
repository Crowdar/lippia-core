package com.crowdar.api.rest;

import com.crowdar.core.JsonUtils;
import com.crowdar.core.PropertyManager;

import java.io.File;
import java.io.IOException;

public class APIManager {

    public static final String BASE_URL = PropertyManager.getProperty("base.api.url");
    private static final ThreadLocal<Response> LAST_RESPONSE = new ThreadLocal<Response>();

    public static void setLastResponse(Response lastResponse) {
        LAST_RESPONSE.set(lastResponse);
    }

    public static Response getLastResponse() {
        return LAST_RESPONSE.get();
    }

    public static <T> T getResponseFromJsonFile(String jsonFileName, Class<T> valueType) throws IOException {
        return JsonUtils.getJSONFromFileAsObject("response".concat(File.separator).concat(jsonFileName), valueType);
    }
}

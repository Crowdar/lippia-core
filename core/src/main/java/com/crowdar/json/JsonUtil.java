package com.crowdar.json;

import com.crowdar.core.PropertyManager;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonUtil {

    public static final String JSON_PATH_PROPERTY = "crowdar.json.path";

    private static JsonUtil jsonUtil;

    public static JsonUtil i() {
        if (jsonUtil == null) {
            jsonUtil = new JsonUtil();
        }
        return jsonUtil;
    }

    private final String Json_Path = PropertyManager.getProperty(JSON_PATH_PROPERTY);
    private ObjectMapper mapper;

    public <T> T readObject(String json, Class<T> cl) {


        try {
            return getMapper().readValue(new File(getPath(json)), cl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getPath(String jsonName) {
        return getJson_Path() + jsonName;
    }

    public <T> List<T> readObjectList(String json) {
        try {
            return getMapper().readValue(new File(getPath(json)), List.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String getJson_Path() {
        return Json_Path;
    }

    public ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

    public void setMapper(ObjectMapper mapper) {
        this.mapper = mapper;
    }


}

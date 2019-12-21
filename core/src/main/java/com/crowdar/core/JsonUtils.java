package com.crowdar.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.collect.Lists;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();


    public static <T> T deserialize(String json, Class<T> type) {
        try {
            TypeFactory typeFactory = mapper.getTypeFactory();
            return (T) mapper.readValue(json, typeFactory.constructType(type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String serialize(Object json) {
        String jsonResult = null;
        try {
            jsonResult = mapper.writeValueAsString(Lists.newArrayList(json));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    /**
     * Given a Json file location,
     * this method returns that JSON as a String
     *
     * @param fileName
     * @return String (json)
     */
    public static String getJSONFromFile(String fileName) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            String path = System.getProperty("user.dir").concat(File.separator).concat("src").concat(File.separator).concat("test").concat(File.separator).concat("resources").concat(File.separator).concat("jsons").concat(File.separator).concat(fileName).concat(".json");
            FileInputStream fis = new FileInputStream(path);
            JsonNode rootNode = mapper.readTree(fis);
            json = rootNode.toString();
        } catch (IOException e) {
            System.out.println("JSON was not found " + fileName + " " + e.getMessage());
        }
        return json;
    }


    public static String prettyJsonToCompact(String prettyJson) {
        JsonNode jsonNode = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            jsonNode = objectMapper.readValue(prettyJson, JsonNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode.toString();
    }
}

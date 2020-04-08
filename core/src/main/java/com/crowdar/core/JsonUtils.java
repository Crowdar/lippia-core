package com.crowdar.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.TagType;
import com.github.jknack.handlebars.Template;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class JsonUtils {

    private static ObjectMapper mapper = new ObjectMapper();

    private static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }

    public static <T> T deserialize(String json, Class<T> type) {
        try {
            TypeFactory typeFactory = getMapper().getTypeFactory();
            return (T) mapper.readValue(json, typeFactory.constructType(type));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String serialize(Object json) {
        String jsonResult = null;
        try {
            jsonResult = getMapper().writeValueAsString(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResult;
    }

    public static boolean isJSONValid(Object jsonObject) {
        String jsonString = serialize(jsonObject);
        return isJSONValid(jsonString);
    }

    public static boolean isJSONValid(String json) {
        try {
            new JSONObject(json);
        } catch (JSONException ex) {
            try {
                new JSONArray(json);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Given a Json file location,
     * this method returns that JSON as a String
     *
     * @param fileName
     * @return String (json)
     */
    public static String getJSONFromFile(String fileName) {
        String path = System.getProperty("user.dir").concat(File.separator).concat("src").concat(File.separator).concat("test").concat(File.separator).concat("resources").concat(File.separator).concat("jsons").concat(File.separator).concat(fileName).concat(".json");
        return getJSONFromPath(path);
    }

    public static String getJSONFromPath(String path) {
        return getJSON(Paths.get(path));
    }

    public static <T> T getJSONFromFileAsObject(String file, Class<T> valueType) throws IOException {
        String json = getJSONFromFile(file);
        return getMapper().readValue(json, valueType);
    }

    public static <T> List<T> getListJSONFromFileAsObject(String file, Class<T> valueType) throws IOException {
        TypeFactory typeFactory = getMapper().getTypeFactory();
        String json = getJSONFromFile(file);
        return getMapper().readValue(json, typeFactory.constructCollectionType(List.class, valueType));
    }

    /**
     * Given a Json file location,
     * this method returns that JSON as a String
     *
     * @param file
     * @return String (json)
     */
    public static String getJSON(Path file) {
        ObjectMapper mapper = new ObjectMapper();
        String json = null;
        try {
            FileInputStream fis = new FileInputStream(file.toFile());
            JsonNode rootNode = mapper.readTree(fis);
            json = rootNode.toString();
        } catch (IOException e) {
            System.out.println("JSON was not found " + file + " " + e.getMessage());
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

    /**
     * @param jsonUnparsed json as String with Handlebars {{  }} to be replaced from propertyManager
     * @return String with replaced handlebars vars
     * @throws IOException
     */
    public static String replaceVarsFromPropertyManager(String jsonUnparsed) throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline(jsonUnparsed);
        List<String> vars = template.collect(TagType.VAR);
        for (String var : vars) {
            jsonUnparsed = jsonUnparsed.replace("{{" + var + "}}", PropertyManager.getProperty(var));
        }
        return jsonUnparsed;
    }


}

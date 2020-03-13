package com.crowdar.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.TagType;
import com.github.jknack.handlebars.Template;
import com.google.common.collect.Lists;

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
        String path = System.getProperty("user.dir").concat(File.separator).concat("src").concat(File.separator).concat("test").concat(File.separator).concat("resources").concat(File.separator).concat("jsons").concat(File.separator).concat(fileName).concat(".json");
        return getJSON(Paths.get(path));
    }

    /**
     * Given a Json file location,
     * this method returns that JSON as a String
     *
     * @param fileName
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

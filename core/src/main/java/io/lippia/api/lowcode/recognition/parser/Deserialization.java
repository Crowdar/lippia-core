package io.lippia.api.lowcode.recognition.parser;

import com.crowdar.core.JsonUtils;
import com.crowdar.util.XmlUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.lippia.api.lowcode.exception.LippiaException;

import java.io.File;
import java.io.IOException;

import static io.lippia.api.lowcode.variables.ParametersUtility.replaceVars;


public class Deserialization {

    private static final String DEFAULT_RESOURCES_PATH = System.getProperty("user.dir").concat(File.separator)
                                                       .concat("src").concat(File.separator)
                                                       .concat("test").concat(File.separator)
                                                       .concat("resources").concat(File.separator);

    private static final String DEFAULT_JSON_PATH = DEFAULT_RESOURCES_PATH.concat("jsons").concat(File.separator);
    private static final String DEFAULT_XML_PATH = DEFAULT_RESOURCES_PATH.concat("xmls").concat(File.separator);

    private Deserialization() {}

    @SuppressWarnings("this is used for schema validations")
    public static String getPathFromJsonPath(Types type) {
        return type.getSimplifyPath(DEFAULT_JSON_PATH);
    }

    @SuppressWarnings("this will be used for schema validations")
    public static String getPathFromXmlPath(Types type) {
        return type.getSimplifyPath(DEFAULT_XML_PATH);
    }

    public static JsonObject getJsonSource(Types type, String jsonName) {
        String sourcePath = type.getSimplifyPath(DEFAULT_JSON_PATH);
        try {
            String jsonString = JsonUtils.getJSONFromPath(sourcePath.concat(jsonName));
            return getJsonObject(jsonString);
        } catch (IOException e) {
            throw new LippiaException(e.getMessage());
        }
    }

    public static JsonObject getXmlSource(Types type, String xmlName) {
        String sourcePath = type.getSimplifyPath(DEFAULT_XML_PATH);
        try {
            String xmlString = XmlUtils.getXMLFromPath(sourcePath.concat(xmlName));
            return getJsonObject(xmlString);
        } catch (IOException e) {
            throw new LippiaException(e.getMessage());
        }
    }

    public static JsonObject getJsonObject(String jsonString) {
        jsonString = replaceVars(jsonString);
        return new Gson().fromJson(jsonString, JsonObject.class);
    }

}
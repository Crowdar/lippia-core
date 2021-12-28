package io.lippia.api.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.crowdar.core.PropertyManager;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.TagType;
import com.github.jknack.handlebars.Template;

public class XmlUtils {

    private static XmlMapper mapper;

    private static XmlMapper getMapper() {
        if (mapper == null) {
            mapper = new XmlMapper();
        }
        return mapper;
    }

    public static <T> T deserialize(String xml, Class<T> type) {
        try {
            TypeFactory typeFactory = getMapper().getTypeFactory();
            return (T) getMapper().readValue(xml, typeFactory.constructType(type));
        } catch (IOException e) {
            LogManager.getLogger(XmlUtils.class).error(e.getMessage());
        }
        return null;
    }

    public static String serialize(Object xml) {
        String jsonResult = null;
        try {
            jsonResult = getMapper().writeValueAsString(xml);
        } catch (IOException e) {
            LogManager.getLogger(XmlUtils.class).error(e.getMessage());
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
    public static String getXMLFromFile(String fileName) throws IOException {
        String path = System.getProperty("user.dir").concat(File.separator).concat("src").concat(File.separator).concat("test").concat(File.separator).concat("resources").concat(File.separator).concat("xmls").concat(File.separator).concat(fileName).concat(".xml");
        return getXMLFromPath(path);
    }

    public static String getXMLFromPath(String path) throws IOException {
        return getXML(Paths.get(path));
    }

    public static <T> T getXMLFromFileAsObject(String file, Class<T> valueType) throws IOException {
        String xml = getXMLFromFile(file);
        return getMapper().readValue(xml, valueType);
    }

    public static <T> List<T> getListXMLFromFileAsObject(String file, Class<T> valueType) throws IOException {
        TypeFactory typeFactory = getMapper().getTypeFactory();
        String xml = getXMLFromFile(file);
        return getMapper().readValue(xml, typeFactory.constructCollectionType(List.class, valueType));
    }

    /**
     * Given a xml file location,
     * this method returns that xml as a String
     *
     * @param file
     * @return String (xml)
     */
    public static String getXML(Path file) throws IOException {
        FileInputStream fis = new FileInputStream(file.toFile());
        JsonNode rootNode = getMapper().readTree(fis);
        return rootNode.toString();
    }


    /**
     * @param xmlUnparsed xml as String with Handlebars {{  }} to be replaced from propertyManager
     * @return String with replaced handlebars vars
     * @throws IOException
     */
    public static String replaceVarsFromPropertyManager(String xmlUnparsed) throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline(xmlUnparsed);
        List<String> vars = template.collect(TagType.VAR);
        for (String var : vars) {
        	xmlUnparsed = xmlUnparsed.replace("{{" + var + "}}", PropertyManager.getProperty(var));
        }
        return xmlUnparsed;
    }


}

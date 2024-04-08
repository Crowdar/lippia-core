package io.lippia.api.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.File;
import java.io.FileInputStream;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import io.lippia.api.lowcode.exception.LippiaException;

import org.apache.log4j.Logger;

import com.crowdar.core.PropertyManager;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.TagType;
import com.github.jknack.handlebars.Template;

import org.json.JSONException;
import org.json.XML;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class XmlUtils {

    private static XmlMapper mapper;

    private static XmlMapper getMapper() {
        if (mapper == null) {
            mapper = new XmlMapper();
        }
        return mapper;
    }

    public static boolean isXMLValid(String xml) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xml));
            builder.parse(inputSource);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isXSDValid(String xsd) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new StringReader(xsd)));

            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(new StringReader(xsd)));
            return true;
        } catch (SAXException | IOException e) {
            return false;
        }
    }

    public static String asJson(String xmlString) {
        if (isXMLValid(xmlString)) {
            try {
                return XML.toJSONObject(xmlString).toString();
            } catch (JSONException e) {
                throw new LippiaException(e.getMessage());
            }
        } else {
            throw new LippiaException("Invalid XML provided, \n\t%s", xmlString);
        }
    }

    public static <T> T deserialize(String xml, Class<T> type) {
        try {
            TypeFactory typeFactory = getMapper().getTypeFactory();
            return (T) getMapper().readValue(xml, typeFactory.constructType(type));
        } catch (IOException e) {
            Logger.getLogger(XmlUtils.class).error(e.getMessage());
        }
        return null;
    }

    public static String serialize(Object xml) {
        String jsonResult = null;
        try {
            jsonResult = getMapper().writeValueAsString(xml);
        } catch (IOException e) {
            Logger.getLogger(XmlUtils.class).error(e.getMessage());
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

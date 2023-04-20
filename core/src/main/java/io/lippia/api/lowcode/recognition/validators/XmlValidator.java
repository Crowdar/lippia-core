package io.lippia.api.lowcode.recognition.validators;

import org.json.JSONArray;
import org.json.JSONException;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

public class XmlValidator {

    private XmlValidator() {}

    private static final DocumentBuilderFactory documentBuilderFactory;

    static {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        documentBuilderFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_SCHEMA, "");
    }

    public static boolean isXMLValid(String xml) {
        try {
            documentBuilderFactory.newDocumentBuilder().parse(
                    new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return false;
        }

        return true;
    }

    public static boolean isJSONArrayValid(String jsonString) {
        try {
            new JSONArray(jsonString);
        } catch (JSONException var4) {
            return false;
        }

        return true;
    }
}
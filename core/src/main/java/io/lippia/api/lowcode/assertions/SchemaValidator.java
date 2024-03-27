package io.lippia.api.lowcode.assertions;

import com.fasterxml.jackson.databind.JsonNode;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import io.lippia.api.lowcode.exception.LippiaException;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import java.io.IOException;
import java.io.StringReader;

public class SchemaValidator {
    public static void validateJsonSchema(String jsonData, String jsonSchema) {
        try {
            JsonNode schNode = JsonLoader.fromString(jsonSchema);
            JsonNode datNode = JsonLoader.fromString(jsonData);

            JsonSchema sch = JsonSchemaFactory.byDefault().getJsonSchema(schNode);
            ProcessingReport report = sch.validate(datNode);
            if (!report.isSuccess()) {
                throw new LippiaException(report.toString());
            }
        } catch (ProcessingException | IOException e) {
            throw new LippiaException(e.getMessage());
        }
    }

    public static void validateXmlSchema(String xmlData, String xmlSchema) {
        try {
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new StreamSource(new StringReader(xmlSchema)));

            Validator validator = schema.newValidator();

            validator.validate(new StreamSource(new StringReader(xmlData)));
        } catch (SAXException | IOException e) {
            throw new LippiaException(e.getMessage());
        }
    }
}

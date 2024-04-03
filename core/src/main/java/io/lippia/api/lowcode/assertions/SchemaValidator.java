package io.lippia.api.lowcode.assertions;

import com.crowdar.util.JsonUtils;

import com.fasterxml.jackson.databind.JsonNode;

import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

import io.lippia.api.lowcode.exception.LippiaException;

import java.io.IOException;

public class SchemaValidator {
    private SchemaValidator() {
    }

    public static void validate(String data, String schema) {
        if (JsonUtils.isJSONValid(data)) {
            validateJsonSchema(data, schema);
        } else {
            throw new LippiaException("Content %s is not in the expected format", data);
        }
    }

    private static void validateJsonSchema(String jsonData, String jsonSchema) {
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
}

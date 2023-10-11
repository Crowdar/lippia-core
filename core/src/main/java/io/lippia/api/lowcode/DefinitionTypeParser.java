package io.lippia.api.lowcode;

import com.fasterxml.jackson.core.JsonProcessingException;

public abstract class DefinitionTypeParser {
    public abstract Object parse(Object... entries) throws JsonProcessingException;
}

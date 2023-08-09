package io.lippia.api.lowcode;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;

public abstract class PrimitiveDefinitionTypeParser extends DefinitionTypeParser {

    static class Byte extends PrimitiveDefinitionTypeParser {
        @Override
        public java.lang.Byte parse(java.lang.Object... entries) throws JsonProcessingException {
            entries[0] = entries[0].toString().replaceAll("0x$", "");
            return new ObjectMapper().readValue((java.lang.String) entries[0], java.lang.Byte.class);
        }
    }

    static class Boolean extends PrimitiveDefinitionTypeParser {
        @Override
        public java.lang.Boolean parse(java.lang.Object... entries) throws JsonProcessingException {
            return new ObjectMapper().readValue((java.lang.String) entries[0], java.lang.Boolean.class);
        }
    }

    static class Integer extends PrimitiveDefinitionTypeParser {
        @Override
        public java.lang.Integer parse(java.lang.Object... entries) throws JsonProcessingException {
            return new ObjectMapper().readValue((java.lang.String) entries[0], java.lang.Integer.class);
        }
    }

    static class Float extends PrimitiveDefinitionTypeParser {
        @Override
        public java.lang.Float parse(java.lang.Object... entries) throws JsonProcessingException {
            entries[0] = entries[0].toString().replaceAll("[fF]$", "");
            return new ObjectMapper().readValue((java.lang.String) entries[0], java.lang.Float.class);
        }
    }

    static class Double extends PrimitiveDefinitionTypeParser {
        @Override
        public java.lang.Double parse(java.lang.Object... entries) throws JsonProcessingException {
            entries[0] = entries[0].toString().replaceAll("[dD]$", "");
            return new ObjectMapper().readValue((java.lang.String) entries[0], java.lang.Double.class);
        }
    }

    static class Long extends PrimitiveDefinitionTypeParser {
        @Override
        public java.lang.Long parse(java.lang.Object... entries) throws JsonProcessingException {
            entries[0] = entries[0].toString().replaceAll("[lL]$", "");
            return new ObjectMapper().readValue((java.lang.String) entries[0], java.lang.Long.class);
        }
    }

    static class String extends PrimitiveDefinitionTypeParser {
        @Override
        public java.lang.String parse(java.lang.Object... entries) throws JsonProcessingException {
            return new ObjectMapper().readValue((java.lang.String) entries[0], java.lang.String.class);
        }
    }

    static class Array extends PrimitiveDefinitionTypeParser {
        @Override
        public java.lang.Object parse(java.lang.Object... entries) throws JsonProcessingException {
            return new ObjectMapper().readValue((java.lang.String) entries[0], ArrayList.class);
        }
    }

    static class Object extends PrimitiveDefinitionTypeParser {
        @Override
        public java.lang.Object parse(java.lang.Object... entries) throws JsonProcessingException {
            return new ObjectMapper().readValue((java.lang.String) entries[0], java.lang.Object.class);
        }
    }

}

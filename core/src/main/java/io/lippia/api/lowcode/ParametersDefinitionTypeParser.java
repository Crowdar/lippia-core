package io.lippia.api.lowcode;

import com.crowdar.core.EnvironmentManager;
import com.crowdar.core.PropertyManager;
import com.google.gson.Gson;
import io.lippia.api.lowcode.patterns.Patterns;
import io.lippia.api.lowcode.variables.VariablesManager;
import org.apache.commons.lang.NotImplementedException;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public abstract class ParametersDefinitionTypeParser extends DefinitionTypeParser {
    public static class Variable extends ParametersDefinitionTypeParser {
        @Override
        public Object parse(Object... entries) {
            String VARIABLE_REPLACE_PATTERN = "\\$\\(var.%s\\)";

            Matcher varsMatcher = Patterns.VARIABLE_PATTERN.matcher((String) entries[0]);
            while (varsMatcher.find()) {
                String keyVar = varsMatcher.group(1);
                Object keyVal = VariablesManager.getInstance().getVariable(keyVar);
                if (keyVal instanceof String) {
                    entries[0] = ((String) entries[0]).replaceAll
                            (String.format(VARIABLE_REPLACE_PATTERN, keyVar), (String) keyVal);
                    continue;
                }

                if (keyVal instanceof List || keyVal instanceof Map) {
                    entries[0] = ((String) entries[0]).replaceAll
                            ("\"?" + String.format(VARIABLE_REPLACE_PATTERN, keyVar) + "\"?", new Gson().toJson(keyVal));
                    continue;
                }

                entries[0] = ((String) entries[0]).replaceAll
                        ("\"?" + String.format(VARIABLE_REPLACE_PATTERN, keyVar) + "\"?", keyVal.toString());
            }

            return entries[0];
        }
    }

    public static class EnvironmentProperty extends ParametersDefinitionTypeParser {
        @Override
        public Object parse(Object... entries) {
            String ENV_PROPERTY_REPLACE_PATTERN = "\\$\\(env.%s\\)";

            Matcher environmentPropertyMatcher = Patterns.ENVIRONMENT_PATTERN.matcher((String) entries[0]);
            while (environmentPropertyMatcher.find()) {
                String property = environmentPropertyMatcher.group(1);
                entries[0] = ((String) entries[0]).replaceAll(String.format(ENV_PROPERTY_REPLACE_PATTERN, property), EnvironmentManager.getProperty(property));
            }

            return entries[0];
        }
    }

    public static class FileProperty extends ParametersDefinitionTypeParser {
        @Override
        public Object parse(Object... entries) {
            String FILE_PROPERTY_REPLACE_PATTERN = "\\$\\(pro.%s\\)";

            Matcher filePropertyMatcher = Patterns.PROPERTY_PATTERN.matcher((String) entries[0]);
            while (filePropertyMatcher.find()) {
                String fileProperty = filePropertyMatcher.group(1);
                entries[0] = ((String)entries[0]).replaceAll(String.format(FILE_PROPERTY_REPLACE_PATTERN), PropertyManager.getProperty(fileProperty));
            }

            return entries[0];
        }
    }

    @SuppressWarnings("technical debt")
    public static class XLSXProperty extends ParametersDefinitionTypeParser {
        @Override
        public Object parse(Object... entries) {
            throw new NotImplementedException();
        }
    }

    public static class CSVProperty extends ParametersDefinitionTypeParser {
        @Override
        public Object parse(Object... entries) {
            throw new NotImplementedException();
        }
    }

    public static class RDBProperty extends ParametersDefinitionTypeParser {
        @Override
        public Object parse(Object... entries) {
            throw new NotImplementedException();
        }
    }

    public static class NRDBProperty extends ParametersDefinitionTypeParser {
        @Override
        public Object parse(Object... entries) {
            throw new NotImplementedException();
        }
    }
}

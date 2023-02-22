package io.lippia.api.lowcode.variables;

import io.lippia.api.lowcode.EnvironmentManager;

import java.util.regex.Matcher;

import static io.lippia.api.lowcode.recognition.validators.Patterns.*;
import static io.lippia.api.lowcode.variables.PropertiesManager.getProperty;
import static io.lippia.api.lowcode.variables.VariablesManager.getVariable;


public class ParametersUtility {

    private ParametersUtility() {}

    private static final String VARIABLE_REPLACE_PATTERN = "[{]{2}%s[}]{2}";

    @SuppressWarnings("technical debt > {L:23-31}")
    public static String replaceVars(String str) {
        Matcher varsMatcher = VARIABLE_PATTERN.matcher(str);
        while (varsMatcher.find()) {
            String variable = varsMatcher.group();
            if (getVariable(variable).matches("\\d+\\.?\\d*")) {
                str = str.replaceAll("\"" + String.format(VARIABLE_REPLACE_PATTERN, variable) + "\"", getVariable(variable));
            }

            if (!getVariable(variable).matches("[^\"].*[^\"]")) {
                VariablesManager.setVariable(variable, getVariable(variable).replace("\"", ""));
            }

            str = str.replaceAll(String.format(VARIABLE_REPLACE_PATTERN, variable), getVariable(variable));
        }

        varsMatcher = PROPERTY_PATTERN.matcher(str);
        while (varsMatcher.find()) {
            String property = varsMatcher.group();
            str = str.replaceAll("\\$\\{" + property + "}", getProperty(property));
        }
        varsMatcher = ENV_MANAGER_PROPERTY_PATTERN.matcher(str);
        while (varsMatcher.find()) {
            String envProperty = varsMatcher.group();
            str = str.replace("env." + envProperty, EnvironmentManager.getProperty(envProperty));
            System.out.println(str);
        }
        if (VARIABLE_PATTERN.matcher(str).find() || PROPERTY_PATTERN.matcher(str).find()) {
            replaceVars(str);
        }

        return str;
    }

}
package com.crowdar.core;

import com.crowdar.core.PropertyManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValue;

import io.lippia.api.lowcode.exception.LippiaException;

public class EnvironmentManager {
    private static final String definedEnvironment;
    private static final JsonObject resources;
    private static JsonObject resource;

    private EnvironmentManager() {}

    static {
        definedEnvironment = PropertyManager.getProperty("environment");
        resources = parseFileAsJsonObject(loadFileConfiguration());
    }

    private static Config loadFileConfiguration() {
        return ConfigFactory.parseResources("lippia.conf");
    }

    private static JsonObject parseFileAsJsonObject(Config configuration) {
        ConfigValue configValue = configuration.getValue("environments");
        String jsonString = configValue.render(ConfigRenderOptions.concise());
        return new Gson().fromJson(jsonString, JsonObject.class);
    }

    public static String getProperty(String property) {
        parseResources();
        if (resource.has(property)) {
            return resource.getAsJsonPrimitive(property).getAsString();
        }

        throw new LippiaException("Property {" + property + "} has not been defined in " + definedEnvironment + " in lippia.conf");
    }

    private static void parseResources() {
        String[] environments = definedEnvironment.split("#");
        for (String env: environments) {
            parseResources(env);
        }
    }

    private static void parseResources(String env) {
        if (resources.has(env) && resource == null) {
            resource = resources.getAsJsonObject(env);
        } else if (resource.has(env)) {
            resource = resource.getAsJsonObject(env);
        }

        if (resource != null) return;

        throw new LippiaException(env + " environment have not been defined in lippia.conf");
    }
}
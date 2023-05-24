package com.crowdar.core;


import com.crowdar.core.PropertyManager;
import com.google.gson.JsonObject;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import com.typesafe.config.ConfigValue;
import io.lippia.api.lowcode.recognition.parser.Deserialization;

public class EnvironmentManager {

    private static String definedEnvironment;
    private static JsonObject resources;
    private static JsonObject resource;

    static {
        definedEnvironment = PropertyManager.getProperty("environment");
        resources          = deserialize(loadConfiguration());
        resource           = mapResources();
    }

    private EnvironmentManager() {}

    private static Config loadConfiguration() {
        return ConfigFactory.parseResources("lippia.conf");
    }

    private static JsonObject deserialize(Config configuration) {
        ConfigValue configValue = configuration.getValue("environments");
        String jsonString = configValue.render(ConfigRenderOptions.concise());
        return Deserialization.getJsonObject(jsonString);
    }

    public static String getProperty(String property) {
        if (resource.has(property)) {
            return resource.getAsJsonPrimitive(property).getAsString();
        }

        throw new RuntimeException("Property {" + property + "} has not been defined in " + definedEnvironment + " in lippia.conf");
    }

    private static JsonObject mapResources() {
        if (resources.has(definedEnvironment)) {
            resource = resources.getAsJsonObject(definedEnvironment);
        } else if (resources.has("default")) {
            resource = resources.getAsJsonObject("default");
        }

        if (!resource.isJsonNull()) return resource;

        throw new RuntimeException(definedEnvironment + " & default environments have not been defined in lippia.conf");
    }

}
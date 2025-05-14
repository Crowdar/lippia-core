package io.lippia.api.lowcode.configuration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import io.lippia.api.configuration.EndpointConfiguration;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static io.lippia.api.lowcode.Engine.evaluateExpression;

public enum ConfigurationType {

    BASE_URL       (EndpointConfiguration::url),
    URL_PARAMETER  (EndpointConfiguration::urlParameter),
    ENDPOINT       (EndpointConfiguration::endpoint),
    HEADERS        (EndpointConfiguration::header),
    BODY           (EndpointConfiguration::body),
    HTTP_METHOD    (EndpointConfiguration::method);

    Function<String, String> configResolver                    = null;
    Consumer<String> endpointConfigurationConsumer             = null;
    BiConsumer<String, String> endpointConfigurationBiConsumer = null;

    ConfigurationType(BiConsumer<String, String> endpointConfigurationBiConsumer) {
        this.endpointConfigurationBiConsumer = endpointConfigurationBiConsumer;
    }

    ConfigurationType(Consumer<String> endpointConfigurationConsumer) {
        this.endpointConfigurationConsumer = endpointConfigurationConsumer;
    }

    public void assign(String key, String value) {
        value = evaluateExpression(value).toString();
        if (configResolver != null) {
            value = configResolver.apply(value);
        }

        endpointConfigurationBiConsumer.accept(key, value);
    }

    public void assign(String assignment) {
        Object evalExpr = evaluateExpression(assignment);
        if (evalExpr instanceof List || evalExpr instanceof Map) {
            assignment = new Gson().toJson(evalExpr);
        } else assignment = (String) evalExpr;

        if (configResolver != null) {
            assignment = configResolver.apply(assignment);
        }

        endpointConfigurationConsumer.accept(assignment);
    }

    public void assign(JsonObject json) {
        json.keySet().forEach(keyStr -> {
            Object keyValue = json.get(keyStr);
            if (keyValue instanceof JsonObject) {
                assign((JsonObject) keyValue);
            } else {
                assign(keyStr, String.valueOf(keyValue));
            }
        });
    }

}
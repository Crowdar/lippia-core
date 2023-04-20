package io.lippia.api.lowcode.recognition.validators;


import io.lippia.api.configuration.EndpointConfiguration;

import static io.lippia.api.lowcode.variables.ParametersUtility.replaceVars;

public class Syntax {

    private Syntax() {}

    public static String endpointResolver(String endpoint) {
        if (!endpoint.startsWith("/") &&
                !EndpointConfiguration.getInstance().getHttConfiguration().getUrl().endsWith("/")) {
            endpoint = "/".concat(endpoint);
        }

        if (endpoint.contains("/")) {
            String[] splitEndpoint = endpoint.split("/");
            String endpoints = "";
            for (String s : splitEndpoint) {
                s = s.trim();
                s = replaceVars(s);
                endpoints = endpoints.concat("/" + s);
            }

            endpoint = endpoints;
        }

        return endpoint;
    }

}
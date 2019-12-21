package com.crowdar.api.rest;

import com.crowdar.core.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;

public class Request {

    private Object body;
    private String url;
    private String endpoint;
    private Object headers;
    private Object urlParameters;

    public Request() {
        super();
    }

    public String getUrl() {
        return url;
    }

    /*
     * Remplaza en la url los valores {} por la variable enviada
     */
    public String getUrl(String var) {
        return url.replace("{}", var);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Object getBody() {
        return this.convertToJson(body);
    }

    public Object getHeaders() {
        return this.convertToJson(headers);
    }

    public void setHeaders(Object headers) {
        this.headers = headers;
    }

    public HashMap<String, String> getUrlParameters() {
        if (urlParameters == null || urlParameters.toString().isEmpty()) {
            return new HashMap<>();
        }
        try {
            return (HashMap) (new ObjectMapper()).readValue(this.convertToJson(urlParameters), HashMap.class);
        } catch (IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public void setUrlParameters(Object urlParameters) {
        this.urlParameters = urlParameters;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    //TODO: moderlo a JsonUtils y un nombre mas explicativo, por ej: convertObjectToJson
    private String convertToJson(Object jsonObject) {
        String json = JsonUtils.serialize(jsonObject);
        return json.substring(0, json.length() - 1).substring(1);
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}

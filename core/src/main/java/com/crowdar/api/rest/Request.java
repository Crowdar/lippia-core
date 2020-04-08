package com.crowdar.api.rest;

import com.crowdar.core.JsonUtils;
import com.crowdar.core.Utils;
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

    //TODO: hasta el momento solo soporta envio de json y text. CREO que html, xml NO, pero no estoy seguro.
    public Object getBody() {
        if (body == null) {
            return "";
        } else if (JsonUtils.isJSONValid(body)) {
            return JsonUtils.serialize(body);
        } else {
            return body.toString();
        }
    }

    public Object getHeaders() {
        return JsonUtils.serialize(headers);
    }

    public void setHeaders(Object headers) {
        this.headers = headers;
    }

    public HashMap<String, String> getUrlParameters() {
        if (urlParameters == null || urlParameters.toString().isEmpty()) {
            return new HashMap<>();
        }
        try {
            return (HashMap) (new ObjectMapper()).readValue(JsonUtils.serialize(urlParameters), HashMap.class);
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

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }
}

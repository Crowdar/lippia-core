package com.crowdar.api.rest;

import com.crowdar.core.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;

import static com.crowdar.api.rest.APIManager.BASE_URL;

public class Request {

    private Object body;
    private String url;
    private String endpoint;
    private Object headers;
    private Object urlParameters;

    public Request() {
        super();
    }

    public String getCompleteUrl() {
        String completeUrl = url;
        if (StringUtils.isEmpty(url)) {
            completeUrl = BASE_URL;
        }
        if (!StringUtils.isEmpty(endpoint)) {
            completeUrl = completeUrl.concat(endpoint);
        }
        return completeUrl;
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

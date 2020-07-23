package com.crowdar.api.rest;

import com.crowdar.core.JsonUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.crowdar.api.rest.APIManager.BASE_URL;

public class Request {

    private Object body;
    private String url;
    private String endpoint;
    private Map<String, String> headers;
    private Map<String, String> urlParameters;

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

    public Map<String, String> getHeaders() {
        if(MapUtils.isEmpty(headers)){
            headers = new HashMap<>();
        }
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public void addHeader(String key, String value){
        getHeaders().put(key, value);
    }

    public Map<String, String> getUrlParameters() {
        if (MapUtils.isEmpty(urlParameters)) {
            urlParameters =  new HashMap<>();
        }
        return urlParameters;
    }

    public void addUrlParameter(String key, String value){
        getUrlParameters().put(key, value);
    }

    public void setUrlParameters(Map<String, String> urlParameters) {
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

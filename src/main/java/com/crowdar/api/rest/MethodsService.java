package com.crowdar.api.rest;

import com.crowdar.core.JsonUtils;

import static com.crowdar.api.rest.APIManager.BASE_URL;
import static com.crowdar.api.rest.APIManager.setLastResponse;

public class MethodsService {

    private static Request getRequest(String jsonFileName) {
        String jsonRequest = JsonUtils.getJSONFromFile(jsonFileName);
        return JsonUtils.deserialize(jsonRequest, Request.class);
    }

    public static <T> Response get(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName);
        Response resp = new RestClient().get(BASE_URL, classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response post(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName);
        Response resp = new RestClient().post(BASE_URL, classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response patch(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName);
        Response resp = new RestClient().patch(BASE_URL, classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response delete(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName);
        Response resp = new RestClient().delete(BASE_URL, classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

}

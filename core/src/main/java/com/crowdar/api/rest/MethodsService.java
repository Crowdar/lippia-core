package com.crowdar.api.rest;

import static com.crowdar.api.rest.APIManager.BASE_URL;
import static com.crowdar.api.rest.APIManager.setLastResponse;

import java.util.Map;

import com.crowdar.core.JsonUtils;

public class MethodsService {

    private static RestClient restClient;

    private static RestClient getRestClient() {
        if (restClient == null) {
            restClient = new RestClient();
        }
        return restClient;
    }

    public static <T> Response get(Request req, Class<T> classModel) {
        Response resp = getRestClient().get(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response get(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName, null);
        return get(req, classModel);
    }

    public static <T> Response get(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        return get(req, classModel);
    }

    public static <T> Response post(Request req, Class<T> classModel) {
        Response resp = getRestClient().post(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response post(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName, null);
        return post(req, classModel);
    }


    public static <T> Response post(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        return post(req, classModel);
    }

    public static <T> Response put(Request req, Class<T> classModel) {
        Response resp = getRestClient().put(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response put(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName, null);
        return put(req, classModel);
    }


    public static <T> Response put(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        return put(req, classModel);
    }

    public static <T> Response patch(Request req, Class<T> classModel) {
        Response resp = getRestClient().patch(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response patch(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName, null);
        return patch(req, classModel);
    }

    public static <T> Response patch(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        return patch(req, classModel);
    }

    public static <T> Response delete(Request req, Class<T> classModel) {
        Response resp = getRestClient().delete(getURL(req), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response delete(String jsonName, Class<T> classModel) {
        Request req = getRequest(jsonName, null);
        return delete(req, classModel);
    }

    public static <T> Response delete(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        return delete(req, classModel);
    }

    protected static Request getRequest(String jsonFileName, Map<String, String> replacementParameters) {
        String jsonRequest = JsonUtils.getJSONFromFile(jsonFileName);

        if (replacementParameters != null) {
            for (String key : replacementParameters.keySet()) {
                jsonRequest = jsonRequest.replace("{{" + key + "}}", replacementParameters.get(key));
            }
        }

        return JsonUtils.deserialize(jsonRequest, Request.class);
    }

    private static String getURL(Request req) {
        String url = req.getUrl();
        String endpoint = req.getEndpoint();

        if (url == null || url.isEmpty()) {
            url = BASE_URL;
        }

        if (endpoint == null) {
            endpoint = "";
        }

        return url + endpoint;
    }

}

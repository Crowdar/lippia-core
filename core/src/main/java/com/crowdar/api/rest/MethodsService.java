package com.crowdar.api.rest;

import com.crowdar.core.JsonUtils;
import com.crowdar.util.ValidateUtils;

import java.util.List;
import java.util.Map;

import static com.crowdar.api.rest.APIManager.setLastResponse;

public class MethodsService {

    private static RestClient restClient;

    private static RestClient getRestClient() {
        if (restClient == null) {
            restClient = new RestClient();
        }
        return restClient;
    }

    public static <T> Response get(Request req, Class<T> classModel) {
        Response resp = getRestClient().get(req.getCompleteUrl(), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response get(String jsonName, Class<T> classModel) {
        return get(jsonName, classModel, null);
    }

    public static <T> Response get(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        return get(req, classModel);
    }

    public static <T> Response post(Request req, Class<T> classModel) {
        Response resp = getRestClient().post(req.getCompleteUrl(), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response post(String jsonName, Class<T> classModel) {
        return post(jsonName, classModel, null);
    }

    public static <T> Response post(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        return post(req, classModel);
    }

    public static <T> Response put(Request req, Class<T> classModel) {
        Response resp = getRestClient().put(req.getCompleteUrl(), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response put(String jsonName, Class<T> classModel) {
        return put(jsonName, classModel, null);
    }


    public static <T> Response put(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        return put(req, classModel);
    }

    public static <T> Response patch(Request req, Class<T> classModel) {
        Response resp = getRestClient().patch(req.getCompleteUrl(), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response patch(String jsonName, Class<T> classModel) {
        return patch(jsonName, classModel, null);
    }

    public static <T> Response patch(String jsonName, Class<T> classModel, Map<String, String> jsonParameters) {
        Request req = getRequest(jsonName, jsonParameters);
        return patch(req, classModel);
    }

    public static <T> Response delete(Request req, Class<T> classModel) {
        Response resp = getRestClient().delete(req.getCompleteUrl(), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders().toString());
        setLastResponse(resp);
        return resp;
    }

    public static <T> Response delete(String jsonName, Class<T> classModel) {
        return delete(jsonName, classModel, null);
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

    public static <T> void validateFields(List<T> actualList, List<T> expectedList) throws Exception {
        ValidateUtils.validateFields(actualList, expectedList);
    }

    public static void validateFields(Object actual, Object expected) throws Exception {
        ValidateUtils.validateFields(actual, expected);
    }
}

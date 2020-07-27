package com.crowdar.api.rest;

import com.crowdar.core.JsonUtils;
import com.crowdar.util.ValidateUtils;
import org.apache.log4j.Logger;
import org.testng.Assert;

import java.io.IOException;
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
        Response resp = getRestClient().get(req.getCompleteUrl(), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders());
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
        Response resp = getRestClient().post(req.getCompleteUrl(), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders());
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
        Response resp = getRestClient().put(req.getCompleteUrl(), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders());
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
        Response resp = getRestClient().patch(req.getCompleteUrl(), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders());
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
        Response resp = getRestClient().delete(req.getCompleteUrl(), classModel, req.getBody().toString(), req.getUrlParameters(), req.getHeaders());
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
        String jsonRequest = null;
        try {
            jsonRequest = JsonUtils.getJSONFromFile(jsonFileName);
        } catch (IOException e) {
            Logger.getRootLogger().error(e.getMessage());
            Assert.fail(e.getMessage());
        }

        if (replacementParameters != null) {
            for (String key : replacementParameters.keySet()) {
                jsonRequest = jsonRequest.replace("{{" + key + "}}", replacementParameters.get(key));
            }
        }
        Request request = JsonUtils.deserialize(jsonRequest, Request.class);
        Logger.getRootLogger().info(">>>Request: " + request.toString());
        return request;
    }

    /**
     * Generic validation. Do assertions for all the expected variables and write file outputs in target folder.
     * Validate two lists
     * Step: se obtuvo el response esperado en <entity> con el <jsonResponsePath>
     * expected response is obtained in '<entity>' with '<jsonResponsePath>'
     *
     * @param actualList
     * @param expectedList
     * @throws Exception
     */
    public <T> void validateFields(List<T> actualList, List<T> expectedList) throws Exception {
        ValidateUtils.validateFields(actualList, expectedList);
    }

    /**
     * Generic validation. Do assertions for all the expected variables and write file outputs in target folder.
     * Validate two objects
     * Step: se obtuvo el response esperado en <entity> con el <jsonResponsePath>
     * expected response is obtained in '<entity>' with '<jsonResponsePath>'
     *
     * @param actual
     * @param expected
     * @throws Exception
     */
    public void validateFields(Object actual, Object expected) throws Exception {
        ValidateUtils.validateFields(actual, expected);
    }

    /**
     * Set your own expected object, modifying expected object with parameters. Call validateFields(actual, expected); at the end.
     * Step: se obtuvo el response esperado en <entity> con el <jsonResponsePath> y sus parametros <parameters>
     * expected response is obtained in '<entity>' with '<jsonResponsePath>' and the parameters '<parameters>'
     *
     * @param actual
     * @param expected
     * @param parameters
     * @throws Exception
     */
    public void validateFields(Object actual, Object expected, Map<String, String> parameters) throws Exception {
    }

    /**
     * Set your own expected list, modifying expected list with parameters. Call validateFields(actual, expected); at the end.
     * Step: se obtuvo el response esperado en <entity> con el <jsonResponsePath> y sus parametros <parameters>
     * expected response is obtained in '<entity>' with '<jsonResponsePath>' and the parameters '<parameters>'
     *
     * @param actualList
     * @param expectedList
     * @param parameters
     * @throws Exception
     */
    public <T> void validateFields(List<T> actualList, List<T> expectedList, Map<String, String> parameters) throws Exception {
    }

    /**
     * Set your own expected object, modifying the json expected. Call validateFields(actual, expected); at the end.
     * Step: se obtuvo el response esperado en <entity> modificando el <jsonResponsePath>
     * expected response is obtained in '<entity>' modifying the '<jsonResponsePath>'
     *
     * @param jsonExpectedPath
     * @param actual
     * @throws Exception
     */
    public void validateFields(String jsonExpectedPath, Object actual) throws Exception {
    }

    /**
     * Set your own expected list, modifying the json expected. Call validateFields(actual, expected); at the end.
     * Step: se obtuvo el response esperado en <entity> modificando el <jsonResponsePath>
     * expected response is obtained in '<entity>' modifying the '<jsonResponsePath>'
     *
     * @param jsonExpectedPath
     * @param actualList
     * @throws Exception
     */
    public <T> void validateFields(String jsonExpectedPath, List<T> actualList) throws Exception {
    }

    /**
     * Set your own expected object, modifying the json expected with parameters. Call validateFields(actual, expected); at the end.
     * Step: se obtuvo el response esperado en <entity> modificando el <jsonResponsePath> y sus parametros <parameters>
     * expected response is obtained in '<entity>' modifying the '<jsonResponsePath>' and the parameters '<parameters>'
     *
     * @param jsonExpectedPath
     * @param actual
     * @param parameters
     * @throws Exception
     */
    public void validateFields(String jsonExpectedPath, Object actual, Map<String, String> parameters) throws Exception {
    }

    /**
     * Set your own expected list, modifying the json expected with parameters. Call validateFields(actual, expected); at the end.
     * Step: se obtuvo el response esperado en <entity> modificando el <jsonResponsePath> y sus parametros <parameters>
     * expected response is obtained in '<entity>' modifying the '<jsonResponsePath>' and the parameters '<parameters>'
     *
     * @param jsonExpectedPath
     * @param actualList
     * @param parameters
     * @throws Exception
     */
    public <T> void validateFields(String jsonExpectedPath, List<T> actualList, Map<String, String> parameters) throws Exception {
    }

    /**
     * Set your own expected object. Call validateFields(actual, expected); at the end.
     * Step: se obtuvo el response esperado en <entity>
     * expected response is obtained in '<entity>'
     *
     * @param actual
     * @throws Exception
     */
    public void validateFields(Object actual) throws Exception {
    }

    /**
     * Set your own expected list. Call validateFields(actual, expected); at the end.
     * Step: se obtuvo el response esperado en <entity>
     * expected response is obtained in '<entity>'
     *
     * @param actualList
     * @param <T>
     * @throws Exception
     */
    public <T> void validateFields(List<T> actualList) throws Exception {
    }

    /**
     * Set your own expected object with parameters. Call validateFields(actual, expected); at the end.
     * Step: se obtuvo el response esperado en <entity> y sus parametros <parameters>
     * expected response is obtained in '<entity>' and the parameters '<parameters>'
     *
     * @param actual
     * @param parameters
     * @throws Exception
     */
    public void validateFields(Object actual, Map<String, String> parameters) throws Exception {
    }

    /**
     * Set your own expected list with parameters. Call validateFields(actual, expected); at the end.
     * Step: se obtuvo el response esperado en <entity> y sus parametros <parameters>
     * expected response is obtained in '<entity>' and the parameters '<parameters>'
     *
     * @param actualList
     * @param parameters
     * @param <T>
     * @throws Exception
     */
    public <T> void validateFields(List<T> actualList, Map<String, String> parameters) throws Exception {
    }
}

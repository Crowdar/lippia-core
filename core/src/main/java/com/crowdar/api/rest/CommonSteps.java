package com.crowdar.api.rest;

import com.crowdar.core.PropertyManager;
import com.crowdar.core.Utils;
import com.crowdar.util.MapUtils;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.apache.commons.lang.StringUtils;
import org.testng.Assert;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

public class CommonSteps {

    private Class objectClass;
    private Object actualJsonResponse;
    private Object expectedJsonResponse;

    private final String STATUS_CODE_ERROR = "Status code are not equals.";
    private final String GET_ENTITY_SERVICE_METHOD_NAME = "getEntityService";

    @Then("se obtuvo el status code (.*)")
    @And("status code (.*) is obtained")
    public void iWillGetTheProperStatusCodeStatusCode(int expStatusCode) {
        int actualStatusCode = APIManager.getLastResponse().getStatusCode();
        Assert.assertEquals(actualStatusCode, expStatusCode, STATUS_CODE_ERROR);
    }

    @Then("no se obtuvo ningun response")
    @And("not response at all is obtained")
    public void verifyNullResponse() {
        Object actualJsonResponse = getActualResponse();
        Assert.assertEquals(actualJsonResponse, null);
    }

    @Then("se obtuvo el response vacío")
    @And("response is empty")
    public void verifyEmptyResponse() {
        Object actualJsonResponse = getActualResponse().toString();
        Assert.assertEquals(actualJsonResponse, "[]");
    }

    @Then("se obtuvo el response de array vacío")
    @And("response array is empty")
    public void verifyEmptyArrayResponse() {
        Object[] actualJsonResponse = (Object[]) getActualResponse();
        Assert.assertEquals(actualJsonResponse.length, 0);
    }

    @Then("se obtuvo el response esperado en (.*) con el (.*)")
    @And("expected response is obtained in (.*) with (.*)")
    public void iWillGetTheProperResponse(String entity, String jsonName) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setInjectorParameters(jsonName);

        getServiceClass(entity).getMethod("validateFields", objectClass, objectClass).invoke("", expectedJsonResponse, actualJsonResponse);
    }

    @Then("con los parametros: (.*) y el (.*) se obtuvo el response esperado en (.*)")
    @And("with the parameters (.*) and the (.*) expected response is obtained in (.*)")
    public void iWillGetTheProperResponseWithParameters(String inputParameters, String jsonName, String entity) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setInjectorParameters(jsonName);
        Map<String, String> parameters = MapUtils.splitIntoMap(inputParameters, ",", ":");
        getServiceClass(entity).getMethod("validateFields", objectClass, objectClass, Map.class).invoke("", expectedJsonResponse, actualJsonResponse, parameters);
    }

    @Then("modificando el (.*) se obtuvo el response esperado en (.*)")
    @And("modifying the (.*) expected response is obtained in (.*) ")
    public void iWillGetTheProperResponseModified(String expectedJsonName, String entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException {
        setInjectorParameters(null);

        getServiceClass(entity).getMethod("validateFields", String.class, objectClass).invoke("", "response/".concat(expectedJsonName), actualJsonResponse);
    }

    @Then("con los parametros (.*) y modificando el (.*) se obtuvo el response esperado en (.*)")
    @And("with the parameters (.*) and modifying the (.*) expected response is obtained in (.*)")
    public void iWillGetTheProperResponseModifiedWithParameters(String inputParameters, String expectedJsonName, String entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException {
        setInjectorParameters(null);
        Map<String, String> parameters = MapUtils.splitIntoMap(inputParameters, ",", ":");
        getServiceClass(entity).getMethod("validateFields", String.class, objectClass, Map.class).invoke("", "response/".concat(expectedJsonName), actualJsonResponse, parameters);
    }

    @Then("se obtuvo el response esperado en: (.*)")
    @And("expected response is obtained in: (.*)")
    public void iWillGetTheProperResponseWithObject(String entity) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        setInjectorParameters(null);

        getServiceClass(entity).getMethod("validateFields", objectClass).invoke("", actualJsonResponse);
    }

    @Then("con los parametros= (.*) se obtuvo el response esperado en (.*)")
    @And("with the parameters= (.*) is obtained expected response in (.*)")
    public void iWillGetTheProperResponseWithObjectAndParameters(String inputParameters, String entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException {
        setInjectorParameters(null);
        Map<String, String> parameters = MapUtils.splitIntoMap(inputParameters, ",", ":");
        getServiceClass(entity).getMethod("validateFields", objectClass, Map.class).invoke("", actualJsonResponse, parameters);
    }

    private Class getServiceClass(String entity) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        Class entityCLass = Class.forName(PropertyManager.getProperty("crowdar.api.entityConfiguration"));
        Enum entityConfiguration = Enum.valueOf(entityCLass, entity);
        Method method = entityConfiguration.getClass().getMethod(GET_ENTITY_SERVICE_METHOD_NAME);
        method.setAccessible(true);
        return (Class) method.invoke(entityConfiguration);
    }

    private void setInjectorParameters(String expectedJsonName) throws IOException {
        if (getActualResponse().getClass().isArray()) {
            actualJsonResponse = Utils.parseArrayToList((Object[]) getActualResponse());
            objectClass = List.class;
            if (!StringUtils.isEmpty(expectedJsonName)) {
                Class responseClass = getResponseClass((List) actualJsonResponse);
                expectedJsonResponse = getListResponseFromFile(expectedJsonName, responseClass);
            }
        } else {
            actualJsonResponse = getActualResponse();
            objectClass = Object.class;
            if (!StringUtils.isEmpty(expectedJsonName)) {
                expectedJsonResponse = getResponseFromFile(expectedJsonName);
            }
        }
    }

    private Class<?> getResponseClass(List actualJsonResponse) {
        return actualJsonResponse.iterator().next().getClass();
    }

    private List getListResponseFromFile(String jsonName, Class responseClass) throws IOException {
        return APIManager.getListResponseFromJsonFile(jsonName, responseClass);
    }

    private Object getResponseFromFile(String jsonName) throws IOException {
        return APIManager.getResponseFromJsonFile(jsonName, Object.class);
    }

    private Object getActualResponse() {
        return APIManager.getLastResponse().getResponse();
    }
}

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

    @Then("se obtuvo el texto (.*) como response")
    @And("text (.*) was obtained in response")
    public void validateTextResponse(String response) {
        Response actualResponse = APIManager.getLastResponse();
        Assert.assertEquals(actualResponse.getResponse().toString(), response);
    }

    @Then("se obtuvo el response esperado en ([^ ]*) con el ([^ ]*)")
    @And("expected response is obtained in '([^']*)' with '([^']*)'")
    public void iWillGetTheProperResponse(String entity, String jsonName) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        setInjectorParameters(jsonName);
        invokeValidateMethod(entity, objectClass, objectClass, expectedJsonResponse, actualJsonResponse);
    }

    @Then("se obtuvo el response esperado en ([^ ]*) con el ([^ ]*) y sus parametros ([^ ]*)")
    @And("expected response is obtained in '([^']*)' with '([^ ]*)' and the parameters '([^']*)'")
    public void iWillGetTheProperResponseWithParameters(String entity, String jsonName, String inputParameters) throws IOException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InstantiationException {
        setInjectorParameters(jsonName);
        Map<String, String> parameters = MapUtils.splitIntoMap(inputParameters, ",", ":");
        invokeValidateMethod(entity, parameters, objectClass, expectedJsonResponse);
    }

    @Then("se obtuvo el response esperado en ([^ ]*) modificando el ([^ ]*)")
    @And("expected response is obtained in '([^']*)' with '([^']*)' modifying the '([^']*)'")
    public void iWillGetTheProperResponseModified(String entity, String expectedJsonName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException, InstantiationException {
        setInjectorParameters(null);
        invokeValidateMethod(entity, String.class, objectClass, "response/".concat(expectedJsonName), actualJsonResponse);
    }

    @Then("se obtuvo el response esperado en ([^ ]*) modificando el ([^ ]*) y sus parametros ([^ ]*)")
    @And("expected response is obtained in '([^']*)' modifying the '([^ ]*)' and the parameters '([^']*)'")
    public void iWillGetTheProperResponseModifiedWithParameters(String entity, String expectedJsonName, String inputParameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException, InstantiationException {
        setInjectorParameters(null);
        Map<String, String> parameters = MapUtils.splitIntoMap(inputParameters, ",", ":");
        invokeValidateMethod(entity, parameters, String.class, "response/".concat(expectedJsonName));
    }

    @Then("se obtuvo el response esperado en ([^ ]*)")
    @And("expected response is obtained in '([^']*)'")
    public void iWillGetTheProperResponseWithObject(String entity) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        setInjectorParameters(null);
        invokeValidateMethod(entity, "validateFields");
    }

    @Then("se obtuvo el response esperado en ([^ ]*) y sus parametros ([^ ]*)")
    @And("expected response is obtained in: '([^']*)' and the parameters '([^']*)'")
    public void iWillGetTheProperResponseWithObjectAndParameters(String entity, String inputParameters) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException, ClassNotFoundException, InstantiationException {
        setInjectorParameters(null);
        Map<String, String> parameters = MapUtils.splitIntoMap(inputParameters, ",", ":");
        invokeValidateMethod(entity, objectClass, Map.class, actualJsonResponse, parameters);
    }

    @Then("se obtuvo el response esperado en ([^ ]*) con el metodo ([^ ]*)")
    @And("expected response is obtained in '([^']*)' with the method '([^']*)'")
    public void iWillGetTheProperResponseWithObject(String entity, String method) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        this.setInjectorParameters(null);
        invokeValidateMethod(entity, method);
    }

    private void invokeValidateMethod(String entity, String method) throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        try {
            Class service = this.getServiceClass(entity);
            service.getMethod(method, this.objectClass).invoke(service.newInstance(), this.actualJsonResponse);
        } catch (InvocationTargetException e) {
            Assert.fail(e.getCause().toString());
        }
    }

    private void invokeValidateMethod(String entity, Class objectClass, Class objectClass2, Object expectedJsonResponse, Object actualJsonResponse) throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        try {
            Class service = getServiceClass(entity);
            service.getMethod("validateFields", objectClass, objectClass2).invoke(service.newInstance(), expectedJsonResponse, actualJsonResponse);
        } catch (InvocationTargetException e) {
            Assert.fail(e.getCause().toString());
        }
    }

    private void invokeValidateMethod(String entity, Map<String, String> parameters, Class objectClass, Object expectedJsonResponse) throws ClassNotFoundException, IllegalAccessException, NoSuchMethodException, InstantiationException {
        try {
            Class service = getServiceClass(entity);
            service.getMethod("validateFields", objectClass, objectClass, Map.class).invoke(service.newInstance(), expectedJsonResponse, actualJsonResponse, parameters);
        } catch (InvocationTargetException e) {
            Assert.fail(e.getCause().toString());
        }
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

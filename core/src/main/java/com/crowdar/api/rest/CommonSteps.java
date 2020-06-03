package com.crowdar.api.rest;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import org.testng.Assert;

public class CommonSteps {

    private final String STATUS_CODE_ERROR = "Status code are not equals.";

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

    private Object getActualResponse() {
        return APIManager.getLastResponse().getResponse();
    }
}

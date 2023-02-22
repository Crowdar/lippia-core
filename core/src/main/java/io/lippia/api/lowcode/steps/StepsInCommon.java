package io.lippia.api.lowcode.steps;


import com.crowdar.api.rest.APIManager;
import com.crowdar.core.JsonUtils;
import com.crowdar.database.DatabaseManager;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.lippia.api.configuration.enums.ParameterTypeEnum;
import io.lippia.api.extractor.ddbb.DatabaseStringValueExtractor;
import io.lippia.api.extractor.json.JsonStringValueExtractor;
import io.lippia.api.extractor.xml.XmlStringValueExtractor;
import io.lippia.api.lowcode.Engine;
import io.lippia.api.lowcode.messages.Messages;
import io.lippia.api.lowcode.recognition.RecognitionObjectType;
import io.lippia.api.lowcode.recognition.parser.Deserialization;
import io.lippia.api.lowcode.recognition.parser.Types;
import io.lippia.api.lowcode.variables.VariablesManager;
import io.lippia.api.service.CommonService;
import org.testng.Assert;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV4;
import static io.lippia.api.lowcode.Engine.gson;
import static io.lippia.api.lowcode.configuration.ConfigurationType.*;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;


public class StepsInCommon {


    Engine engine = new Engine();

    @Given("^define ([^\\d]\\S+) = (\\S+)$")
    public void setVariable(String key, String value) throws UnsupportedEncodingException {
        this.engine.set(key, value);
    }

    @Given("^base url (\\S+)$")
    public void setBaseUrl(String baseUrl) {
        this.engine.configure(BASE_URL, baseUrl);
    }

    @When("^execute method (GET|POST|PUT|PATCH|DELETE)$")
    public void setHttpMethodAndExecute(String httpMethod) {
        this.engine.configure(HTTP_METHOD, httpMethod);
        this.engine.call();
    }

    @When("^call (\\S+.feature)([@:\\$])(\\S+)$")
    public void call(String feature, String filterType, String filterValue) throws Throwable {
        this.engine.call(feature, filterType, filterValue);
    }

    @Given("^header (\\S+) = (\\S+)$")
    public void setHeader(String key, String value) {
        this.engine.configure(HEADERS, key, value);
    }

    @Given("^headers (\\S+)$")
    public void setHeaders(String headers) {
        this.engine.configure(HEADERS, RecognitionObjectType.find(headers).corresponding(Types.HEADERS));
    }

    @Given("^body (\\S+)$")
    public void setBody(String body) {
        this.engine.configure(BODY, RecognitionObjectType.find(body).corresponding(Types.BODIES).toString());
    }

    @Given("^param (\\S+) = (\\S+)$")
    public void setParam(String key, String value) {
        this.engine.configure(URL_PARAMETER, key, value);
    }

    @Given("^endpoint (\\S+)$")
    public void setEndpoint(String endpoint) {
        this.engine.configure(ENDPOINT, endpoint);
    }

    @Then("^the status code should be (\\d+)$")
    public void status(int expectedStatusCode) {
        this.engine.validates(expectedStatusCode, APIManager.getLastResponse().getStatusCode(), Integer::equals, Messages.STATUS_CODE_ERROR);
    }

    @Then("^response should be ([^\\s].+) = ([^\\s].*)$")
    public void response(String path, String expectedValue) throws UnsupportedEncodingException {
        this.engine.responseMatcher(path, expectedValue);
    }

    @Then("^validate schema (.+)$") // it supports only json
    public void schema(String jsonName) throws IOException {
        Object respuesta = APIManager.getLastResponse().getResponse();
        if (respuesta instanceof List || respuesta instanceof Map) {
            respuesta = gson.toJson(respuesta);
        }
        String jsonSchemaPath = Deserialization.getPathFromJsonPath(Types.SCHEMAS).concat(jsonName);
        JsonSchemaFactory jsonSchemaFactory = JsonSchemaFactory.newBuilder().setValidationConfiguration(
                ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze()).freeze();
        Assert.assertTrue(
                matchesJsonSchema(JsonUtils.getJSONFromPath(jsonSchemaPath))
                        .using(jsonSchemaFactory)
                        .matches(respuesta));
    }

    @And("^response should be ([^\\s].+) contains ([^\\s].*)$")
    public void responseShouldBeSContains(String path, String expectedValue) {
        this.engine.responseContainer(path, expectedValue);
    }

    @And("^delete keyValue (.*) in body (.*)$")
    public void deleteKeyvalueAtributoInBodyPath(String attribute, String body) throws IOException {
        CommonService.deleteAttributeInBody(attribute, body);
    }

    @And("^set value (.*) of key (.*) in body (.*)$")
    public void setValueValorOfKeyClaveInBodyPath(String value, String key, String body) throws Exception {
        CommonService.setValue(value, key, body);
    }

    @When("^set values (.*) of keys (.*) in body (.*)$")
    public void setValuesValoresOfKeysInBody(String values, String keys, String body) throws Exception {
        CommonService.setValuesKeys(values, keys, body);
    }

    @When("^i save from result (.*) the attribute (.*) on variable (.*)$")
    public void iSaveVariable(String parameterType, String attribute, String name) throws ParserConfigurationException, IOException, org.xml.sax.SAXException {
        String value = "";
        String responseString = "";

        switch (ParameterTypeEnum.valueOf(parameterType.toUpperCase())) {
            case KEY_VALUE:
                value = attribute;
                break;
            case JSON:
                GsonBuilder gsonBuilder = new GsonBuilder();
                gsonBuilder.serializeNulls();
                Gson gson = gsonBuilder.create();
                Object obtenido = APIManager.getLastResponse().getResponse();
                if (obtenido instanceof List || obtenido instanceof Map) {
                    obtenido = gson.toJson(obtenido);
                }
                responseString = obtenido.toString();
                value = JsonStringValueExtractor.handle(responseString, attribute);
                break;
            case XML:
                responseString = (String) APIManager.getLastResponse().getResponse();
                value = XmlStringValueExtractor.handle(responseString, attribute);
                break;
            case LAST_QUERY:
                List<Map<String, Object>> lastResult = DatabaseManager.getLastResponse();
                value = DatabaseStringValueExtractor.handle(lastResult, attribute);
                break;
            default:
                break;
        }
        VariablesManager.setVariable(name, value);
    }


    @And("i get variable (.*) from result (.*) of attribute (.*)")
    public void iGetVariableFromResultOfAttribute(String name, String parameterType, String attribute) {
        VariablesManager.getVariable(attribute);
    }

    @Then("^validate response should be ([^\\s].+) = ([^\\s].*)$")
    public void valdidateResponse(String path, String expectedValue) throws UnsupportedEncodingException {
        this.engine.responseMatcherUTF(path, expectedValue);
    }

}
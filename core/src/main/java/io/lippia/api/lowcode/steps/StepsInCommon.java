package io.lippia.api.lowcode.steps;


import com.crowdar.api.rest.APIManager;
import com.crowdar.database.DatabaseManager;
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
import io.lippia.api.lowcode.assertions.SchemaValidator;
import io.lippia.api.lowcode.messages.Messages;
import io.lippia.api.lowcode.variables.VariablesManager;
import io.lippia.api.service.CommonService;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import static io.lippia.api.lowcode.configuration.ConfigurationType.*;

public class StepsInCommon {
    Engine engine = new Engine();
    CommonService commonService = new CommonService();

    @Given("^define ([^\\d]\\S+) = ([^\\s].*)$")
    public void setVariable(String key, String value) throws UnsupportedEncodingException {
        this.engine.set(key, value);
    }

    @Given("^base url (\\S+)$")
    public void setBaseUrl(String baseUrl) {
        this.engine.configure(BASE_URL, baseUrl);
    }

    @When("^execute method (GET|POST|PUT|PATCH|DELETE)$")
    @And("^ejecutar metodo (GET|POST|PUT|PATCH|DELETE)$")
    public void setHttpMethodAndExecute(String httpMethod) {
        this.engine.configure(HTTP_METHOD, httpMethod);
        this.engine.call();
    }

    @When("^call (\\S+.feature)([@:\\$])(\\S+)$")
    @And("^invocar (\\S+.feature)([@:\\$])(\\S+)$")
    public void call(String feature, String filterType, String filterValue) throws Throwable {
        this.engine.call(feature, filterType, filterValue);
    }

    @Given("^header (\\S+) = ([^\\s].*)$")
    public void setHeader(String key, String value) {
        this.engine.configure(HEADERS, key, value);
    }

    @Given("^headers ([^\\s].*)$")
    public void setHeaders(String headers) {
        this.engine.configure(HEADERS, headers);
    }

    @Given("^body (\\S+)$")
    public void setBody(String body) {
        this.engine.configure(BODY, body);
    }

    @Given("^setear el valor (.*) de las clave (.*) en el body (.*)$")
    @And("^set value (.*) of key (\\S+) in body (\\S+)$")
    public void set(String value, String key, String in) {
        this.engine.set(value, key, in);
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
    @And("^el status code debe ser (\\d+)$")
    public void status(int expectedStatusCode) {
        this.engine.validates(expectedStatusCode, APIManager.getLastResponse().getStatusCode(), Integer::equals, Messages.STATUS_CODE_ERROR);
    }

    @Then("^response should be ([^\\s].+) = ([^\\s].*)$")
    @And("^la respuesta debe ser ([^\\s].+) = ([^\\s].*)$")
    public void response(String path, String expectedValue) throws UnsupportedEncodingException {
        this.engine.responseMatcher(path, expectedValue);
    }

    @Then("^verify the response ([^\\s].+) '(equals|contains)' ([^\\s].*)$")
    @And("^verificar la respuesta ([^\\s].+) '(equals|contains)' ([^\\s].*)$")
    public void response(String path, String condition, String expectedValue) {
        this.engine.responseMatcher(path, condition, expectedValue);
    }

    @Then("^validate schema (.+)$")
    @And("^validar schema (.+)$")
    public void schema(String var0) {
        Object response = APIManager.getLastResponse().getResponse();
        response = this.engine.instanceListOrMapOf(response);

        Object schema = Engine.evaluateExpression(var0);
        schema = this.engine.instanceListOrMapOf(schema);

        SchemaValidator.validate(response.toString(), schema.toString());
    }

    @Then("^response should be ([^\\s].+) contains ([^\\s].*)$")
    @And("^la respuesta debe ser ([^\\s].+) contiene ([^\\s].*)$")
    public void responseShouldBeSContains(String path, String expectedValue) {
        this.engine.responseContainer(path, expectedValue);
    }

    @When("^delete keyValue (.*) in body (.*)$")
    @And("^eliminar clave (.*) en el body (.*)$")
    public void deleteKeyvalueAtributoInBodyPath(String attribute, String body) throws IOException {
        CommonService.deleteAttributeInBody(attribute, body);
    }

    @When("^set values (.*) of keys (.*) in body (.*)$")
    @And("^setear los valores (.*) de las claves (.*) en el body (.*)$")
    public void setValuesValoresOfKeysInBody(String values, String keys, String body) throws Exception {
        CommonService.setValuesKeys(values, keys, body);
    }

    @When("^I save from result (.*) the attribute (.*) on variable (.*)$")
    @And("^guardo del resultado (.*) el atributo (.*) en la variable (.*)$")
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


    @When("I get variable (.*) from result (.*) of attribute (.*)")
    @And("obtengo la variable (.*) del resultado (.*) del atributo (.*)")
    public void iGetVariableFromResultOfAttribute(String name, String parameterType, String attribute) {
        VariablesManager.getVariable(attribute);
    }

    @Then("^validate response should be ([^\\s].+) = ([^\\s].*)$")
    public void valdidateResponse(String path, String expectedValue) throws UnsupportedEncodingException {
        this.engine.responseMatcherISO(path, expectedValue);
    }

    @And("^print response$")
    public void printResponse() {
        commonService.printerLog();
    }

    @Given("^imprimir '(\\S+)'$")
    @And("^print '(\\S+)'$")
    public void print(String param) {
        CommonService.print(param);
    }

}
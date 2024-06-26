package io.lippia.api.lowcode;

import com.crowdar.api.rest.APIManager;

import com.crowdar.bdd.cukes.TestNGSecuencialRunner;

import com.crowdar.core.JsonUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import gherkin.events.PickleEvent;

import io.cucumber.testng.TestNGCucumberRunner;

import io.lippia.api.configuration.EndpointConfiguration;
import io.lippia.api.lowcode.assertions.JsonPathAnalyzer;
import io.lippia.api.lowcode.configuration.ConfigurationType;
import io.lippia.api.lowcode.exception.LippiaException;
import io.lippia.api.lowcode.internal.PicklesBuilder;
import io.lippia.api.service.CallerService;
import io.lippia.api.service.CommonService;
import io.lippia.api.utils.XmlUtils;

import org.testng.Assert;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.function.BiPredicate;
import java.util.regex.Matcher;

import static com.crowdar.util.JsonUtils.isJSONValid;

import static io.lippia.api.lowcode.variables.VariablesManager.setVariable;
import static io.lippia.api.service.MethodServiceEnum.NOSSLVERIFICATION;


public class Engine {
    public static Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();


    static {
        EventDispatcher.dispatch(ParametersDefinitionTypeBuilder::new);
        EventDispatcher.dispatch(PrimitiveDefinitionTypeBuilder::new);
        EventDispatcher.dispatch(FileEventDefinitionTypeBuilder::new);
    }

    public static Object evaluateExpression(Object... entries) {
        return EventDispatcher.trigger(entries);
    }

    public void set(String value, String key, String in) {
        JsonKeysProcessor.set(value, key, in);
    }

    public void delete(String key, String in) {
        JsonKeysProcessor.delete(key, in);
    }

    public void set(String key, String value) throws UnsupportedEncodingException {
        if (value.matches("^response\\.?\\S+$") || value.matches("^\\$\\.?\\S+$")) {
            value = responseMatcherGeneric(value.replaceFirst("response", Matcher.quoteReplacement("$")), StandardCharsets.UTF_8).toString();
        }

        setVariable(key, evaluateExpression(value));
    }

    public void configure(ConfigurationType config, String key, String value) {
        config.assign(key, value);
    }

    public void configure(ConfigurationType config, String source) {
        config.assign(source);
    }

    public void configure(ConfigurationType config, JsonObject json) {
        config.assign(json);
    }

    public void call() {
        EndpointConfiguration.getInstance().setMethodService(NOSSLVERIFICATION);
        try {
            System.out.println(new Gson().toJson(CallerService.getRequest(EndpointConfiguration.getInstance())));
            CallerService.call(EndpointConfiguration.getInstance());
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | IOException e) {
            throw new LippiaException("Call Step has not been executed, track: \n%s", e.getMessage());
        }

        CommonService.BODY.remove();
        EndpointConfiguration.clean();
    }


    public void call(String featureName, String separator, String filter) throws Throwable {
        List<PickleEvent> pickles = new PicklesBuilder.Builder()
                .from(featureName)
                .separatorType(separator)
                .filterBy(filter)
                .build();

        EndpointConfiguration lastConfig = EndpointConfiguration.getInstance();
        EndpointConfiguration.clean();
        TestNGCucumberRunner tg = new TestNGCucumberRunner(TestNGSecuencialRunner.class);

        for (PickleEvent pickle : pickles) {
            tg.runScenario(pickle);
        }
        EndpointConfiguration.setInstance(lastConfig);
    }


    public <T> void validates(T v1, T v2, BiPredicate<T, T> typeMatcher, final String errorMessage) {
        if (typeMatcher.test(v1, v2)) return;

        Assert.fail(String.format(errorMessage, v1, v2));
    }

    public void responseMatcher(String path, String expectedValue) throws UnsupportedEncodingException {
        Object pathValue = responseMatcherGeneric(path, StandardCharsets.UTF_8);
        Assert.assertEquals(pathValue, evaluateExpression(expectedValue), "no match!");
    }

    public void responseMatcher(String path,String condition, String expectedValue) {
        String pathValue = this.responseMatcherGeneric(path, StandardCharsets.UTF_8).toString();
        expectedValue = expectedValue.replace("\"", "");

        if (condition.equals("equals")) {
            Assert.assertEquals(pathValue, evaluateExpression(expectedValue).toString(), "The value " + pathValue + " doesn't match with " + evaluateExpression(expectedValue).toString());
        } else if (condition.equals("contains")) {
            Assert.assertTrue(pathValue.contains(evaluateExpression(expectedValue).toString()), "The value " + pathValue + " doesn't match with " + evaluateExpression(expectedValue).toString());
        } else {
            Assert.fail("unsupported condition: " + condition);
        }
    }

    public void responseMatcherISO(String path, String expectedValue) throws UnsupportedEncodingException {
        String pathValue = responseMatcherGeneric(path, StandardCharsets.ISO_8859_1).toString();
        Assert.assertEquals(pathValue, evaluateExpression(expectedValue), "no match!");
    }

    public Object responseMatcherGeneric(String path, Charset Standard) {
        Object entry = APIManager.getLastResponse().getResponse();

        if (JsonUtils.isJSONValid(entry)) {
            if (entry instanceof List || entry instanceof Map) {
                entry = new Gson().toJson(entry);
            }
        } else if (XmlUtils.isXMLValid(entry.toString())) {
            entry = XmlUtils.asJson(entry.toString());
        }

        return JsonPathAnalyzer.read(new String(entry.toString().getBytes(Standard)), path);
    }

    public void responseContainer(String path, String expectedValue) {
        String response = (isJSONValid(APIManager.getLastResponse().getResponse().toString())) ?
                APIManager.getLastResponse().getResponse().toString() :
                new Gson().toJson(APIManager.getLastResponse().getResponse());

        Object pathValue = JsonPathAnalyzer.read(response, path);
        Assert.assertTrue(pathValue.toString().contains(evaluateExpression(expectedValue).toString()), "no se encontraron coincidencias!");
    }

    public Object instanceListOrMapOf(Object jsonVar) {
        if (jsonVar instanceof List || jsonVar instanceof Map) {
            jsonVar = Engine.gson.toJson(jsonVar);
        }
        return jsonVar;
    }

}
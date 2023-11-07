package io.lippia.api.service;


import com.crowdar.api.rest.APIManager;
import com.crowdar.api.rest.Response;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.lippia.api.configuration.EndpointConfiguration;
import io.lippia.api.lowcode.Engine;
import io.lippia.api.lowcode.steps.StepsInCommon;
import io.lippia.api.lowcode.variables.VariablesManager;
import io.lippia.api.utils.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;

import static com.crowdar.api.rest.APIManager.getLastResponse;
import static com.crowdar.core.JsonUtils.getJSONFromPath;
import static io.lippia.api.lowcode.Engine.evaluateExpression;
import static io.lippia.api.lowcode.Engine.gson;


public class CommonService {
    static Engine engine = new Engine();
    static StepsInCommon stepsInCommon = new StepsInCommon();

    public static ThreadLocal<String> BODY = new ThreadLocal<>();


    public static void deleteAttributeInBody(String attribute, String jsonName) throws IOException {
        try {
            String jsonFromFile = (BODY.get() == null) ? getJSONFromFileBody(jsonName) : BODY.get();
            BODY.set(jsonFromFile);
            Json json = Json.of(jsonFromFile);
            json.remove(attribute);
            BODY.set(String.valueOf(json));
            stepsInCommon.setBody(BODY.get());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static String getJSONFromFileBody(String fileName) throws IOException {
        return getJSONFromFileGeneric(fileName, "resources");
    }

    public static String getJSONFromFileResponse(String fileName) throws IOException {
        return getJSONFromFileGeneric(fileName, "resources");
    }

    public static String getJSONFromFileGeneric(String fileName, String from) throws IOException {
        String file = String.format(fileName).replace("/", File.separator);
        String path = System.getProperty("user.dir").concat(File.separator).concat("src").concat(File.separator).concat("test").concat(File.separator).concat(from).concat(File.separator).concat(file);
        return getJSONFromPath(path);
    }

    public static void setValue(String newValue, String attribute, String jsonName)  {
        try {
            String jsonFromFile = (BODY.get() == null) ? getJSONFromFileBody(jsonName) : BODY.get();
            BODY.set(jsonFromFile);
            Json json = Json.of(jsonFromFile);
            json.set(attribute, evaluateExpression(newValue));
            BODY.set(String.valueOf(json));
            stepsInCommon.setBody(BODY.get());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void setValuesKeys(String newValue, String attribute, String jsonName) throws Exception {
        ArrayList<String> claves = setLista(attribute);
        ArrayList<Object> valores = setListaValores(newValue);
        ThreadLocal<String> jsont = new ThreadLocal<>();
        if (claves.size() == valores.size() && claves.size() > 0) {
            jsont.set(getJSONFromFileBody(jsonName));
            for (int i = 0; i < claves.size(); i++) {
                String json = jsont.get();
                if (json.contains(claves.get(i))) {
                    JSONObject jsonObj = new JSONObject(json);
                    updateValue(jsonObj, claves.get(i), valores.get(i).getClass().toString());
                    jsont.set(jsonObj.toString());
                }
            }
            BODY.set(jsont.get());
            System.out.println("El body que se genero fue :" + BODY.get());
            stepsInCommon.setBody(BODY.get());
        }
    }

    public static void updateValue(JSONObject obj, String keyMain, Object newValue) {
        Iterator<String> iterator = obj.keys();
        String key = null;
        while (iterator.hasNext()) {
            key = iterator.next();
            if ((obj.optJSONArray(key) == null) && (obj.optJSONObject(key) == null)) {
                if ((key.equals(keyMain))) {
                    obj.put(key, newValue);
                }
            }
            if (obj.optJSONObject(key) != null) {
                updateValue(obj.getJSONObject(key), keyMain, newValue);
            }
            if (obj.optJSONArray(key) != null) {
                JSONArray jArray = obj.getJSONArray(key);
                for (int i = 0; i < jArray.length(); i++) {
                    updateValue(jArray.getJSONObject(i), keyMain, newValue);
                }
            }
        }

    }


    public static ArrayList<String> setLista(String atributos) {
        ArrayList<String> list = null;
        try {
            String[] atributoSplit = atributos.split(",");
            list = new ArrayList<String>(Arrays.asList(atributoSplit));
        } catch (Exception e) {
            System.out.println("No se pudo generar la lista de :" + e.getMessage());
        }
        return list;
    }

    public static ArrayList<Object> setListaValores(String valores) {
        ArrayList<Object> list = null;
        try {
            Object[] atributoSplit = valores.split(",");
            list = new ArrayList<Object>(Arrays.asList(atributoSplit));
        } catch (Exception e) {
            System.out.println("No se pudo generar la lista de :" + e.getMessage());
        }
        return list;
    }


    public static void compareResponseWith(String path) throws IOException {
        String rs_esperado = getJSONFromFileResponse(path);
        Object obtenido = APIManager.getLastResponse().getResponse();
        if (obtenido instanceof List || obtenido instanceof Map) {
            obtenido = new Gson().toJson(obtenido);
        }

        Assert.assertEquals(obtenido, rs_esperado, "Los respuestas no son iguales");

    }


    public static Object getValueOf(Object valor) {
        Object result = null;
        if (valor.toString().startsWith("$.")) {
            result = engine.responseMatcherGeneric(valor.toString(), StandardCharsets.UTF_8);
        } else {
            result = evaluateExpression(valor);
        }

        return result;
    }

    public void printerLog() {
        EndpointConfiguration.getInstance().setMethodService(MethodServiceEnum.NOSSLVERIFICATION);
        Logger logger = LoggerFactory.getLogger(this.getClass());
        Response response = getLastResponse();
        Object status = response.getStatusCode();
        Object body = response.getResponse();
        logger.info("\n┌────────────────────────────────────────────────────────────────────────────────────┐\n" +
                "|────────────────────────────────── BEGIN RESPONSE ──────────────────────────────────|\n" +
                " status " + status + "\n" + getPrettyJson(body) + "\n" +
                "|─────────────────────────────────── END RESPONSE  ──────────────────────────────────|\n" +
                "└────────────────────────────────────────────────────────────────────────────────────┘"
        );
    }


    public static String getPrettyJson(Object jsonString) {
        if (jsonString == null) return null;
        if (jsonString instanceof Map) {
            jsonString = gson.toJson(jsonString, Map.class);
        }
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(new String(jsonString.toString().getBytes(StandardCharsets.UTF_8)));
        return gson.toJson(je);
    }

    /**
     * This method prints any input through the console, it can be a defined variable, a response or a request
     * @param param is the name of the variable, the attribute of a response, or a request
     */
    public static void print(String param) {
        String param1 = param.contains("'") ? param.replace("'", "") : param;
        try {
            if (param1.equals("response")) {
                CommonService commonService = new CommonService();
                commonService.printerLog();
            }
            if (param1.matches("^response\\.?\\S+$") || param1.matches("^\\$\\.?\\S+$")) {
                String value = engine.responseMatcherGeneric(param1.replaceFirst("response", Matcher.quoteReplacement("$")), StandardCharsets.UTF_8).toString();
                System.out.println("Attribute " + param1 + ": " + value);
            }
            if (param1.contains("{{")) {
                System.out.println("Variable " + param1 + ": " + VariablesManager.getVariable(param1.replaceAll("[\\{\\{|\\}\\}]", "")));
            }
        } catch (Exception e) {
            System.out.println("Could not find variable or path" + e.getMessage());
        }
    }

}

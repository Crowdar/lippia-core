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
import io.lippia.api.utils.Json;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.Assert;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.crowdar.api.rest.APIManager.getLastResponse;
import static com.crowdar.core.JsonUtils.getJSONFromPath;
import static io.lippia.api.lowcode.Engine.evaluateExpression;
import static io.lippia.api.lowcode.Engine.gson;


public class CommonService {
    static Engine engine = new Engine();
    static StepsInCommon stepsInCommon = new StepsInCommon();

    public static ThreadLocal<String> BODY = new ThreadLocal<>();
    public static ThreadLocal<String> VALUE = new ThreadLocal<>();

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

    public static void setValue(String newValue, String attribute, String jsonName) throws Exception {
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


    public static Object getValueOf(Object valor) throws UnsupportedEncodingException {
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
}

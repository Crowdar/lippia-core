package io.lippia.api.lowcode;

import com.crowdar.core.JsonUtils;
import com.google.gson.Gson;

import io.lippia.api.configuration.EndpointConfiguration;
import io.lippia.api.lowcode.assertions.JsonPathAnalyzer;
import io.lippia.api.service.CommonService;
import io.lippia.api.utils.XmlUtils;

import java.util.List;
import java.util.Map;

import static io.lippia.api.lowcode.variables.VariablesManager.setVariable;

public class JsonKeysProcessor {
    private JsonKeysProcessor() {
    }

    public static void delete(String key, String in) {
        processOperation(key, in, null);
    }

    public static void set(String value, String key, String in) {
        processOperation(key, in, value.equals("null") ? null : Engine.evaluateExpression(value));
    }

    private static void processOperation(String key, String in, Object value) {
        boolean isXml = false;

        if (CommonService.BODY.get() == null) {
            Object content = Engine.evaluateExpression(in);

            if (JsonUtils.isJSONValid(content.toString())) {
                if (content instanceof List || content instanceof Map) {
                    content = new Gson().toJson(content);
                }
            } else if (XmlUtils.isXMLValid(content.toString())) {
                content = XmlUtils.asJson(content.toString());
                isXml = true;
            }

            CommonService.BODY.set(content.toString());
        }

        String[] splJsonPath = key.split("\\.");
        String completeJsonPath = "$";

        if (splJsonPath.length > 1) {
            key = splJsonPath[splJsonPath.length - 1];

            for (int i = 0; i <= splJsonPath.length - 2; i++) {
                completeJsonPath = completeJsonPath.concat(".").concat(splJsonPath[i]);
            }
        }

        String newJson;
        if (XmlUtils.isXMLValid(CommonService.BODY.get())) {
            isXml = true;
            CommonService.BODY.set(XmlUtils.asJson(CommonService.BODY.get()));
        }

        if (value == null && key != null) {
            newJson = JsonPathAnalyzer.delete(CommonService.BODY.get(), completeJsonPath.concat(".").concat(key));
        } else {
            newJson = JsonPathAnalyzer.set(CommonService.BODY.get(), completeJsonPath, key, value);
        }

        CommonService.BODY.set(isXml ? JsonUtils.asXml(newJson) : newJson);
        if (in.startsWith("$(") && in.endsWith(")")) {
            in = in.substring(6, in.length() - 1);
            setVariable(in, CommonService.BODY.get());
        }

        EndpointConfiguration.body(CommonService.BODY.get());
    }
}

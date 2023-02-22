package io.lippia.api.lowcode.variables;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.lippia.api.lowcode.exception.LippiaException;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;
import java.util.Map;

public class VariablesManager {

    private static final Logger logger = LoggerFactory.getLogger(VariablesManager.class);

    private static final ThreadLocal<VariablesManager> INSTANCE = new ThreadLocal<>();

    private VariablesManager() {}

    public static void clean() {
        INSTANCE.remove();
    }

    public static VariablesManager getInstance() {
        if (INSTANCE.get() == null) {
            INSTANCE.set(new VariablesManager());
        }

        return INSTANCE.get();
    }

    public static void setVariable(String key, String value) {
        if (variables.containsKey(key)) {
            if (StringUtils.equals(variables.get(key), value)) return;
            logger.info("Variable {{" + key + "}} has already been defined\n" +
                            "Overwrite in process from [" + variables.get(key) + "] to [" + value + "]"
            );
        }

        variables.put(key, value);
    }

    public static String getVariable(String key) {
        if (!variables.containsKey(key)) {
            throw new LippiaException("Variable {{" + key + "}} has not been defined");
        }

        return variables.get(key);
    }

    private static final Map<String, String> variables = new LinkedHashMap<>();

}
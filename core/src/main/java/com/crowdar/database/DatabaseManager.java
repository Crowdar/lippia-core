package com.crowdar.database;

import com.crowdar.core.PropertyManager;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.TagType;
import com.github.jknack.handlebars.Template;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DatabaseManager {

    private static final ThreadLocal<List<Map<String, Object>>> LAST_RESULT = new ThreadLocal<>();

    public static void setLastResponse(List<Map<String, Object>> lastResult) {
    	LAST_RESULT.set(lastResult);
    }

    public static List<Map<String, Object>> getLastResponse() {
        return LAST_RESULT.get();
    }
   
    public static String replaceVars(String query, String key, String value) throws IOException {
    	return query.replace("{{" + key + "}}", value);
    }
    
    /**
     * @param query sql as String with Handlebars {{  }} to be replaced key Value
     * @return String with replaced handlebars vars
     * @throws IOException
     */
    public static String replaceSemicolonSeparatedVars(String query, String varsString) throws IOException {
    	String[] vars = varsString.split(";");
    	
    	for(String varString : vars) {
    		String[] var = varString.split("=");
            query = replaceVars(query, var[0], var[1]);
    	}
    	
        return query;
    }
}

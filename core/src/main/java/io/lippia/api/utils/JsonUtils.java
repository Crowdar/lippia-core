package io.lippia.api.utils;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.PathNotFoundException;

import net.minidev.json.JSONStyle;
import net.minidev.json.JSONValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class JsonUtils {


    private JsonUtils() {
        // only static methods
    }


    public static String toJson(Object o) {
        return toJson(o, false);
    }

    public static String toJson(Object o, boolean pretty) {
        if (!pretty) { // TODO use JSONStyleIdent in json-smart 2.4
            try {
                return JSONValue.toJSONString(o);
            } catch (Throwable t) {
            }
        }
        return JsonUtils.toJsonSafe(o, pretty);
    }
    
    public static String toJsonSafe(Object o, boolean pretty) {
        StringBuilder sb = new StringBuilder();
        // anti recursion / back-references
        Set<Object> seen = Collections.newSetFromMap(new IdentityHashMap());
        recurseJsonString(o, pretty, sb, 0, seen);
        if (pretty) {
            sb.append('\n');
        }
        return sb.toString();
    }
    
    private static void recurseJsonString(Object o, boolean pretty, StringBuilder sb, int depth, Set<Object> seen) {
        if (o == null) {
            sb.append("null");
        } else if (o instanceof List) {
            List list = (List) o;            
            if (list.isEmpty() || seen.add(o)) {                
                sb.append('[');
                if (pretty) {
                    sb.append('\n');
                }
                Iterator iterator = list.iterator();
                while (iterator.hasNext()) {
                    Object child = iterator.next();
                    if (pretty) {
                        pad(sb, depth + 1);
                    }
                    recurseJsonString(child, pretty, sb, depth + 1, seen);
                    if (iterator.hasNext()) {
                        sb.append(',');
                    }
                    if (pretty) {
                        sb.append('\n');
                    }
                }
                if (pretty) {
                    pad(sb, depth);
                }
                sb.append(']');
            } else {
                ref(sb, o);
            }
        } else if (o instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) o;
            if (map.isEmpty() || seen.add(o)) {
                sb.append('{');
                if (pretty) {
                    sb.append('\n');
                }                
                Iterator<Map.Entry<String, Object>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, Object> entry = iterator.next();
                    Object key = entry.getKey(); // found a rare case where this was a boolean
                    if (pretty) {
                        pad(sb, depth + 1);
                    }
                    sb.append('"').append(escapeValue(key == null ? null : key.toString())).append('"').append(':');
                    if (pretty) {
                        sb.append(' ');
                    }
                    recurseJsonString(entry.getValue(), pretty, sb, depth + 1, seen);
                    if (iterator.hasNext()) {
                        sb.append(',');
                    }
                    if (pretty) {
                        sb.append('\n');
                    }
                }
                if (pretty) {
                    pad(sb, depth);
                }
                sb.append('}');
            } else {
                ref(sb, o);
            }
        } else if (o instanceof String) {
            String value = (String) o;
            sb.append('"').append(escapeValue(value)).append('"');
        } else if (o instanceof Number || o instanceof Boolean) {
            sb.append(o);
        } else { // TODO custom writers ?
            String value = o.toString();
            sb.append('"').append(escapeValue(value)).append('"');
        }
    }
    
    private static void pad(StringBuilder sb, int depth) {
        for (int i = 0; i < depth; i++) {
            sb.append(' ').append(' ');
        }
    }

    private static void ref(StringBuilder sb, Object o) {
        sb.append("\"#ref:").append(o.getClass().getName()).append('"');
    }

    public static String escapeValue(String raw) {
        return JSONValue.escape(raw, JSONStyle.LT_COMPRESS);
    }

    public static boolean isJson(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        if (s.charAt(0) == ' ') {
            s = s.trim();
            if (s.isEmpty()) {
                return false;
            }
        }
        return s.charAt(0) == '{' || s.charAt(0) == '[';
    }
}
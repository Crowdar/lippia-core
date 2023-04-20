package io.lippia.api.lowcode.recognition;


import com.google.gson.JsonObject;
import io.lippia.api.lowcode.exception.LippiaException;
import io.lippia.api.lowcode.recognition.parser.Deserialization;
import io.lippia.api.lowcode.recognition.parser.Types;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/** su único propósito es reconocer los tipos de archivos, y devolver el source con el formato ya validado **/
public enum RecognitionObjectType {

    JSON_FILE(Deserialization::getJsonSource,     "\\S+.json"),
    XML_FILE(Deserialization::getXmlSource,       "\\S+.xml"),
    JSON_STRING(Deserialization::getJsonObject,       "\\{.*\\}");

    RecognitionObjectType(Function<String, JsonObject> function, String regex) {
        this(function, regexMatcher(regex));
    }
    RecognitionObjectType(BiFunction<Types, String, JsonObject> function, String regex) {
        this(function, regexMatcher(regex));
    }

    private Function<String, JsonObject>          pFunction  = null;
    private BiFunction<Types, String, JsonObject> sFunction  = null;
    private Predicate<String> typeMatcher                             = null;

    // representa al objeto, o al archivo
    private static String objectType = null;

    RecognitionObjectType(Function<String, JsonObject> function, Predicate<String> typeMatcher) {
        this.pFunction = function;
        this.typeMatcher = typeMatcher;
    }

    RecognitionObjectType(BiFunction<Types, String, JsonObject> function, Predicate<String> typeMatcher) {
        this.sFunction = function;
        this.typeMatcher = typeMatcher;
    }

    private static Predicate<String> regexMatcher(String regex) {
        Pattern p = Pattern.compile(regex);
        return arg0 -> p.matcher(arg0).matches();
    }

    public static RecognitionObjectType find(String type) {
        for (RecognitionObjectType recognitionType : RecognitionObjectType.values()) {
            if (recognitionType.typeMatcher.test(type)) {
                objectType = type; return recognitionType;
            }
        }

        throw new LippiaException("Recognition type has failed, type {{" + type + "}} not found");
    }

    public JsonObject corresponding(Types type) {
        if (pFunction == null) {
            return sFunction.apply(type, objectType);
        }

        return pFunction.apply(objectType);
    }

}
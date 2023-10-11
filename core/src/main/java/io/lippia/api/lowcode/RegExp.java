package io.lippia.api.lowcode;

import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

class RegExp {
    private RegExp() {
    }

    static boolean matches(String regexp, String sequence) {
        return compile(regexp).matcher(sequence).matches();
    }

    static Predicate<String> matches(String regexp) {
        return (sequence) -> Pattern.compile(regexp).matcher(sequence).matches();
    }
}
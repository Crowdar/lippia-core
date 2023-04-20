package io.lippia.api.lowcode.recognition.validators;

import java.util.regex.Pattern;

public class Patterns {

    /* combination of positive lookbehind & lookahead, finds the 1st word ("var" with "{{" before it and "}}" after it) */
    public static final Pattern VARIABLE_PATTERN = Pattern.compile("(?<=\\{{2})\\w+(?=}{2})");

    /* combination of positive lookbehind & lookahead, finds the 1st word ("prop" with "${" before it and "}" after it) */

    public static final Pattern PROPERTY_PATTERN = Pattern.compile("(?<=\\$\\{)\\w+(?=})");

    public static final Pattern TAG_INCLUSION_PATTERN = Pattern.compile("^@[^~].+$");
    public static final Pattern TAG_EXCLUSION_PATTERN = Pattern.compile("^@~.+$");
    public static final Pattern LOCATION_INCLUSION_PATTERN = Pattern.compile("^:[^~]\\d+$");
    public static final Pattern LOCATION_EXCLUSION_PATTERN = Pattern.compile("^:~\\d+$");
    public static final Pattern SCENARIO_NAME_INCLUSION_PATTERN = Pattern.compile("^\\$[^~].+$");
    public static final Pattern SCENARIO_NAME_EXCLUSION_PATTERN = Pattern.compile("^\\$~.+$");
    public static final Pattern ENV_MANAGER_PROPERTY_PATTERN = Pattern.compile("(?<=env\\.)\\S+");
    private Patterns() {}

}
package io.lippia.api.lowcode.patterns;

import java.util.regex.Pattern;

public class Patterns {

    /* combination of positive lookbehind & lookahead, finds the 1st word ("var" with "{{" before it and "}}" after it) */
    public static final Pattern OLD_VARIABLE_PATTERN = Pattern.compile("(?<=\\{{2})\\w+(?=}{2})");
    public static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\(var\\.(\\w+).*");
    public static final Pattern ENVIRONMENT_PATTERN = Pattern.compile("\\$\\(env\\.(\\w+).*");
    public static final Pattern PROPERTY_PATTERN = Pattern.compile("\\$\\(pro\\.(\\w+).*");

    public static final Pattern TAG_INCLUSION_PATTERN = Pattern.compile("^@[^~].+$");
    public static final Pattern TAG_EXCLUSION_PATTERN = Pattern.compile("^@~.+$");
    public static final Pattern LOCATION_INCLUSION_PATTERN = Pattern.compile("^:[^~]\\d+$");
    public static final Pattern LOCATION_EXCLUSION_PATTERN = Pattern.compile("^:~\\d+$");
    public static final Pattern SCENARIO_NAME_INCLUSION_PATTERN = Pattern.compile("^\\$[^~].+$");
    public static final Pattern SCENARIO_NAME_EXCLUSION_PATTERN = Pattern.compile("^\\$~.+$");

    private Patterns() {}

}
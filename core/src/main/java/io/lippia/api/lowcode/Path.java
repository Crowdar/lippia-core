package io.lippia.api.lowcode;

import io.lippia.api.lowcode.exception.LippiaException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Path {
    private Path() {
    }

    private static final String DEFAULT_RESOURCES_PATH = System.getProperty("user.dir").concat(File.separator)
            .concat("src").concat(File.separator)
            .concat("test").concat(File.separator)
            .concat("resources").concat(File.separator);

    public static String read(java.nio.file.Path file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file.toFile()));
            return br.lines().collect(Collectors.joining());
        } catch (IOException ex) {
            throw new LippiaException(ex.getMessage()); // improve msg
        }
    }

    public static java.nio.file.Path getDefaultPathTo(String file) {
        return Paths.get(DEFAULT_RESOURCES_PATH.concat(file));
    }
}
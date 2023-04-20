package io.lippia.api.lowcode.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SqlFileReader {
    public static String getQueryString(String file) throws IOException {
        @SuppressWarnings("resource")
        BufferedReader bufferedReader = new BufferedReader(new FileReader(buildPath(file)));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null)
        {
            sb.append(line);
        }
        return sb.toString();
    }

    private static String buildPath(String filename) {
        String base =  System.getProperty("user.dir").concat(File.separator)
                .concat("src").concat(File.separator)
                .concat("test").concat(File.separator)
                .concat("resources").concat(File.separator)
                .concat("queries").concat(File.separator);
        return base + filename;
    }


}

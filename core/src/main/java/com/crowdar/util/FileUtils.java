package com.crowdar.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FileUtils {

    private static String tempDirectoryPath = null;

    private static String getDirPath(String dirName) {
        JarFile jarFile = null;
        // First of all check that temporary directory already created
        // so if No temporary directory will be created
        if (tempDirectoryPath == null) {
            tempDirectoryPath = Files.createTempDir().getAbsolutePath();
        }
        // Now the resources will be copy from JAR to this temporary directory
        if (!new File(tempDirectoryPath + File.separator + dirName.replaceAll("/", "")).exists()) {
            try {
                List<JarEntry> dirEntries = new ArrayList<JarEntry>();
                File directory = null;
                String jarFileName = new File(FileUtils.class.getClassLoader().getResource(dirName).getPath()).getParent()
                        .replaceAll("(!|file:\\\\)", "").replaceAll("(!|file:)", "");
                jarFile = new JarFile(URLDecoder.decode(jarFileName, "UTF-8"));
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();
                    if (jarEntry.getName().startsWith(dirName)) {
                        if (jarEntry.getName().replaceAll("/", "").equals(dirName.replaceAll("/", ""))) {
                            directory = new File(tempDirectoryPath + File.separator + dirName.replaceAll("/", ""));
                            directory.mkdirs();
                        } else
                            dirEntries.add(jarEntry);
                    }
                }
                if (directory == null) {
                    throw new RuntimeException("There is no directory " + dirName + "in the jar file");
                }
                for (JarEntry dirEntry : dirEntries) {
                    if (!dirEntry.isDirectory()) {
                        File dirFile = new File(directory.getParent() + File.separator + dirEntry.getName());
                        dirFile.createNewFile();
                        convertStreamToFile(dirEntry.getName(), dirFile);
                    } else {
                        File dirFile = new File(directory.getParent() + File.separator + dirEntry.getName());
                        dirFile.mkdirs();
                    }
                }
                return directory.getAbsolutePath();
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                try {
                    jarFile.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            throw new RuntimeException("There are problems in creation files in directory " + tempDirectoryPath);
        } else {
            return tempDirectoryPath + File.separator + dirName.replaceAll("/", "");
        }
    }

    private static void convertStreamToFile(String resourceFileName, File file) throws IOException {
        InputStream in = FileUtils.class.getClassLoader().getResourceAsStream(resourceFileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF8"));
        BufferedWriter fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
        String line;
        while ((line = reader.readLine()) != null) {
            fileWriter.write(line + "\n");
        }
        fileWriter.flush();
        fileWriter.close();
        in.close();
    }

    public static boolean platformIsWindows() {
        return File.separatorChar == '\\';
    }

    public static void writeToJson(String jsonFilePath, Object object) {
        try {
            Gson gson = new Gson();
            FileWriter fileWriter = new FileWriter(jsonFilePath);
            fileWriter.write(gson.toJson(object));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<String> readLines(File f, String encoding) {
        try {
            return org.apache.commons.io.FileUtils.readLines(f, encoding);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeLines(File f, String encoding, List<String> lines) {
        try {
            org.apache.commons.io.FileUtils.writeLines(f, encoding, lines);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void modifyLine(List<String> lines, int line, String replacement) {
        lines.set(line, replacement);
    }

    private static final String fileFormat = "files/%s.json";

    public static List<Object> jsonReader(String fileName) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            fileName = String.format(fileFormat, fileName);
            List<Object> users;
            try {
                users = objectMapper.readValue(
                        new File(fileName),
                        objectMapper.getTypeFactory().constructCollectionType(
                                List.class, Object.class));
            } catch (EOFException e) {
                users = new ArrayList<>();
            } catch (FileNotFoundException e) {
                users = new ArrayList<>();
                File f = new File(fileName);
                f.getParentFile().mkdirs();
                f.createNewFile();
            }

            return users;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getValueFromJsonFile(String fileName) {
        LinkedList<String> queue = jsonReader(fileName)
                .stream().map(Object::toString)
                .collect(Collectors.toCollection(LinkedList::new));
        String value = queue.remove();
        return value;
    }

    public static <T> void writeListOutputs(List<T> actualList, List<T> expectedList) {
        try {
            String className = getClassName(actualList);
            String random = DateUtils.getActualDateFormatted("ddmmyyHHmm-ss-SSS");

            writeOutput(actualList, random, "actual".concat(className));

            writeOutput(expectedList, random, "expected".concat(className));
        } catch (Exception e) {
            LogManager.getLogger(FileUtils.class).error(">>> Error trying to write the output: ", e);
        }
    }

    private static <T> void writeOutput(List<T> list, String random, String fileInitialName) throws IOException {
        FileWriter writer = getFileWriter(random, fileInitialName.concat("_"));
        List<String> headers = new ArrayList<>();
        Boolean headersAdded = false;
        for (Object object : list) {
            List<String> outputs = new ArrayList<>();
            Map<String, Object> sorted = MapUtils.sortMap(object);

            addData(headers, headersAdded, outputs, sorted);
            headersAdded = writeDataInFile(writer, headers, headersAdded, outputs);
        }
        writer.close();
    }

    private static Boolean writeDataInFile(FileWriter writer, List<String> headers, Boolean headersAdded, List<String> outputs) throws IOException {
        if (!headersAdded) {
            String headerActual = String.join(",", headers);
            writer.write(headerActual.concat(System.lineSeparator()));
        }
        String output = String.join(",", outputs);
        writer.write(output.concat(System.lineSeparator()));
        return true;
    }

    private static void addData(List<String> headers, Boolean headersAdded, List<String> outputs, Map<String, Object> sorted) {
        Iterator<Map.Entry> iterator = MapUtils.getIterator(sorted);
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();

            String key = entry.getKey();
            Object value = entry.getValue();
            outputs.add(String.valueOf(value));
            if (!headersAdded) {
                headers.add(key.toUpperCase());
            }
        }
    }

    private static <T> String getClassName(List<T> list) {
        return list.iterator().next().getClass().getSimpleName();
    }

    private static FileWriter getFileWriter(String random, String fileName) throws IOException {
        File file = new File(System.getProperty("user.dir").concat(File.separator).concat("target").concat(File.separator).concat("output"));
        file.mkdir();
        return new FileWriter(file.getAbsolutePath().concat(File.separator).concat(fileName + random + ".txt"));
    }
}

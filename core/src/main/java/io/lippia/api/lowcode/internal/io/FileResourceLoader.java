package io.lippia.api.lowcode.internal.io;

import java.lang.reflect.Constructor;

public class FileResourceLoader implements LoaderFactory {

    private Constructor<?> constructor = null;

    public FileResourceLoader() {
        String classpath = "cucumber.runtime.io.FileResourceLoader";
        constructor = getConstructor(classpath);
    }

    @Override
    public Object load() {
        return getInstance(constructor);
    }

}
package io.lippia.api.lowcode.internal.io;

import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface LoaderFactory {

    Logger log = LoggerFactory.getLogger(LoaderFactory.class);

    Object load();

    default Class<?> getClass(String classPath) {
        try {
            return Class.forName(classPath);
        } catch (ClassNotFoundException e) {
            log.error(e.getMessage());
        }

        throw new RuntimeException("Class path {" + classPath + "} could not be located");
    }

    default Object getInstance(Constructor<?> constructor, Object... initArgs) {
        try {
            return constructor.newInstance(initArgs);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            log.error(e.getMessage());
        }

        throw new RuntimeException("pending");
    }

    default Constructor<?>[] getConstructors(String path) {
        Constructor<?>[] constructors = getClass(path).getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
            constructor.setAccessible(true);
        }

        return constructors;
    }

    default Constructor<?> getConstructor(String classPath) {
        Constructor<?> constructor = null;
        try {
            constructor = getClass(classPath).getDeclaredConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage());
        }

        return constructor;
    }

    default Method[] getMethods(String classPath) {
        return getClass(classPath).getDeclaredMethods();
    }

    default Method[] getMethods(Object instance) {
        return instance.getClass().getDeclaredMethods();
    }

    default Method getMethod(String classPath, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = getClass(classPath).getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage());
        }

        return method;
    }

    default Method getMethod(Object instance, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = instance.getClass().getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            log.error(e.getMessage());
        }

        return method;
    }

}
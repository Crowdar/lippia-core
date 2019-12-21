package com.crowdar.core;

public class MyThreadLocal {

    static final class ContextLocal extends ThreadLocal<Context> {
        @Override
        protected Context initialValue() {
            return Context.getInstance();
        }
    }

    private static final ThreadLocal<Context> userThreadLocal = new ContextLocal();

    public static Object getData(String key) {
        return userThreadLocal.get().getData(key);
    }
}

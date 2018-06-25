package com.crowdar.core;

public class MyThreadLocal {

    static final class ContextLocal extends ThreadLocal<Context> {
        @Override
        protected Context initialValue() {
            return Context.getInstance();
        }
    }

    private static final ThreadLocal<Context> userThreadLocal = new ContextLocal();

    public static Context get() {
        return userThreadLocal.get();
    }
}

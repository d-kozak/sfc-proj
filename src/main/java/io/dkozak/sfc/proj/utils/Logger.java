package io.dkozak.sfc.proj.utils;

public class Logger {

    public static Logger logger(Class<?> clazz) {
        return new Logger(clazz.getSimpleName() + ": ");
    }

    private final String prefix;

    private Logger(String prefix) {
        this.prefix = prefix;
    }

    public void log(String message) {
        System.out.println(this.prefix + message);
    }
}

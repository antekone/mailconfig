package org.anadoxin.mailconfig.boot;

class Log {
    public static void put(String fmt, Object... args) {
        String str = String.format(fmt, args);
        System.out.println("[log] " + str);
    }
}

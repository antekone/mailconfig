package org.anadoxin.mailconfig;

import org.anadoxin.mailconfig.boot.Log;
import lombok.*;
import java.util.*;

public class ServerInfo {
    private Map<String, String> options = new Hashtable<String, String>();

    public void setOption(String key, String value) {
        options.put(key, value);
    }

    public String getOption(String key, String defaultValue) {
        String s = getOption(key);
        if(s == null) {
            return defaultValue;
        }

        return s;
    }

    public String getOption(String key) {
        return options.get(key);
    }

    public String getHostName() {
        return getOption("hostname");
    }

    public String getProtocol() {
        return getOption("protocol");
    }

    public boolean isWantSSL() {
        return getOption("ssl", "false").compareTo("true") == 0;
    }

    public boolean isWantIdle() {
        return getOption("idle", "false").compareTo("true") == 0;
    }
}

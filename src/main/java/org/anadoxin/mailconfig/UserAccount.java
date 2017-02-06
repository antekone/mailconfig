package org.anadoxin.mailconfig;

import java.util.*;
import lombok.*;

public class UserAccount {
    private Map<String, String> options = new Hashtable<String, String>();

    @Getter @Setter String name;
    @Getter @Setter String user;
    @Getter @Setter String pass;
    @Getter @Setter String server;
    @Getter @Setter List<String> templates = new ArrayList<String>();

    public void setOption(String name, String value) {
        this.options.put(name, value);
    }

    public String getOption(String name, String defaultValue) {
        String ret = getOption(name);
        return ret != null ? ret : defaultValue;
    }

    public String getOption(String name) {
        String value = this.options.get(name);
        return preprocess(value);
    }

    private String preprocess(String data) {
        String s;
        s = data.replace("${AccountNameId}", name.replace("@", "_").replace(".", "_"));
        return s;
    }
}

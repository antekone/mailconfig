package org.anadoxin.mailconfig;

import org.anadoxin.mailconfig.boot.Log;

import lombok.*;
import java.util.*;

public class OptionSets {
    final Map<String, Map<String, String>> sets = new Hashtable<String, Map<String, String>>();

    public String getOption(String setName, String name) {
        Map<String, String> opts = sets.get(setName);
        if(opts == null) {
            return null;
        }

        return opts.get(name);
    }

    public void setOption(String setName, String valueName, String valueData) {
        Map<String, String> opts = sets.get(setName);
        if(opts == null) {
            opts = new Hashtable<String, String>();
            sets.put(setName, opts);
        }

        opts.put(valueName, valueData);
    }
}

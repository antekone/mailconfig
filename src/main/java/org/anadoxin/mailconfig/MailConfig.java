package org.anadoxin.mailconfig;

import org.anadoxin.mailconfig.boot.*;
import java.io.*;
import org.hjson.*;
import java.util.*;

public class MailConfig {
    private JsonObject config;

    public MailConfig() {

    }

    public boolean initFromReader(Reader rdr) {
        if(rdr == null)
            return false;

        try {
            this.config = JsonValue.readHjson(rdr).asObject();
            return true;
        } catch(IOException ex) {
            Log.put("Error while parsing config file: %s", ex.getMessage());
            return false;
        }
    }

    public List<String> getServerList() {
        JsonObject serversObj = this.config.valueOf("servers").asObject();
        return null;
    }
}

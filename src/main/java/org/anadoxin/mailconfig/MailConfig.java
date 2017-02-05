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
        try {
            return this.config.get("servers").asObject().names();
        } catch(UnsupportedOperationException e) {
            Log.put("can't cast 'servers' from HJSON to Array");
            return null;
        }
    }

    public ServerInfo getServerByJsonValue(JsonValue jv) throws UnsupportedOperationException {
        ServerInfo si = new ServerInfo();
        JsonObject jo = jv.asObject();

        si.setHostName(jo.get("hostname").asString());
        si.setWantSSL(jo.get("ssl").asBoolean());
        si.setProtocol(jo.get("protocol").asString());

        return si;
    }

    private JsonValue getJsonValueInDict(JsonValue collection, String settingKey) {
        try {
            for(JsonObject.Member n: collection.asObject()) {
                if(n.getName().compareTo(settingKey) == 0) {
                    return n.getValue();
                }
            }
        } catch(UnsupportedOperationException e) {
            Log.put("Can't find value when searching for `%s`: %s", settingKey, e.getMessage());
        }

        return null;
    }

    public ServerInfo getServerByName(String name) {
        JsonValue serverObject = getJsonValueInDict(this.config.get("servers"), name);
        if(serverObject == null) {
            Log.put("can't find selected server: `%s`", name);
            return null;
        }

        ServerInfo si = getServerByJsonValue(serverObject);
        return si;
    }

//    public String getLogFile() {
//        return getJsonValueInDict(this.config.get("options"), "logfile").asString();
//    }
}

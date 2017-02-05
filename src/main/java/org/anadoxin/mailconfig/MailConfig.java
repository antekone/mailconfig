package org.anadoxin.mailconfig;

import org.anadoxin.mailconfig.boot.*;
import java.io.*;
import org.hjson.*;
import java.util.*;

public class MailConfig {
    private JsonObject config;
    private OptionSets optionSets = new OptionSets();
    private List<UserAccount> accounts = new ArrayList<UserAccount>();

    public MailConfig() {

    }

    public boolean initFromReader(Reader rdr) {
        if(rdr == null)
            return false;

        try {
            this.config = JsonValue.readHjson(rdr).asObject();

            if(!initOptionSets()) {
                Log.put("Failure during initOptionSets");
                return false;
            }

            if(!initAccounts()) {
                Log.put("Failure during initAccounts");
                return false;
            }

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

    private boolean initAccounts() {
        try {
            for(JsonObject.Member m: this.config.get("accounts").asObject()) {
                final String accName = m.getName();
                final JsonValue accValue = m.getValue();

                UserAccount acc = processAccount(accName, accValue.asObject());
                if(acc == null) {
                    Log.put("Error while defining account '%s'", accName);
                    break;
                }

                accounts.add(acc);
            }

            return true;
        } catch(UnsupportedOperationException e) {
            Log.put("UnsupportedOperationException during initAccounts(): %s", e.getMessage());
        }

        return false;
    }

    private UserAccount processAccount(String name, JsonObject value) {
        UserAccount acc = new UserAccount();
        acc.setName(name);
        acc.setUser(value.get("user").asString());
        acc.setPass(value.get("pass").asString());
        acc.setServer(value.get("server").asString());
        acc.setTemplate(value.get("template").asString());
        return acc;
    }

    private boolean initOptionSets() {
        try {
            for(JsonObject.Member m: this.config.get("option sets").asObject()) {
                final String setName = m.getName();
                final JsonValue setValue = m.getValue();

                processOptionSet(setName, setValue.asObject());
            }

            return true;
        } catch(UnsupportedOperationException e) {
            Log.put("UnsupportedOperationException during initOptionSets(): %s", e.getMessage());
        }

        return false;
    }

    private void processOptionSet(String name, JsonObject dict) {
        for(JsonObject.Member m: dict) {
            final String valueName = m.getName();
            final String valueData = m.getValue().asString();

            Log.put("adding option set %s / %s=%s", name, valueName, valueData);
            this.optionSets.setOption(name, valueName, valueData);
        }
    }
}

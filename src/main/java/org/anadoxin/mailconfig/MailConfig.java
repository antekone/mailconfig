package org.anadoxin.mailconfig;

import org.anadoxin.mailconfig.boot.*;
import java.io.*;
import org.hjson.*;
import java.util.*;
import java.util.stream.*;

public class MailConfig {
    private JsonObject config;
    private OptionSets optionSets = new OptionSets();

    private Map<String, UserAccount> accounts = new Hashtable<String, UserAccount>();
    private Map<String, ServerInfo> servers = new Hashtable<String, ServerInfo>();
    private Map<String, String> globals = new Hashtable<String, String>();

    private interface JsonProcessFlatCallback {
        boolean process(String itemName, String value);
    }

    private interface JsonStoreFlatCallback {
        void store(String itemName, String itemValue);
    }

    private interface JsonProcessCallback<T> {
        T process(String itemName, JsonObject object);
    }

    private interface JsonStoreCallback<T> {
        void store(String itemName, T object);
    }

    public MailConfig() {
    }

    public boolean initFromReader(Reader rdr) {
        if(rdr == null)
            return false;

        try {
            this.config = JsonValue.readHjson(rdr).asObject();

            if(!initFlatSection("global",
                    (name, value) -> { globals.put(name, value); })) {
                Log.put("Failure during initGlobals");
                return false;
            }

            if(!initSection("option sets",
                    (name, object) -> processOptionSet(name, object),
                    (name, object) -> { })) {
                Log.put("Failure during initOptionSets");
                return false;
            }

            if(!initSection("servers",
                    (name, object) -> processServer(name, object),
                    (name, object) -> { servers.put(name, object); })) {
                Log.put("Failure during initServers");
                return false;
            }

            if(!initSection("accounts",
                    (name, object) -> processAccount(name, object),
                    (name, object) -> { accounts.put(name, object); })) {
                Log.put("Failure during initAccounts");
                return false;
            }

            if(!initTemplates()) {
                Log.put("Failure during template resolution");
                return false;
            }

            return true;
        } catch(IOException ex) {
            Log.put("Error while parsing config file: %s", ex.getMessage());
            return false;
        }
    }

    public String getGlobal(String name) {
        return globals.get(name);
    }

    public List<String> getServerList() {
        return servers.keySet().stream().collect(Collectors.toList());
    }

    public List<String> getAccountList() {
        return accounts.keySet().stream().collect(Collectors.toList());
    }

    public ServerInfo getServerByName(String name) {
        return servers.get(name);
    }

    public UserAccount getAccountByName(String name) {
        return accounts.get(name);
    }

    private <T> boolean initFlatSection(String jsonNodeName, JsonStoreFlatCallback store) {
        try {
            JsonValue nodeValue = this.config.get(jsonNodeName);
            if(nodeValue == null) {
                // Need to return 'true', because some sections are optional, so we need to
                // check for validity somewhere else.
                return true;
            }

            for(JsonObject.Member m: nodeValue.asObject()) {
                final String name = m.getName();
                final String value = m.getValue().asString();
                store.store(name, value);
            }

            return true;
        } catch(UnsupportedOperationException e) {
            Log.put("UnsupportedOperationException during initSection, where node name is '%s': %s",
                jsonNodeName, e.getMessage());
        }

        return false;
    }

    private <T> boolean initSection(String jsonNodeName, JsonProcessCallback<T> process, JsonStoreCallback<T> store) {
        try {
            JsonValue nodeValue = this.config.get(jsonNodeName);
            if(nodeValue == null) {
                // Need to return 'true', because some sections are optional, so we need to
                // check for validity somewhere else.
                return true;
            }

            for(JsonObject.Member m: nodeValue.asObject()) {
                final String name = m.getName();
                final JsonValue value = m.getValue();

                T item = process.process(name, value.asObject());
                if(item == null) {
                    Log.put("Error while defining object: '%s'", name);
                    return false;
                }

                store.store(name, item);
            }

            return true;
        } catch(UnsupportedOperationException e) {
            Log.put("UnsupportedOperationException during initSection, where node name is '%s': %s",
                jsonNodeName, e.getMessage());
        }

        return false;
    }

    private ServerInfo processServer(String name, JsonObject value) {
        ServerInfo si = new ServerInfo();

        for(JsonObject.Member m: value) {
            si.setOption(m.getName(), m.getValue().asString());
        }

        return si;
    }

    private UserAccount processAccount(String name, JsonObject value) {
        UserAccount acc = new UserAccount();
        acc.setName(name);
        acc.setUser(value.get("user").asString());
        acc.setPass(value.get("pass").asString());
        acc.setServer(value.get("server").asString());

        for(JsonValue item: value.get("templates").asArray()) {
            acc.getTemplates().add(item.asString());
        }

        return acc;
    }

    private boolean processOptionSet(String name, JsonObject dict) {
        for(JsonObject.Member m: dict) {
            final String valueName = m.getName();
            final String valueData = m.getValue().asString();

            this.optionSets.setOption(name, valueName, valueData);
        }

        return true;
    }

    private boolean initTemplates() {
        for(Map.Entry<String, UserAccount> entry: accounts.entrySet()) {
            UserAccount acc = entry.getValue();
            String accName = entry.getKey();

            for(String templateName: acc.getTemplates()) {
                Map<String, String> options = this.optionSets.getOptionsForOptionSetName(templateName);
                if(options == null) {
                    Log.put("Error: can't locate option set '%s', defined as template in account '%s'.",
                        templateName, accName);

                    return false;
                }

                options.forEach((k, v) -> acc.setOption(k, v));
            }
        }

        return true;
    }
}

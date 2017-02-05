package org.anadoxin.mailconfig;

import java.util.*;
import lombok.*;

public class UserAccount {
    Map<String, String> options = new Hashtable<String, String>();

    @Getter @Setter String name;
    @Getter @Setter String user;
    @Getter @Setter String pass;
    @Getter @Setter String server;
    @Getter @Setter String template;

    void setOption(String name, String value) {
//        options.set(name, value);
    }
}

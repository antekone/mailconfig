package org.anadoxin.mailconfig;

import java.util.*;
import org.anadoxin.mailconfig.boot.Log;

public class PrintRenderer extends AbstractRenderer {
    public void log(String fmt, Object... args) {
        String s = String.format(fmt, args);
        Log.put("[print] %s", s);
    }

    public void invoke() {
        // Print servers, accounts, settings, etc.
        log("Will generate configuration and run/monitor scripts for following accounts:");

        for(String accountName: mc.getAccountList()) {
            log("Account name: %s", accountName);

            UserAccount acc = mc.getAccountByName(accountName);
            ServerInfo si = mc.getServerByName(acc.getServer());

            log("      Server: %s", si.getHostName());
            log("        Idle: %s", si.isWantIdle() ? "yes" : "no");

            for(String optionKey: acc.getOptionKeySet()) {
                log("          --> %s = %s", optionKey, acc.getOption(optionKey));
            }
        }
    }
}

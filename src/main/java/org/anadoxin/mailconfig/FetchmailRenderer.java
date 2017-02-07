package org.anadoxin.mailconfig;

import org.anadoxin.mailconfig.boot.Log;
import org.apache.commons.io.*;
import java.io.*;

public class FetchmailRenderer extends AbstractRenderer {
    public void log(String fmt, Object... args) {
        String s = String.format(fmt, args);
        Log.put("[fetchmail] %s", s);
    }

    public void invoke() {
        for(String accountName: mc.getAccountList()) {
            UserAccount acc = mc.getAccountByName(accountName);
            ServerInfo si = mc.getServerByName(acc.getServer());

            String configFilePath = acc.getOption("configfile");
            File configFile = new File(configFilePath);

            String configFileBaseDir = FilenameUtils.getFullPath(configFilePath);
            Log.put("configFileBaseDir=%s", configFileBaseDir);



            // Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(configFilePath)));
        }
    }
}

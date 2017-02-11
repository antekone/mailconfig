package org.anadoxin.mailconfig;

import org.anadoxin.mailconfig.boot.Log;
import org.apache.commons.io.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.*;
import java.util.*;

public class InvokerRenderer extends AbstractRenderer {
    private CommonContext ctx;

    public InvokerRenderer(CommonContext ctx) {
        this.ctx = ctx;
    }

    public void log(String fmt, Object... args) {
        String s = String.format(fmt, args);
        Log.put("[invoker] %s", s);
    }

    public void invoke() {
        try {
            doGenerate();
        } catch(FileNotFoundException e) {
            log("File not found: %s", e.getMessage());
        } catch(IOException e) {
            log("I/O exception: %s", e.getMessage());
        }
    }

    private void doGenerate() throws FileNotFoundException, IOException {
        String invokerPathString = mc.getGlobal("invoker");

        PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(invokerPathString))));
        pw.printf("#!/usr/bin/env bash\n\n");

        for(String accName: mc.getAccountList()) {
            UserAccount acc = mc.getAccountByName(accName);
            String configFilePathString = acc.getOption("configfile");
            String homePathString = FilenameUtils.getFullPath(configFilePathString);
            String runscriptFilePathString = homePathString + "/runscript.sh";

            if(!checkString(configFilePathString) || !checkString(homePathString)) {
                log("Fatal: missing `configfile` option");
                return;
            }

            pw.printf("%s\n", runscriptFilePathString);

            Set<PosixFilePermission> set = new HashSet<PosixFilePermission>();
            set.add(PosixFilePermission.OWNER_READ);
            set.add(PosixFilePermission.OWNER_WRITE);
            set.add(PosixFilePermission.OWNER_EXECUTE);
            set.add(PosixFilePermission.GROUP_READ);
            set.add(PosixFilePermission.GROUP_EXECUTE);
            set.add(PosixFilePermission.OTHERS_READ);
            set.add(PosixFilePermission.OTHERS_EXECUTE);
            Files.setPosixFilePermissions(FileSystems.getDefault().getPath(invokerPathString), set);
        }

        pw.close();
    }
}


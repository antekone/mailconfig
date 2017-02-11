package org.anadoxin.mailconfig;

import org.anadoxin.mailconfig.boot.Log;

import java.util.*;

public class CommonContext {
    List<String> runScriptFilePaths = new ArrayList<String>();

    public void addRunScriptFilePath(String runScriptFilePath) {
        runScriptFilePaths.add(runScriptFilePath);
    }

    public List<String> getRunScriptFilePaths() {
        return runScriptFilePaths;
    }
}

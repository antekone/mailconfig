package org.anadoxin.mailconfig;

import org.anadoxin.mailconfig.boot.Log;

abstract public class AbstractRenderer {
    protected MailConfig mc;

    public void setMailConfig(MailConfig mc) {
        this.mc = mc;
    }

    protected boolean checkString(String str) {
        return str != null && !str.equals("");
    }

    abstract public void invoke();
}

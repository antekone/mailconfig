package org.anadoxin.mailconfig;

import org.anadoxin.mailconfig.boot.Log;

abstract public class AbstractRenderer {
    protected MailConfig mc;

    public void setMailConfig(MailConfig mc) {
        this.mc = mc;
    }

    abstract public void invoke();
}

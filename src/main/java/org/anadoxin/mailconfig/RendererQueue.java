package org.anadoxin.mailconfig;

import java.util.*;

public class RendererQueue {
    private List<AbstractRenderer> renderers = new ArrayList<AbstractRenderer>();
    private MailConfig mc;

    public void addRenderer(AbstractRenderer r) {
        r.setMailConfig(mc);
        renderers.add(r);
    }

    public void setMailConfig(MailConfig mc) {
        this.mc = mc;
    }

    public void run() {
        renderers.forEach((r) -> r.invoke());
    }
}

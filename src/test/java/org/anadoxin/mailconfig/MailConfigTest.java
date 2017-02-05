package org.anadoxin.test;

import static org.junit.jupiter.api.Assertions.*;

import org.anadoxin.mailconfig.boot.*;
import org.anadoxin.mailconfig.*;
import org.junit.jupiter.api.*;

class MailConfigTest {
    private MailConfig mc = new MailConfig();

    @Test
    void ListServersInvalidArguments() {
        assertFalse(mc.initFromReader(null));
    }
}


package org.anadoxin.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.io.*;

import org.anadoxin.mailconfig.boot.*;
import org.anadoxin.mailconfig.*;
import org.junit.jupiter.api.*;

class MailConfig1Test {
    private MailConfig mc = new MailConfig();

    @BeforeEach
    void loadMailConfig() throws FileNotFoundException {
        InputStream dataFile = this.getClass().getClassLoader().getResourceAsStream("Test1.hjson");
        assertNotNull(dataFile);
        InputStreamReader rdr = new InputStreamReader(dataFile);
        assertTrue(mc.initFromReader(rdr));
    }

    @Test
    void ListServersValidArguments1() {
        List<String> servers = mc.getServerList();
        assertNotNull(servers);
    }
}


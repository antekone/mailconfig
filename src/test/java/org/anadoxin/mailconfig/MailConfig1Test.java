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
    void ListServersValidArgumentsNullCheck() {
        List<String> servers = mc.getServerList();
        assertNotNull(servers);
    }

    @Test
    void ListServersValidArgumentsSizeMoreThan0() {
        List<String> servers = mc.getServerList();
        assertEquals(servers.size(), 3);
    }

    @Test
    void ListServersValid() {
        List<String> servers = mc.getServerList();
        assertEquals(servers.get(0), "gmail imap");
        assertEquals(servers.get(1), "zoho imap");
    }

    @Test
    void GetServerByName1NotNull() {
        ServerInfo si = mc.getServerByName("gmail imap");
        assertNotNull(si);
    }

    @Test
    void GetServerByName1CheckHostName() {
        ServerInfo si = mc.getServerByName("gmail imap");
        assertNotNull(si.getHostName());
    }

    @Test
    void GetServerByName1CheckHostNameValue() {
        ServerInfo si = mc.getServerByName("gmail imap");
        assertEquals(si.getHostName(), "imap.gmail.com");
    }

    @Test
    void GetServerByName2CheckHostNameValue() {
        ServerInfo si = mc.getServerByName("zoho imap");
        assertEquals(si.getHostName(), "imap.zoho.com");
    }

    @Test
    void GetServerByName3CheckSSL1() {
        assertTrue(mc.getServerByName("gmail imap").isWantSSL());
    }

    @Test
    void GetServerByName3CheckSSL2() {
        assertFalse(mc.getServerByName("zoho imap").isWantSSL());
    }

    @Test
    void GetServerByName4Protocol() {
        assertEquals(mc.getServerByName("gmail imap").getProtocol(), "imap");
        assertEquals(mc.getServerByName("zoho imap").getProtocol(), "imap");
        assertEquals(mc.getServerByName("some pop3").getProtocol(), "pop3");
    }

    @Test
    void CheckOptions() {
        assertEquals(mc.getAccountByName("antonone1@gmail.com").getOption("logfile"), "/path/to/antonone1_gmail_com.log");
        assertEquals(mc.getAccountByName("antonone1@gmail.com").getOption("mda"), "/path/to/mda --args sth");
        assertEquals(mc.getAccountByName("antonone1@gmail.com").getOption("interval"), "1");

        assertEquals(mc.getAccountByName("antek11@zoho.com").getOption("logfile"), "/path/to/antek11_zoho_com.log");
        assertEquals(mc.getAccountByName("antek11@zoho.com").getOption("mda"), "/path/to/mda --args sth");
        assertEquals(mc.getAccountByName("antek11@zoho.com").getOption("interval"), "1");

        assertEquals(mc.getAccountByName("third@sth.com").getOption("logfile"), "/shadowed");
        assertEquals(mc.getAccountByName("third@sth.com").getOption("mda"), "/path/to/mda --args sth");
        assertEquals(mc.getAccountByName("third@sth.com").getOption("interval"), "1");
    }
}

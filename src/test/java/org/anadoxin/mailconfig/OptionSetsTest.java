package org.anadoxin.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;
import java.io.*;

import org.anadoxin.mailconfig.boot.*;
import org.anadoxin.mailconfig.*;
import org.junit.jupiter.api.*;

class OptionSetsTest {
    private OptionSets set;

    @BeforeEach
    void setUp() {
        set = new OptionSets();
    }


    @Test
    void nothing() {
        assertNull(set.getOption("setname", "nooption"));
    }

    @Test
    void add1() {
        set.setOption("one", "a", "1");
        assertEquals(set.getOption("one", "a"), "1");
    }

    @Test
    void add2() {
        set.setOption("one", "a", "1");
        set.setOption("one", "b", "2");
        assertEquals(set.getOption("one", "a"), "1");
        assertEquals(set.getOption("one", "b"), "2");
    }

    @Test
    void add3() {
        set.setOption("one", "a", "1");
        assertEquals(set.getOption("one", "c"), null);
    }

    @Test
    void add4() {
        set.setOption("one", "a", "1");
        set.setOption("one", "b", "2");
        assertEquals(set.getOption("two", "a"), null);
        assertEquals(set.getOption("one", "b"), "2");
    }
}

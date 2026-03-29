package com.doubleentrybookkeeping.domain.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;

import org.junit.Test;
public class EntryTests {
    @Test
    public void create_base_entry() {
        var entry = new Entry(1, 1, 1, "cash", 50);
        assertEquals(1, entry.getId());
        assertEquals("cash", entry.getMethod());
    }
}

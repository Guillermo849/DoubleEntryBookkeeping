package com.doubleentrybookkeeping.domain.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Test;

import java.util.Date;

public class EntryTests {
    @Test
    public void create_base_entry() {
        var entry = new Entry(1, 1, Entry.Type.REVENUE,Entry.PayMethod.CARD, 50, "14/06/2026", "ConceptTest", "ImageTest");
        assertEquals(1, entry.getId());
        assertEquals(Entry.PayMethod.CARD, entry.getPayMethod());
    }
}

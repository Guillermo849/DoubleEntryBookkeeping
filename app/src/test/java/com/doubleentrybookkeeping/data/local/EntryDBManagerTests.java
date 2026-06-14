package com.doubleentrybookkeeping.data.local;

import static org.junit.Assert.assertEquals;

import com.doubleentrybookkeeping.domain.models.Category;
import com.doubleentrybookkeeping.domain.models.DbObject;
import com.doubleentrybookkeeping.domain.models.Entry;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.text.DateFormat;
import java.util.Date;

public class EntryDBManagerTests extends TestBase{

    public EntryDBManagerTests() throws FileNotFoundException {
    }

    @Test
    public void test_addDbObject_returns_entry() {
        Category category = new Category(1, 0, "Supermercado");
        Entry entry = new Entry(1, 1, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 15.0f);
        DbObject returnedObject = entryDbManager.addDbObject(entry);
        assertEquals(entry, returnedObject);
    }

    @Test
    public void test_modifyDbObject_returns_entry() {
        Category category = new Category(1, "Supermercado");
        Entry entry = new Entry(2, 1, Entry.Type.REVENUE, Entry.PayMethod.CASH, 15.0f, "14/06/2026", "Sabrosura", "imagen");
        DbObject returnedObject = entryDbManager.modifyDbObject(entry);
        assertEquals(entry, returnedObject);
    }

    @Test
    public void test_deleteDbObject_does_not_throw() {
        Category category = new Category(1, "Supermercado");
        Entry entry = new Entry(3, 1, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 15.0f);
        entryDbManager.deleteDbObject(entry);
    }

    // TODO: Fix Tests
    @Test
    public void return_entry_with_given_category() throws FileNotFoundException {
        // Act
        var filteredEntries = entryDbManager.FilterByCategoryID(3);
        Entry entry = new Entry(1, 1, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 15.0f);
        // Assert
        assertEquals(1, filteredEntries.length);
        assertEquals(entry.getId(), filteredEntries[0].getId());
    }

    @Test
    public void return_entry_with_given_date() throws FileNotFoundException {
        // Act
        var filteredEntries = entryDbManager.FilterByDate("14/06/2026");
        // Assert
        assertEquals(1, filteredEntries.length);
    }

}

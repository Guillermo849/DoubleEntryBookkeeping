package com.doubleentrybookkeeping.data.local;

import static org.junit.Assert.assertEquals;

import com.doubleentrybookkeeping.domain.models.Category;
import com.doubleentrybookkeeping.domain.models.DbObject;
import com.doubleentrybookkeeping.domain.models.Entry;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Date;

public class EntryDBManagerTests {
    private final String pathToDb = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\EntriesDB.csv";
    private final EntryDBManager manager = new EntryDBManager(pathToDb);

    public EntryDBManagerTests() throws FileNotFoundException {
    }

    @Test
    public void test_addDbObject_returns_entry() {
        Category category = new Category(1, 0, "Supermercado");
        Entry entry = new Entry(1, 1, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 15.0f);
        DbObject returnedObject = manager.addDbObject(entry);
        assertEquals(entry, returnedObject);
    }

    @Test
    public void test_modifyDbObject_returns_entry() {
        Category category = new Category(1, "Supermercado");
        Entry entry = new Entry(2, 1, Entry.Type.REVENUE, Entry.PayMethod.CASH, 15.0f, new Date(), "Sabrosura", "imagen");
        DbObject returnedObject = manager.modifyDbObject(entry);
        assertEquals(entry, returnedObject);
    }

    @Test
    public void test_deleteDbObject_does_not_throw() {
        Category category = new Category(1, "Supermercado");
        Entry entry = new Entry(3, 1, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 15.0f);
        manager.deleteDbObject(entry);
    }
}

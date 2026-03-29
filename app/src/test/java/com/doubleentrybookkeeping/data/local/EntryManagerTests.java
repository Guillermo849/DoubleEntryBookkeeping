package com.doubleentrybookkeeping.data.local;

import static org.junit.Assert.assertEquals;

import com.doubleentrybookkeeping.domain.models.Category;
import com.doubleentrybookkeeping.domain.models.DbObject;
import com.doubleentrybookkeeping.domain.models.Entry;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.Date;

public class EntryManagerTests {
    private final String pathToDb = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\EntriesDB.csv";
    private final EntryManager manager = new EntryManager(pathToDb);

    public EntryManagerTests() throws FileNotFoundException {
    }

    @Test
    public void test_addDbObject_returns_entry() {
        Category category = new Category(1, 0, "Supermercado");
        Entry entry = new Entry(1, category, "Gasto", "Cash", 15.0, new Date(), "Sabrosura");
        DbObject returnedObject = manager.addDbObject(entry);
        assertEquals(entry, returnedObject);
    }

    @Test
    public void test_modifyDbObject_returns_entry() {
        Category category = new Category(1, "Supermercado");
        Entry entry = new Entry(1, category, "Gasto", "Cash", 15.0, new Date(), "Sabrosura");
        DbObject returnedObject = manager.modifyDbObject(entry);
        assertEquals(entry, returnedObject);
    }

    @Test
    public void test_deleteDbObject_does_not_throw() {
        Category category = new Category(1, "Supermercado");
        Entry entry = new Entry(1, category, "Gasto", "Cash", 15.0, new Date(), "Sabrosura");
        manager.deleteDbObject(entry);
    }
}

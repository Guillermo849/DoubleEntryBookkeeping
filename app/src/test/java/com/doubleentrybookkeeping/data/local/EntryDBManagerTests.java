package com.doubleentrybookkeeping.data.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.doubleentrybookkeeping.domain.models.DbObject;
import com.doubleentrybookkeeping.domain.models.Entry;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

public class EntryDBManagerTests extends TestBase{

    public EntryDBManagerTests() throws FileNotFoundException {
    }

    @Test
    public void test_addDbObject_adds_entry_to_file() throws IOException {
        // Arrange
        Entry entry = new Entry(1, 1, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 15.0f);

        // Act
        DbObject returnedObject = manager.getEntryManager().addDbObject(entry);

        // Assert
        assertEquals(entry, returnedObject);
        List<String> lines = Files.readAllLines(new File(pathToEntryDb).toPath());
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("1,1,EXPENSES,CARD,15.0")));
    }

    @Test
    public void test_addDbObject_returns_same_entry_object() {
        // Arrange
        Entry entry = new Entry(10, 2, Entry.Type.REVENUE, Entry.PayMethod.CASH, 99.99f);

        // Act
        DbObject result = manager.getEntryManager().addDbObject(entry);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof Entry);
        Entry returned = (Entry) result;
        assertEquals(10, returned.getId());
        assertEquals(2, returned.getCategory());
        assertEquals(Entry.Type.REVENUE, returned.getType());
        assertEquals(Entry.PayMethod.CASH, returned.getPayMethod());
        assertEquals(99.99f, returned.getAmount(), 0.001f);
    }

    @Test
    public void test_addDbObject_does_not_alter_existing_entries() throws IOException {
        // Arrange
        Entry newEntry = new Entry(5, 3, Entry.Type.REVENUE, Entry.PayMethod.CASH, 50.0f);
        List<String> linesBefore = Files.readAllLines(new File(pathToEntryDb).toPath());
        int countBefore = linesBefore.size();

        // Act
        manager.getEntryManager().addDbObject(newEntry);

        // Assert
        List<String> linesAfter = Files.readAllLines(new File(pathToEntryDb).toPath());
        assertEquals(countBefore + 1, linesAfter.size());
        assertTrue(linesAfter.stream().anyMatch(line -> line.startsWith("2,1,REVENUE")));
        assertTrue(linesAfter.stream().anyMatch(line -> line.startsWith("3,1,EXPENSES")));
    }

    @Test
    public void test_addDbObject_multiple_entries_all_correct() throws IOException {
        // Arrange
        Entry entry1 = new Entry(11, 1, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 10.0f);
        Entry entry2 = new Entry(12, 2, Entry.Type.REVENUE, Entry.PayMethod.CASH, 20.0f);
        Entry entry3 = new Entry(13, 3, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 30.0f);

        // Act
        manager.getEntryManager().addDbObject(entry1);
        manager.getEntryManager().addDbObject(entry2);
        manager.getEntryManager().addDbObject(entry3);

        // Assert
        List<String> lines = Files.readAllLines(new File(pathToEntryDb).toPath());
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("11,")));
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("12,")));
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("13,")));
    }

    @Test
    public void test_modifyDbObject_modifies_existing_entry() throws IOException {
        // Arrange
        Entry entry = new Entry(2, 2, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 20.0f, "20260201", "Modified", "new_image");

        // Act
        DbObject returnedObject = manager.getEntryManager().modifyDbObject(entry);

        // Assert
        assertEquals(entry, returnedObject);
        List<String> lines = Files.readAllLines(new File(pathToEntryDb).toPath());
        assertFalse(lines.stream().anyMatch(line -> line.contains("Sabrosura")));
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("2,2,EXPENSES,CARD,20.0")));
    }

    @Test
    public void test_modifyDbObject_returns_the_modified_entry() {
        // Arrange
        Entry updatedEntry = new Entry(2, 5, Entry.Type.REVENUE, Entry.PayMethod.CASH, 77.5f);

        // Act
        DbObject result = manager.getEntryManager().modifyDbObject(updatedEntry);

        // Assert
        assertNotNull(result);
        Entry returned = (Entry) result;
        assertEquals(2, returned.getId());
        assertEquals(5, returned.getCategory());
        assertEquals(Entry.Type.REVENUE, returned.getType());
        assertEquals(Entry.PayMethod.CASH, returned.getPayMethod());
        assertEquals(77.5f, returned.getAmount(), 0.001f);
    }

    @Test
    public void test_modifyDbObject_does_not_duplicate_entry() throws IOException {
        // Arrange
        Entry updatedEntry = new Entry(2, 1, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 99.0f);
        List<String> linesBefore = Files.readAllLines(new File(pathToEntryDb).toPath());
        int countBefore = linesBefore.size();

        // Act
        manager.getEntryManager().modifyDbObject(updatedEntry);

        // Assert
        List<String> linesAfter = Files.readAllLines(new File(pathToEntryDb).toPath());
        assertEquals(countBefore, linesAfter.size());
        long count = linesAfter.stream().filter(line -> line.startsWith("2,")).count();
        assertEquals(1, count);
    }

    @Test
    public void test_deleteDbObject_removes_entry() throws IOException {
        // Arrange
        Entry entry = new Entry(3, 1, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 15.0f);

        // Act
        manager.getEntryManager().deleteDbObject(entry);

        // Assert
        List<String> lines = Files.readAllLines(new File(pathToEntryDb).toPath());
        assertFalse(lines.stream().anyMatch(line -> line.startsWith("3,")));
    }

    @Test
    public void test_deleteDbObject_reduces_line_from_file() throws IOException {
        // Arrange
        Entry entry = new Entry(2, 1, Entry.Type.REVENUE, Entry.PayMethod.CASH, 15.0f);
        List<String> linesBefore = Files.readAllLines(new File(pathToEntryDb).toPath());
        int countBefore = linesBefore.size();

        // Act
        manager.getEntryManager().deleteDbObject(entry);

        // Assert
        List<String> linesAfter = Files.readAllLines(new File(pathToEntryDb).toPath());
        assertEquals(countBefore - 1, linesAfter.size());
    }

    @Test
    public void test_deleteDbObject_only_removes_exact_id_match() throws IOException {
        // Arrange
        Entry entry20 = new Entry(20, 1, Entry.Type.REVENUE, Entry.PayMethod.CASH, 50.0f);
        manager.getEntryManager().addDbObject(entry20);
        Entry entryToDelete = new Entry(2, 1, Entry.Type.REVENUE, Entry.PayMethod.CASH, 15.0f);

        // Act
        manager.getEntryManager().deleteDbObject(entryToDelete);

        // Assert
        List<String> lines = Files.readAllLines(new File(pathToEntryDb).toPath());
        assertFalse(lines.stream().anyMatch(line -> line.startsWith("2,")));
        assertTrue(lines.stream().anyMatch(line -> line.startsWith("20,")));
    }

    // TODO: Fix Tests
    @Test
    public void return_entry_with_given_category() {
        // Act
        var filteredEntries = manager.getEntryManager().FilterByCategoryID(3);
        Entry entry = new Entry(1, 1, Entry.Type.EXPENSES, Entry.PayMethod.CARD, 15.0f);
        // Assert
        assertEquals(1, filteredEntries.length);
        assertEquals(entry.getId(), filteredEntries[0].getId());
    }

    @Test
    public void return_entry_with_given_date() {
        // Act
        var filteredEntries = manager.getEntryManager().FilterByDate("20260201");
        // Assert
        assertEquals(1, filteredEntries.length);
    }

}

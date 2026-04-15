package com.doubleentrybookkeeping.data.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class DataBaseManagerTests {
    private final String pathCategoryToDb = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\CategoryDB.csv";
    private final String pathToEntryDb = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\EntriesDB.csv";
    private final DataBaseManager manager = new DataBaseManager(pathCategoryToDb, pathToEntryDb);

    public DataBaseManagerTests() throws FileNotFoundException {
    }

    @Test
    public void check_that_db_path_was_created_correctly() {
        Assert.assertEquals(pathCategoryToDb, manager.getCategoryManager().getDbPath());
    }

    @Test
    public void throw_exception_when_db_path_is_empty() {
        Exception exception = assertThrows(NullPointerException.class, () -> new DataBaseManager("", ""));
        assertEquals(EnumErrors.DataBase.PATH_EMPTY.message, exception.getMessage());
    }

    @Test
    public void check_that_db_file_exists() {
        assertTrue(manager.getCategoryManager().categoryDbExists());
        assertTrue(manager.getEntryManager().entryDbExists());
    }

    @Test
    public void throw_exception_db_file_doesnt_exists() {
        Exception exception = assertThrows(FileNotFoundException.class, () -> new DataBaseManager(".\\path\\to\\nowhere.csv", ".\\path\\to\\nowhere.csv"));
        assertEquals(EnumErrors.DataBase.FILE_NOT_EXISTS.message, exception.getMessage());
    }

    @Test
    public void export_data_bases_format_is_ok() {
        String expectedFormat = "id,name,base_category\n\n" +
                "id,category,type,method,amount,date,concept,image\n";
        assertEquals(expectedFormat, manager.getDataBaseExportData());
    }
}

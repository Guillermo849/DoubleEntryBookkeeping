package com.doubleentrybookkeeping.data.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;

import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class DataBaseManagerTests extends TestBase {

    public DataBaseManagerTests() throws FileNotFoundException {
    }

    @Test
    public void data_base_managers_db_paths_are_ok() {
        Assert.assertEquals(pathToCategoryDb, manager.getCategoryManager().getDbPath());
        Assert.assertEquals(pathToEntryDb, manager.getEntryManager().getDbPath());
    }

    @Test
    public void when_db_path_is_empty_throws_exception() {
        Exception exception = assertThrows(NullPointerException.class, () -> new DataBaseManager("", ""));
        assertEquals(EnumErrors.DataBase.PATH_EMPTY.message, exception.getMessage());
    }

    @Test
    public void check_that_db_file_exists() {
        assertTrue(manager.getCategoryManager().categoryDbExists());
        assertTrue(manager.getEntryManager().entryDbExists());
    }

    @Test
    public void db_file_doesnt_exists_throws_exception() {
        Exception exception = assertThrows(FileNotFoundException.class, () -> new DataBaseManager(".\\path\\to\\nowhere.csv", ".\\path\\to\\nowhere.csv"));
        assertEquals(EnumErrors.DataBase.FILE_NOT_EXISTS.message, exception.getMessage());
    }

    @Test
    public void export_data_bases_format_is_ok() {
        String expectedFormat = "id,name,base_category\n\n" +
                "id,category,type,method,amount,date,concept,image\n";
        assertEquals(expectedFormat, manager.getDataBaseExportData());
    }

    @Test
    public void export_data_bases_to_file_is_ok() throws IOException {
        String exportPath = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\ExportedDb.csv";
        manager.exportDataBasesTo(exportPath);
        assertTrue(new File(exportPath).exists());
    }

    @Test
    public void delete_category_cascade_is_ok() {

    }

    @Test
    public void import_data_base_to_file_is_ok() throws IOException {
        String categoryImportPath = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\CategoryDB.csv";
        String entriesImportPath = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\EntriesDB.csv";
        String dBImportPath = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\ExportedDb.csv";
        manager.importDataBaseFrom(categoryImportPath, entriesImportPath, dBImportPath);
        assertTrue(new File(categoryImportPath).exists() && new File(entriesImportPath).exists());
    }
}

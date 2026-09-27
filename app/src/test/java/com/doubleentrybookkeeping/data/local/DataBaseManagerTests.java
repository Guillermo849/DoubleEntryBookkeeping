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
    public void export_data_bases_to_file_is_ok() throws IOException {
        String exportPath = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\ExportedDb.csv";
        manager.exportDataBasesTo(exportPath);
        assertTrue(new File(exportPath).exists());
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

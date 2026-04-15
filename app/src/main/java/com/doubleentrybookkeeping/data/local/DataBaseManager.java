package com.doubleentrybookkeeping.data.local;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public class DataBaseManager {
    private final CategoryDbManager categoryDbManager;

    private final EntryDBManager entryDBManager;

    public DataBaseManager(String pathToCategoryDb, String pathToEntryDb) throws FileNotFoundException {
        categoryDbManager = new CategoryDbManager(pathToCategoryDb);
        entryDBManager = new EntryDBManager(pathToEntryDb);
    }

    public CategoryDbManager getCategoryManager() {
        return categoryDbManager;
    }

    public EntryDBManager getEntryManager() {
        return entryDBManager;
    }

    public String getDataBaseExportData() {
        StringBuilder exportDbDataBuilder = new StringBuilder();

        getDataFromDb(exportDbDataBuilder, categoryDbManager.getDbPath());
        exportDbDataBuilder.append("\n");
        getDataFromDb(exportDbDataBuilder, entryDBManager.getDbPath());

        return exportDbDataBuilder.toString();
    }

    private void getDataFromDb(StringBuilder exportDbDataBuilder, String dbpath) {
        try (BufferedReader br = new BufferedReader(new FileReader(dbpath))) {
            String line;

            while ((line = br.readLine()) != null)
                exportDbDataBuilder.append(line).append("\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

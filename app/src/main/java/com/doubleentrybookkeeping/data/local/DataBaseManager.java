package com.doubleentrybookkeeping.data.local;


import java.io.FileNotFoundException;

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
}

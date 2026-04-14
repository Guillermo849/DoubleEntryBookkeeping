package com.doubleentrybookkeeping.data.local;


import java.io.FileNotFoundException;

public class DataBaseManager {
    private final CategoryDbManager categoryDbManager;

    public DataBaseManager(String pathToCategoryDb) throws FileNotFoundException {
        categoryDbManager = new CategoryDbManager(pathToCategoryDb);
    }

    public CategoryDbManager getCategoryManager() {
        return categoryDbManager;
    }
}

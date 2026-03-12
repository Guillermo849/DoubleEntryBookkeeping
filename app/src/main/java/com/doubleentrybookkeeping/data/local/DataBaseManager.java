package com.doubleentrybookkeeping.data.local;


import java.io.File;

public class DataBaseManager {
    private final File categoryDb;

    public DataBaseManager(String pathToDb) {
        if (pathToDb.isEmpty())
            throw new NullPointerException(EnumErrors.DataBase.PATH_EMPTY.message);

        categoryDb = new File(pathToDb);
    }

    public String getDbPath(){
        return categoryDb.getPath();
    }

    public boolean categoryDbExists() {
        return categoryDb.exists();
    }
}

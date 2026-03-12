package com.doubleentrybookkeeping.data.local;


import java.io.File;
import java.io.FileNotFoundException;

public class DataBaseManager {
    private final File categoryDb;

    public DataBaseManager(String pathToDb) throws FileNotFoundException {
        if (pathToDb.isEmpty())
            throw new NullPointerException(EnumErrors.DataBase.PATH_EMPTY.message);

        categoryDb = new File(pathToDb);
        checkDataBaseFileExists();
    }

    public String getDbPath() {
        return categoryDb.getPath();
    }

    public boolean categoryDbExists() {
        return categoryDb.exists();
    }

    public void checkDataBaseFileExists() throws FileNotFoundException {
        if (!categoryDbExists()) {
            throw new FileNotFoundException(EnumErrors.DataBase.FILE_NOT_EXISTS.message);
        }
    }
}

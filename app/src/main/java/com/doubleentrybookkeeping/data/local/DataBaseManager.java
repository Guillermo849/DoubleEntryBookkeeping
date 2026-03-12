package com.doubleentrybookkeeping.data.local;


import java.io.File;
import java.io.FileNotFoundException;

public class DataBaseManager {
    private final File categoryDb;

    public DataBaseManager(String pathToDb) throws FileNotFoundException {
        checkDataBasePathIsEmpty(pathToDb);

        categoryDb = new File(pathToDb);
        checkDataBaseFileExists();
    }

    public void checkDataBasePathIsEmpty(String pathToDb){
        if (pathToDb.isEmpty())
            throw new NullPointerException(EnumErrors.DataBase.PATH_EMPTY.message);
    }

    public void checkDataBaseFileExists() throws FileNotFoundException {
        if (!categoryDbExists())
            throw new FileNotFoundException(EnumErrors.DataBase.FILE_NOT_EXISTS.message);
    }

    public boolean categoryDbExists() {
        return categoryDb.exists();
    }

    public String getDbPath() {
        return categoryDb.getPath();
    }
}

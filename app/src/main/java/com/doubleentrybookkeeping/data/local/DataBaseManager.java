package com.doubleentrybookkeeping.data.local;


import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;

import java.io.File;
import java.io.FileNotFoundException;

public class DataBaseManager {
    private final File dbFile;

    public DataBaseManager(String pathToDb) throws FileNotFoundException {
        checkDataBasePathIsEmpty(pathToDb);

        dbFile = new File(pathToDb);
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
        return dbFile.exists();
    }

    public String getDbPath() {
        return dbFile.getPath();
    }
}

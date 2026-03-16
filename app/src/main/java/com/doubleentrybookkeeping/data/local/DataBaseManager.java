package com.doubleentrybookkeeping.data.local;


import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;
import com.doubleentrybookkeeping.domain.models.DbObject;

import java.io.File;
import java.io.FileNotFoundException;

import kotlin.NotImplementedError;

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

    public DbObject addDbObject(DbObject dbObject) {
        throw new NotImplementedError();
    }

    public DbObject modifyDbObject(DbObject dbObject) {
        throw new NotImplementedError();
    }

    public void deleteDbObject(DbObject dbObject) {
        throw new NotImplementedError();
    }
}

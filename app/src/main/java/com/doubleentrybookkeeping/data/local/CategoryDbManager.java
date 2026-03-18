package com.doubleentrybookkeeping.data.local;

import com.doubleentrybookkeeping.domain.models.DbObject;

import java.io.FileNotFoundException;

import kotlin.NotImplementedError;

public class CategoryDbManager extends DataBaseManager{

    public CategoryDbManager(String pathToDb) throws FileNotFoundException {
        super(pathToDb);
    }


    @Override
    public DbObject addDbObject(DbObject dbObject) {
        throw new NotImplementedError();
    }

    @Override
    public DbObject modifyDbObject(DbObject dbObject) {
        throw new NotImplementedError();
    }

    @Override
    public void deleteDbObject(DbObject dbObject) {
        throw new NotImplementedError();
    }
}

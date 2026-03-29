package com.doubleentrybookkeeping.data.local;

import com.doubleentrybookkeeping.domain.models.DbObject;
import com.doubleentrybookkeeping.domain.models.Entry;

import java.io.FileNotFoundException;
import java.util.Date;

import kotlin.NotImplementedError;

public class FilterDBManager extends DataBaseManager{

    public FilterDBManager(String pathToDb) throws FileNotFoundException {
        super(pathToDb);
    }

    public Entry[] FilterByCategory(int categoryID) {

        return null;
    }

    public Entry[] FilterByDate(Date date) {

        return null;
    }

    public Entry[] FilterByType(Type type) {

        return null;
    }

    public enum Type {
        EXPENSES,
        REVENUE
    }

}

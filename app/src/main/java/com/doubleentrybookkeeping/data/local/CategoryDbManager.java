package com.doubleentrybookkeeping.data.local;

import java.io.FileNotFoundException;

public class CategoryDbManager extends DataBaseManager{

    public CategoryDbManager(String pathToDb) throws FileNotFoundException {
        super(pathToDb);
    }


}

package com.doubleentrybookkeeping.data.local;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;

public class CategoryDbManagerTests {
    private final String pathToDb = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\CategoryDB.csv";

    @Test
    public void check_that_CategoryDbManager_extends_from_DataBaseManagerTest() throws FileNotFoundException {
        CategoryDbManager categoryDbManager = new CategoryDbManager(pathToDb);
        Assert.assertEquals(pathToDb, categoryDbManager.getDbPath());
        Assert.assertTrue(categoryDbManager.categoryDbExists());
    }


}

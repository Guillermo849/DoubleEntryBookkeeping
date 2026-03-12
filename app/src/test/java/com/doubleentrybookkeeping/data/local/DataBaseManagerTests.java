package com.doubleentrybookkeeping.data.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

public class DataBaseManagerTests {
    private final String pathToDb = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\CategoryDB.csv";
    private DataBaseManager manager = new DataBaseManager(pathToDb);

    @Test
    public void check_that_db_path_was_created_correctly() {
        Assert.assertEquals(pathToDb, manager.getDbPath());
    }

    @Test
    public void throw_exception_when_db_path_is_empty() {
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new DataBaseManager("");
        });
        assertEquals(EnumErrors.DataBase.PATH_EMPTY.message, exception.getMessage());
    }

    @Test
    public void check_that_db_file_exits() {
        assertTrue(manager.categoryDbExists());
    }
}

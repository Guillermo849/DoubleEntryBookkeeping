package com.doubleentrybookkeeping.data.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;
import com.doubleentrybookkeeping.domain.models.Category;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

public class CategoryDbManagerTests {
    private final String pathToDb = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\CategoryDB.csv";

    private final CategoryDbManager categoryDbManager = new CategoryDbManager(pathToDb);

    public CategoryDbManagerTests() throws FileNotFoundException {
    }

    @Test
    public void check_that_CategoryDbManager_extends_from_DataBaseManagerTest() {
        Assert.assertEquals(pathToDb, categoryDbManager.getDbPath());
        Assert.assertTrue(categoryDbManager.categoryDbExists());
    }

    @Test
    public void add_Category_Is_OK() throws IOException {
        CategoryDbManager categoryDbManager = new CategoryDbManager(pathToDb);
        categoryDbManager.addCategory(new Category(15,15,"quince"));
    }

    @Test
    public void get_category_from_db() throws IOException {
        var category = categoryDbManager.getCategoryById(1);
        Assert.assertEquals(1, category.getCategoryId());
        Assert.assertEquals(0, category.getBaseCategoryId());
        Assert.assertEquals("CategoryTestName", category.getCategoryName());
    }

    @Test
    public void get_category_from_db_not_found() throws IOException {
        var category = categoryDbManager.getCategoryById(321);
        Assert.assertNull(category);
    }

    @Test
    public void get_category_with_invalid_base_id() {
        Exception exception = assertThrows(NumberFormatException.class, () -> categoryDbManager.getCategoryById(2));
        Assert.assertEquals(EnumErrors.DataBase.INVALID_COLUMN_VALUE_TYPE.message + "base_category", exception.getMessage());
    }

    @Test
    public void delete_category_is_ok() throws IOException {
        Assert.assertNotNull(categoryDbManager.getCategoryById(1));
        categoryDbManager.deleteCategory(1);
        Assert.assertNull(categoryDbManager.getCategoryById(1));
    }
}

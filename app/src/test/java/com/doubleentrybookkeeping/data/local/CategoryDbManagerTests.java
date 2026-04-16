package com.doubleentrybookkeeping.data.local;

import static org.junit.Assert.assertThrows;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;
import com.doubleentrybookkeeping.domain.models.Category;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class CategoryDbManagerTests {
    private final String pathToDb = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\CategoryDB.csv";

    private final CategoryDbManager categoryDbManager = new CategoryDbManager(pathToDb);

    // Categories
    private final Category BaseCategory = new Category(1, 0, "BaseCategory");
    private final Category ChildCategory = new Category(2, 1, "ChildCategory");

    public CategoryDbManagerTests() throws FileNotFoundException {
    }

    @Test
    public void check_that_CategoryDbManager_extends_from_DataBaseManagerTest() {
        Assert.assertEquals(pathToDb, categoryDbManager.getDbPath());
        Assert.assertTrue(categoryDbManager.categoryDbExists());
    }

    @Test
    public void add_Category_Is_OK() throws IOException {
        categoryDbManager.addCategory(new Category(15, 15, "quince"));
    }

    @Test
    public void get_category_from_db() throws IOException {
        // Arrange
        testStartUp();

        // Act
        var category = categoryDbManager.getCategoryById(BaseCategory.getCategoryId());

        // Assert
        Assert.assertEquals(BaseCategory.getCategoryId(), category.getCategoryId());
        Assert.assertEquals(BaseCategory.getBaseCategoryId(), category.getBaseCategoryId());
        Assert.assertEquals(BaseCategory.getCategoryName(), category.getCategoryName());

        testCleanUp();
    }

    @Test
    public void get_category_from_db_not_found() throws IOException {
        // Act
        var category = categoryDbManager.getCategoryById(321);

        // Assert
        Assert.assertNull(category);
    }

    @Test
    public void get_category_with_invalid_base_id() throws IOException {
        // Arrange
        addInvalidCategoryToDb(3, "asdf", "Invalid Category");

        // Act
        Exception exception = assertThrows(NumberFormatException.class, () -> categoryDbManager.getCategoryById(2));

        // Assert
        Assert.assertEquals(EnumErrors.DataBase.INVALID_COLUMN_VALUE_TYPE.message + "base_category", exception.getMessage());

        // Test Clean Up
        categoryDbManager.deleteCategory(3);
    }

    @Test
    public void modify_category_is_ok() throws IOException {
        // Arrange
        testStartUp();
        var newCategory = new Category(BaseCategory.getCategoryId(), BaseCategory.getCategoryName());
        newCategory.setCategoryName("NewNameForCategory");

        // Act
        categoryDbManager.modifyCategory(newCategory);

        // Assert
        var modifiedCategory = categoryDbManager.getCategoryById(BaseCategory.getCategoryId());
        Assert.assertNotEquals(BaseCategory.getCategoryName(), modifiedCategory.getCategoryName());
        Assert.assertEquals(BaseCategory.getCategoryId(), modifiedCategory.getCategoryId());

        testCleanUp();
    }

    @Test
    public void delete_category_is_ok() throws IOException {
        // Arrange
        testStartUp();
        Assert.assertNotNull(categoryDbManager.getCategoryById(BaseCategory.getCategoryId()));

        // Act
        categoryDbManager.deleteCategory(1);

        // Assert
        Assert.assertNull(categoryDbManager.getCategoryById(BaseCategory.getCategoryId()));

        testCleanUp();
    }

    @Test
    public void delete_base_category_sets_child_categories_base_category_column_to_0() throws IOException {
        // Arrange
        testStartUp();
        Assert.assertEquals(1, categoryDbManager.getCategoryById(ChildCategory.getCategoryId()).getBaseCategoryId());

        // Act
        categoryDbManager.deleteCategory(BaseCategory.getCategoryId());

        // Assert
        Assert.assertNull(categoryDbManager.getCategoryById(ChildCategory.getCategoryId()));

        testCleanUp();
    }

    private void addInvalidCategoryToDb(Object id, Object baseCategoryId, Object name) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(categoryDbManager.getDbPath(), true));
        bw.write(id + "," + name + "," + baseCategoryId);
        bw.newLine();
        bw.close();
    }

    private void testStartUp() throws IOException {
        categoryDbManager.addCategory(BaseCategory);
        categoryDbManager.addCategory(ChildCategory);
    }

    private void testCleanUp() throws IOException {
        categoryDbManager.deleteCategory(BaseCategory.getCategoryId());
        categoryDbManager.deleteCategory(ChildCategory.getCategoryId());
    }
}

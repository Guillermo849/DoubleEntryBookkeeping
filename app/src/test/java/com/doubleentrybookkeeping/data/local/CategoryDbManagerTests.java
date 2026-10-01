package com.doubleentrybookkeeping.data.local;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;
import com.doubleentrybookkeeping.domain.models.Category;

import org.junit.Assert;
import org.junit.Test;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class CategoryDbManagerTests extends TestBase {

    public CategoryDbManagerTests() throws FileNotFoundException {
    }

    @Test
    public void create_new_category_manager_is_ok() {
        try {
            // Act
            var categoryManager = new CategoryDbManager(pathToCategoryDb);

            // Assert
            Assert.assertTrue(categoryManager.getDbPath().equals(pathToCategoryDb)
                    || categoryManager.getDbPath().equals(pathToCategoryDb.replace('/', '\\')));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No exceptions should have been thrown");
        }
    }

    @Test
    public void add_Category_Is_OK() throws IOException {
        manager.getCategoryManager().addCategory(new Category(15, 15, "quince"));
    }

    @Test
    public void get_category_from_db() throws IOException {
        // Arrange
        testStartUp();

        // Act
        var fetchedCategory = manager.getCategoryManager().getCategoryById(BaseCategory.getCategoryId());

        // Assert
        Assert.assertEquals(BaseCategory.getCategoryId(), fetchedCategory.getCategoryId());
        Assert.assertEquals(BaseCategory.getBaseCategoryId(), fetchedCategory.getBaseCategoryId());
        Assert.assertEquals(BaseCategory.getCategoryName(), fetchedCategory.getCategoryName());

        testCleanUp();
    }

    @Test
    public void get_category_from_db_not_found() throws IOException {
        // Act
        var nonExistentCategory = manager.getCategoryManager().getCategoryById(321);

        // Assert
        Assert.assertNull(nonExistentCategory);
    }

    @Test
    public void get_category_with_invalid_base_id() throws IOException {
        // Arrange
        addInvalidCategoryToDb(3, "asdf", "Invalid Category");

        // Act
        Exception exception = assertThrows(NumberFormatException.class, () -> manager.getCategoryManager().getCategoryById(2));

        // Assert
        Assert.assertEquals(EnumErrors.DataBase.INVALID_COLUMN_VALUE_TYPE.message + "base_category", exception.getMessage());

        // Test Clean Up
        manager.getCategoryManager().deleteCategory(new Category(3, 0, "Invalid Category"));
    }

    @Test
    public void modify_category_is_ok() throws IOException {
        // Arrange
        testStartUp();
        var newCategoryValues = new Category(BaseCategory.getCategoryId(), "NewNameForCategory");

        // Act
        manager.getCategoryManager().modifyCategory(newCategoryValues);

        // Assert
        var modifiedCategory = manager.getCategoryManager().getCategoryById(BaseCategory.getCategoryId());

        Assert.assertNotEquals(BaseCategory.getCategoryName(), modifiedCategory.getCategoryName());
        Assert.assertEquals(BaseCategory.getCategoryId(), modifiedCategory.getCategoryId());

        testCleanUp();
    }

    @Test
    public void modify_non_existent_category_throws_exception() throws IOException {
        // Act
        Exception exception = assertThrows(NullPointerException.class,
                () -> manager.getCategoryManager().modifyCategory(new Category(999, "DoesNotExist")));

        // Assert
        Assert.assertEquals(
                String.format(EnumErrors.DataBase.DB_OBJECT_DOES_NOT_EXIST.message, "Category", "999", "Category"),
                exception.getMessage());
    }

    @Test
    public void delete_category_is_ok() throws IOException {
        // Arrange
        testStartUp();
        Assert.assertNotNull(manager.getCategoryManager().getCategoryById(BaseCategory.getCategoryId()));

        // Act
        manager.getCategoryManager().deleteCategory(BaseCategory);

        // Assert
        Assert.assertNull(manager.getCategoryManager().getCategoryById(BaseCategory.getCategoryId()));

        testCleanUp();
    }

    @Test
    public void delete_invalid_category_is_ok() throws IOException {
        // Arrange
        addInvalidCategoryToDb(3, "asdf", "Invalid Category");

        // Act
        manager.getCategoryManager().deleteCategory(new Category(3, 0, "Invalid Category"));

        // Assert
        assertNull(manager.getCategoryManager().getCategoryById(3));
    }

    @Test
    public void delete_base_category_removes_child_categories_from_database_is_ok() throws IOException {
        // Arrange
        testStartUp();
        Assert.assertEquals(1, manager.getCategoryManager().getCategoryById(ChildCategory.getCategoryId()).getBaseCategoryId());

        // Act
        manager.getCategoryManager().deleteCategory(BaseCategory);

        // Assert
        Assert.assertNull(manager.getCategoryManager().getCategoryById(ChildCategory.getCategoryId()));

        testCleanUp();
    }

    @Test
    public void delete_category_modifies_entries_category_to_0_is_ok() throws IOException {
        // Arrange
        testStartUp();
        Assert.assertNotNull(manager.getEntryManager().FilterByCategoryID(ChildCategory.getCategoryId()));

        // Act
        manager.getCategoryManager().deleteCategory(ChildCategory);

        // Assert
        var entriesFromDeletedCategory = manager.getEntryManager().FilterByCategoryID(ChildCategory.getCategoryId());
        Assert.assertEquals(0, entriesFromDeletedCategory.length);

        testCleanUp();
    }

    @Test
    public void delete_non_existent_category_is_ok() throws Exception {
        try {
            // Act
            var nonExistentCategory = manager.getCategoryManager().getCategoryById(999999);

            // Assert
            Assert.assertNull(nonExistentCategory);

        } catch (Exception e) {
            throw new Exception("No EXCEPTION should have been thrown.");
        }
    }

    private void addInvalidCategoryToDb(Object id, Object baseCategoryId, Object name) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(manager.getCategoryManager().getDbPath(), true));
        bw.write(id + "," + name + "," + baseCategoryId);
        bw.newLine();
        bw.close();
    }
}

package com.doubleentrybookkeeping.data.local;

import com.doubleentrybookkeeping.domain.models.Entry;

import org.junit.Assert;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.Date;

public class FilterDBManagerTests {

    private Entry entrada1 = new Entry(1,3,Entry.Type.REVENUE,Entry.PayMethod.CARD,10,new Date(2026,2,1),"comida","imagen");
    private final String pathToDb = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\EntriesDB.csv";
    @Test
    public void return_entry_with_given_category() throws FileNotFoundException {
        // Arrange
        var filter = new FilterDBManager(pathToDb);

        // Act
        var filteredEntries = filter.FilterByCategory(3);

        // Assert
        Assert.assertEquals(1, filteredEntries.length);
        Assert.assertEquals(entrada1.getId(), filteredEntries[0].getId());
    }

    /*
    private void testStartUp() throws IOException {
        categoryDbManager.addCategory(BaseCategory);
        categoryDbManager.addCategory(ChildCategory);
    }

    private void testCleanUp() throws IOException {
        categoryDbManager.deleteCategory(BaseCategory.getCategoryId());
        categoryDbManager.deleteCategory(ChildCategory.getCategoryId());
    }

     */
}

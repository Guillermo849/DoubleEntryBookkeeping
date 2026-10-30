package com.doubleentrybookkeeping.data.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

public class DataBaseManagerTests extends TestBase {

    public DataBaseManagerTests() throws FileNotFoundException {
    }

    @Test
    public void export_data_bases_format_is_ok() throws IOException {
        // Act
        manager.exportDataBasesTo(exportPath);

        // Assert
        String expectedFormat = "id,name,base_category\n\n" +
                "id,category,type,method,amount,date,concept,image\n";
        assertEquals(expectedFormat, manager.getDataBaseExportData());
    }

    @Test
    public void export_data_bases_to_file_is_ok() throws IOException {
        // Arrange
        testStartUp();

        // Act
        manager.exportDataBasesTo(exportPath);

        // Assert
        assertTrue(new File(exportPath).exists());

        testCleanUp();
    }

    @Test
    public void import_data_base_to_file_is_ok() throws IOException {
        // Arrange
        testStartUp();

        manager.exportDataBasesTo(exportPath);
        testCleanUp();

        // Act
        manager.importDataBaseFrom(pathToCategoryDb, pathToEntryDb, exportPath);

        // Assert
        assertEquals(2, manager.getEntryManager().FilterByDate(entryWithBaseCategory.getDate()).length);
        assertEquals(2, manager.getCategoryManager().getCategories().length);
        assertTrue(new File(pathToCategoryDb).exists() && new File(pathToEntryDb).exists());

        testCleanUp();
    }

    @Test
    public void getting_this_months_expenses_is_ok() throws IOException, ParseException {
        // Arrange
        Calendar cal = Calendar.getInstance();
        String the12OfThisMonthAndYear = String.format("12/%s/%s", cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR));
        entryWithBaseCategory.setDate(the12OfThisMonthAndYear);
        entryWithChildCategory.setDate(the12OfThisMonthAndYear);

        testStartUp();

        // Act
        var entriesCategories = manager.getMonthYearExpenses();

        // Assert
        assertEquals(2, entriesCategories.getEntries().size());
        assertEquals(2, entriesCategories.getCategories().size());

        testCleanUp();
    }

    @Test
    public void getting_this_months_expenses_when_non_exist_is_ok() throws IOException, ParseException {
        // Act
        var entriesCategories = manager.getMonthYearExpenses();

        // Assert
        assertEquals(0, entriesCategories.getEntries().size());
        assertEquals(0, entriesCategories.getCategories().size());
    }

    @Test
    public void get_last_months_expenses_is_ok() throws IOException, ParseException {
        // Arrange
        Calendar cal = Calendar.getInstance();
        String the12OfThisMonthAndYear = String.format("12/%s/%s", cal.get(Calendar.MONTH)-1, cal.get(Calendar.YEAR));
        entryWithBaseCategory.setDate(the12OfThisMonthAndYear);

        testStartUp();

        // Act
        var entriesCategories = manager.getMonthYearExpenses(cal.get(Calendar.MONTH)-1, cal.get(Calendar.YEAR));

        // Assert
        assertEquals(1, entriesCategories.getEntries().size());
        assertEquals(1, entriesCategories.getCategories().size());

        testCleanUp();
    }

    @Test
    public void get_the_expenses_of_may_of_this_year() throws IOException, ParseException {
        // Arrange
        Calendar cal = Calendar.getInstance();
        String the12OfThisMonthAndYear = String.format("12/05/%s", cal.get(Calendar.YEAR));
        entryWithBaseCategory.setDate(the12OfThisMonthAndYear);

        testStartUp();

        // Act
        var entriesCategories = manager.getMonthYearExpenses(5);

        // Assert
        assertEquals(1, entriesCategories.getEntries().size());
        assertEquals(1, entriesCategories.getCategories().size());

        testCleanUp();
    }

    @Test
    public void get_expenses_from_the_year_2024() throws IOException, ParseException {
        // Arrange
        entryWithBaseCategory.setDate("12/03/2024");
        entryWithChildCategory.setDate("21/10/2024");

        testStartUp();

        // Act
        var entriesCategories = manager.getMonthYearExpenses(2024);

        // Assert
        assertEquals(2, entriesCategories.getEntries().size());
        assertEquals(2, entriesCategories.getCategories().size());

        testCleanUp();
    }
}

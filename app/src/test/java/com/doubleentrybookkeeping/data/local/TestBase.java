package com.doubleentrybookkeeping.data.local;

import com.doubleentrybookkeeping.domain.models.Category;
import com.doubleentrybookkeeping.domain.models.Entry;

import org.junit.Before;
import org.junit.BeforeClass;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class TestBase {

    //region Categories
    protected static final String pathToCategoryDb = "./src/test/java/com/doubleentrybookkeeping/TestResources/CategoryDB.csv";

    protected final Category BaseCategory = new Category(1, 0, "BaseCategory");
    protected final Category ChildCategory = new Category(2, 1, "ChildCategory");
    //endregion

    //region Entries
    protected static final String pathToEntryDb = "./src/test/java/com/doubleentrybookkeeping/TestResources/EntriesDB.csv";

    protected Entry entryWithBaseCategory = new Entry(1, BaseCategory.getCategoryId(), Entry.Type.REVENUE, Entry.PayMethod.CASH, 15.0f, "14/06/2026", "Sabrosura", "imagen");
    protected Entry entryWithChildCategory = new Entry(2, ChildCategory.getCategoryId(), Entry.Type.EXPENSES, Entry.PayMethod.CARD, 20.0f, "14/06/2026", "Sabrosura", "imagen");
    //endregion

    protected final DataBaseManager manager = new DataBaseManager(pathToCategoryDb, pathToEntryDb);

    public TestBase() throws FileNotFoundException {
    }

    @BeforeClass
    public static void testInit() throws IOException {
        File categoryDbFile = new File(pathToCategoryDb);
        createTestCSVFiles(categoryDbFile, "id,name,base_category\n");

        File entryDbFile = new File(pathToEntryDb);
        createTestCSVFiles(entryDbFile, "id,category,type,method,amount,date,concept,image\n");
    }

    private static void createTestCSVFiles(File testFile, String firstLine) throws IOException {
        if (testFile.createNewFile()) {
            try (FileWriter writer = new FileWriter(testFile)) {
                writer.write(firstLine);
            } catch (IOException e) {
                throw new IOException(e.getMessage());
            }
        }
    }

    protected void testStartUp() throws IOException {
        manager.getCategoryManager().addCategory(BaseCategory);
        manager.getCategoryManager().addCategory(ChildCategory);

        manager.getEntryManager().addDbObject(entryWithBaseCategory);
        manager.getEntryManager().addDbObject(entryWithChildCategory);
    }

    protected void testCleanUp() throws IOException {
        manager.getCategoryManager().deleteCategory(BaseCategory);
        manager.getCategoryManager().deleteCategory(ChildCategory);

        manager.getEntryManager().deleteDbObject(entryWithBaseCategory);
        manager.getEntryManager().deleteDbObject(entryWithChildCategory);
    }
}

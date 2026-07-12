package com.doubleentrybookkeeping.data.local;

import com.doubleentrybookkeeping.domain.models.Category;
import com.doubleentrybookkeeping.domain.models.Entry;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

public class TestBase {

    //region Categories
    protected final String pathToCategoryDb = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\CategoryDB.csv";

    protected final Category BaseCategory = new Category(1, 0, "BaseCategory");
    protected final Category ChildCategory = new Category(2, 1, "ChildCategory");
    //endregion

    //region Entries
    protected final String pathToEntryDb = ".\\src\\test\\java\\com\\doubleentrybookkeeping\\TestResources\\EntriesDB.csv";

    protected Entry entryWithBaseCategory = new Entry(1, BaseCategory.getCategoryId(), Entry.Type.REVENUE, Entry.PayMethod.CASH, 15.0f, "14/06/2026", "Sabrosura", "imagen");
    protected Entry entryWithChildCategory = new Entry(2, ChildCategory.getCategoryId(), Entry.Type.EXPENSES, Entry.PayMethod.CARD, 20.0f, "14/06/2026", "Sabrosura", "imagen");
    //endregion

    protected final DataBaseManager manager = new DataBaseManager(pathToCategoryDb, pathToEntryDb);

    public TestBase() throws FileNotFoundException {
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

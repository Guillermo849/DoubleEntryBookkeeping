package com.doubleentrybookkeeping.interfaces.data.local;

import com.doubleentrybookkeeping.domain.models.Category;
import com.doubleentrybookkeeping.interfaces.ICategoryDeletedListener;

import java.io.IOException;

public interface ICategoryDbManager {
    String getDbPath();

    void addListener(ICategoryDeletedListener toAdd);

    void addCategory(Category category) throws IOException;

    void modifyCategory(Category category) throws IOException;

    void deleteCategory(Category category) throws IOException;

    Category getCategoryById(int id) throws IOException;

    Category getCategoryByName(String name) throws IOException;

    Category[] getCategories() throws IOException;
}

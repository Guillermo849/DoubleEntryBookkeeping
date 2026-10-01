package com.doubleentrybookkeeping.data.local;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;
import com.doubleentrybookkeeping.domain.models.Category;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

interface CategoryDeletedListener {
    void deleted_category(int deletedCategory);
}

public class CategoryDbManager {

    private final List<CategoryDeletedListener> listeners = new ArrayList<>();

    private final File dbFile;
    public CategoryDbManager(String pathToDb) throws FileNotFoundException {
        checkDataBasePathIsNotEmpty(pathToDb);

        dbFile = new File(pathToDb);
        checkDataBaseFileExists();
    }

    public void addListener(CategoryDeletedListener toAdd) {
        listeners.add(toAdd);
    }

    private void checkDataBasePathIsNotEmpty(String pathToDb){
        if (pathToDb.isEmpty())
            throw new NullPointerException(EnumErrors.DataBase.PATH_EMPTY.message);
    }

    private void checkDataBaseFileExists() throws FileNotFoundException {
        if (!categoryDbExists())
            throw new FileNotFoundException(EnumErrors.DataBase.FILE_NOT_EXISTS.message);
    }

    private boolean categoryDbExists() {
        return dbFile.exists();
    }

    public String getDbPath() {
        return dbFile.getPath();
    }

    public void addCategory(Category category) throws IOException {
        addCategoryValidation(category);
        BufferedWriter bw = new BufferedWriter(new FileWriter(getDbPath(), true));
        bw.write(toCsvFormat(category));
        bw.newLine();
        bw.close();
    }

    private void addCategoryValidation(Category category) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(getDbPath()));
        String line = br.readLine();

        while(line != null){
            if(line.equals(category)){
                throw new NullPointerException(EnumErrors.Models.CATEGORY_CREATION.message);
            }
            line = br.readLine();
        }
        br.close();
    }

    private String toCsvFormat(Category category) {
        return category.getCategoryId() + "," + category.getCategoryName() + "," + category.getBaseCategoryId();
    }

    public void modifyCategory(Category category) throws IOException {
        DataBaseActionToProcess<Category, String[], Boolean, Set<String>> modify = (categoryGiven,
                                                                                    columnValuesToMatch,
                                                                                    categoryHasBeenModified,
                                                                                    newInformationToInsertInDatabase) ->
        {
            int dbCategoryId = Integer.parseInt(columnValuesToMatch[0]);

            if (dbCategoryId == categoryGiven.getCategoryId()) {
                categoryHasBeenModified = true;
                newInformationToInsertInDatabase.add(toCsvFormat(categoryGiven));
            }
            else
                newInformationToInsertInDatabase.add(columnValuesToMatch[0] + "," + columnValuesToMatch[1] + "," + columnValuesToMatch[2]);

            return categoryHasBeenModified;
        };

        if (!dataBaseReaderTo(modify, category))
            throw new NullPointerException(
                    String.format(EnumErrors.DataBase.DB_OBJECT_DOES_NOT_EXIST.message, "Category", category.getCategoryId()+"", "Category"));
    }

    public void deleteCategory(Category category) throws IOException {
        Set<Category> deletedCategories = new LinkedHashSet<>();

        DataBaseActionToProcess<Category, String[], Boolean, Set<String>> delete = (categoryToDelete,
                                                                                    columnValuesToMatch,
                                                                                    categoryHasBeenDeleted,
                                                                                    newInformationToInsertInDatabase) ->
        {
            try{
                int dbCategoryId = Integer.parseInt(columnValuesToMatch[0]);
                int dbBaseCategoryId = Integer.parseInt(columnValuesToMatch[2]);

                if (dbCategoryId != category.getCategoryId() && dbBaseCategoryId != category.getCategoryId())
                    newInformationToInsertInDatabase.add(columnValuesToMatch[0] + "," + columnValuesToMatch[1] + "," + columnValuesToMatch[2]);
                else {
                    categoryHasBeenDeleted = true;
                    deletedCategories.add(categoryToDelete);
                }

            } catch (NumberFormatException invalidData) {
                categoryHasBeenDeleted = true;
            }
            return categoryHasBeenDeleted;
        };

        dataBaseReaderTo(delete, category);

        if (!deletedCategories.isEmpty())
            for (var listener : listeners)
                for (var deletedCategory : deletedCategories)
                    listener.deleted_category(deletedCategory.getCategoryId());
    }

    private boolean dataBaseReaderTo(DataBaseActionToProcess<Category, String[], Boolean, Set<String>> action, Category category) throws IOException {
        Set<String> newDbInformation = new LinkedHashSet<>();

        boolean modified;
        try (BufferedReader br = new BufferedReader(new FileReader(getDbPath()))) {
            modified = false;

            String line;
            newDbInformation.add(br.readLine());
            while ((line = br.readLine()) != null) {
                String[] columnValues = line.split(",");

                modified = action.run(category, columnValues, modified, newDbInformation);
            }
            br.close();

            if (modified)
                writeToCSV(newDbInformation);
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }

        return modified;
    }

    private void writeToCSV(Set<String> newDbInformation) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(getDbPath()));

        for (var newCategoryLine : newDbInformation) {
            bw.write(newCategoryLine);
            bw.write("\n");
        }
        bw.close();
    }

    public Category getCategoryById(int id) throws IOException {
        try (BufferedReader br = new BufferedReader(new FileReader(getDbPath()))) {
            String line;
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnValues = line.split(",");
                Category lineCategory = getCategoryFromLine(columnValues);

                if (lineCategory.getCategoryId() == id)
                    return lineCategory;
            }
        } catch (NumberFormatException ex) {
            throw new NumberFormatException(EnumErrors.DataBase.INVALID_COLUMN_VALUE_TYPE.message + "base_category");
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
        return null;
    }

    private static Category getCategoryFromLine(String[] columnValues) {
        return new Category(
                Integer.parseInt(columnValues[0]),
                Integer.parseInt(columnValues[2]),
                columnValues[1]);
    }

    @FunctionalInterface
    interface DataBaseActionToProcess<O, S, B, H> {
        boolean run(O o, S s, B b, H h);
    }
}

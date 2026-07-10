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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class CategoryDbManager {

    private final File dbFile;
    public CategoryDbManager(String pathToDb) throws FileNotFoundException {
        checkDataBasePathIsNotEmpty(pathToDb);

        dbFile = new File(pathToDb);
        checkDataBaseFileExists();
    }

    private void checkDataBasePathIsNotEmpty(String pathToDb){
        if (pathToDb.isEmpty())
            throw new NullPointerException(EnumErrors.DataBase.PATH_EMPTY.message);
    }

    private void checkDataBaseFileExists() throws FileNotFoundException {
        if (!categoryDbExists())
            throw new FileNotFoundException(EnumErrors.DataBase.FILE_NOT_EXISTS.message);
    }

    public boolean categoryDbExists() {
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
        dataBaseActionToProcess<Category, String[], Boolean, HashSet<String>> modify = (categoryGiven,
                                                                                        columnValuesToMatch,
                                                                                        categoryHasBeenModified,
                                                                                        newInformationToInsertInDatabase) -> {
            int dbCategoryId = Integer.parseInt(columnValuesToMatch[0]);

            if (dbCategoryId == categoryGiven.getCategoryId()) {
                categoryHasBeenModified = true;
                newInformationToInsertInDatabase.add(toCsvFormat(categoryGiven));
            }
            return categoryHasBeenModified;
        };

        dataBaseReaderTo(modify, category);
    }

    public void deleteCategory(Category category) throws IOException {
        dataBaseActionToProcess<Category, String[], Boolean, HashSet<String>> delete = (categoryToDelete,
                                                                                        columnValuesToMatch,
                                                                                        categoryHasBeenDeleted,
                                                                                        newInformationToInsertInDatabase) -> {
            try{
                int dbCategoryId = Integer.parseInt(columnValuesToMatch[0]);
                int dbBaseCategoryId = Integer.parseInt(columnValuesToMatch[2]);

                if (dbCategoryId != category.getCategoryId() && dbBaseCategoryId != category.getCategoryId())
                    newInformationToInsertInDatabase.add(columnValuesToMatch[0] + "," + columnValuesToMatch[1] + "," + columnValuesToMatch[2]);
                else
                    categoryHasBeenDeleted = true;

            } catch (NumberFormatException ignored) {}
            return categoryHasBeenDeleted;
        };

        dataBaseReaderTo(delete, category);
    }

    private void dataBaseReaderTo(dataBaseActionToProcess<Category, String[], Boolean, HashSet<String>> action, Category category) throws IOException {
        HashSet<String> newDbInformation = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(getDbPath()))) {
            boolean modified = false;

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
    }

    private void writeToCSV(HashSet<String> newDbInformation) throws IOException {
        List<String> informationIterator = new ArrayList<>(newDbInformation);
        Collections.reverse(informationIterator);
        BufferedWriter bw = new BufferedWriter(new FileWriter(getDbPath()));

        for (var newCategoryLine : informationIterator) {
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
    interface dataBaseActionToProcess<O, S, B, H> {
        boolean run(O o, S s, B b, H h);
    }
}

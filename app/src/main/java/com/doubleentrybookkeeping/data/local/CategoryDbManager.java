package com.doubleentrybookkeeping.data.local;

import androidx.annotation.NonNull;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;
import com.doubleentrybookkeeping.domain.models.Category;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class CategoryDbManager extends DataBaseManager {

    public CategoryDbManager(String pathToDb) throws FileNotFoundException {
        super(pathToDb);
    }


    public void addCategory(Category category) throws IOException {
        addCategoryValidation(category);
        BufferedWriter bw = new BufferedWriter(new FileWriter(getDbPath(), true));
        bw.write(toCsvFormat(category));
        bw.newLine();
        bw.close();
    }


    public void addCategoryValidation(Category category) throws IOException {
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
        HashSet<String> newDbInformation = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(getDbPath()))) {
            boolean modified = false;

            String line;
            newDbInformation.add(br.readLine());
            while ((line = br.readLine()) != null) {
                String[] columnValues = line.split(",");

                int dbCategoryId = Integer.parseInt(columnValues[0]);
                if (dbCategoryId == category.getCategoryId()) {
                    modified = true;
                    newDbInformation.add(toCsvFormat(category));
                }
            }
            br.close();

            if (modified)
                writeToCSV(newDbInformation);
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    public void deleteCategory(int categoryId) throws IOException {
        HashSet<String> newDbInformation = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(getDbPath()))) {
            String line;
            newDbInformation.add(br.readLine());

            while ((line = br.readLine()) != null) {
                String[] columnValues = line.split(",");

                try{
                    int dbCategoryId = Integer.parseInt(columnValues[0]);
                    int dbBaseCategoryId = Integer.parseInt(columnValues[2]);

                    if (dbCategoryId != categoryId && dbBaseCategoryId != categoryId)
                        newDbInformation.add(line);

                    if (dbBaseCategoryId == categoryId) {
                        var childCategory = getCategoryFromLine(columnValues);
                        childCategory.setBaseCategoryId(0);
                        newDbInformation.add(toCsvFormat(childCategory));
                    }
                } catch (NumberFormatException ignored) {}
            }
            br.close();

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
}

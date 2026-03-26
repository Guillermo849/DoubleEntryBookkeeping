package com.doubleentrybookkeeping.data.local;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;
import com.doubleentrybookkeeping.domain.models.Category;
import com.doubleentrybookkeeping.domain.models.DbObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import kotlin.NotImplementedError;

public class CategoryDbManager extends DataBaseManager {

    public CategoryDbManager(String pathToDb) throws FileNotFoundException {
        super(pathToDb);
    }


    public void addCategory(Category category) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(getDbPath(), true));
        bw.write(to_csv_format(category));
        bw.newLine();
        bw.close();
    }

    private String to_csv_format(Category category) {
        return category.getCategoryId() + "," + category.getCategoryName() + "," + category.getBaseCategoryId();
    }

    @Override
    public DbObject modifyDbObject(DbObject dbObject) {
        throw new NotImplementedError();
    }

    public void deleteCategory(int categoryId) throws IOException {
        HashSet<String> newDbInformation = new HashSet<>();

        try (BufferedReader br = new BufferedReader(new FileReader(getDbPath()))) {
            String line;
            newDbInformation.add(br.readLine());
            while ((line = br.readLine()) != null) {
                String[] columnValues = line.split(",");

                int dbCategoryId = Integer.parseInt(columnValues[0]);
                if (dbCategoryId != categoryId)
                    newDbInformation.add(line);
            }
            br.close();

            writeToCSV(newDbInformation);
        } catch (IOException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    private void writeToCSV(HashSet<String> newDbInformation) throws IOException {
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
                Category lineCategory = new Category(
                        Integer.parseInt(columnValues[0]),
                        Integer.parseInt(columnValues[2]),
                        columnValues[1]);
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
}

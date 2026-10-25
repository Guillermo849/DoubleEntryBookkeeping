package com.doubleentrybookkeeping.data.local;


import androidx.annotation.NonNull;

import com.doubleentrybookkeeping.domain.models.Category;
import com.doubleentrybookkeeping.domain.models.Entry;
import com.doubleentrybookkeeping.interfaces.data.local.ICategoryDbManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class DataBaseManager {
    private final ICategoryDbManager categoryDbManager;

    private final EntryDBManager entryDBManager;

    public DataBaseManager(String pathToCategoryDb, String pathToEntryDb) throws FileNotFoundException {
        categoryDbManager = new CategoryDbManager(pathToCategoryDb);
        entryDBManager = new EntryDBManager(pathToEntryDb);

        categoryDbManager.addListener(entryDBManager);
    }

    public ICategoryDbManager getCategoryManager() {
        return categoryDbManager;
    }

    public EntryDBManager getEntryManager() {
        return entryDBManager;
    }

    public void exportDataBasesTo(String pathToExportFile) throws IOException {
        File exportFile = new File(pathToExportFile);
        if (!exportFile.exists()){
            exportFile.createNewFile();
        }

        BufferedWriter bw = new BufferedWriter(new FileWriter(pathToExportFile, false));
        bw.write(getDataBaseExportData());
        bw.newLine();
        bw.close();
    }

    public String getDataBaseExportData() {
        StringBuilder exportDbDataBuilder = new StringBuilder();

        getDataFromDb(exportDbDataBuilder, categoryDbManager.getDbPath());
        exportDbDataBuilder.append("\n");
        getDataFromDb(exportDbDataBuilder, entryDBManager.getDbPath());

        return exportDbDataBuilder.toString();
    }

    private void getDataFromDb(StringBuilder exportDbDataBuilder, String dbpath) {
        try (BufferedReader br = new BufferedReader(new FileReader(dbpath))) {
            String line;

            while ((line = br.readLine()) != null)
                exportDbDataBuilder.append(line).append("\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void importDataBaseFrom(String pathOfCategotyFile, String pathOfEntriesFile, String pathOfImportFile) throws IOException {
        File importedCategory = new File(pathOfCategotyFile);
        File importedEntries = new File(pathOfEntriesFile);
        importedCategory.createNewFile();
        importedEntries.createNewFile();

        BufferedWriter categorybw = new BufferedWriter(new FileWriter(pathOfCategotyFile, false));
        BufferedWriter entriesbw = new BufferedWriter(new FileWriter(pathOfEntriesFile, false));
        String[] importedData = getDataBaseImportData(pathOfImportFile);
        categorybw.write(importedData[0]);
        categorybw.newLine();
        categorybw.close();
        entriesbw.write(importedData[1]);
        entriesbw.newLine();
        entriesbw.close();
    }

    private String[] getDataBaseImportData(String pathOfImportFile) {
        StringBuilder[] exportDbDataBuilder = new StringBuilder[2];
        exportDbDataBuilder[0] = new StringBuilder();
        exportDbDataBuilder[1] = new StringBuilder();

        getDataFromImportedDb(exportDbDataBuilder, pathOfImportFile);

        return new String[]{exportDbDataBuilder[0].toString(), exportDbDataBuilder[1].toString()};
    }

    private void getDataFromImportedDb(StringBuilder[] exportDbDataBuilder, String dbpath) {
        try (BufferedReader br = new BufferedReader(new FileReader(dbpath))) {
            String line;

            boolean writingCategoryDb = true;

            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    writingCategoryDb = false;
                    continue;
                }
                if (writingCategoryDb) {
                    exportDbDataBuilder[0].append(line).append("\n");
                } else {
                    exportDbDataBuilder[1].append(line).append("\n");
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error al importar base de datos", e);
        }
    }

    // Todo: Remove throws ParseException
    public EntriesCategories getMonthYearExpenses() throws ParseException, IOException {
        Calendar cal = Calendar.getInstance();
        String startOfTheMonth = String.format("1/0%s/%s", cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR));
        String endOfMonth = String.format("31/0%s/%s", cal.get(Calendar.MONTH)+1, cal.get(Calendar.YEAR));

        return getEntriesCategories(startOfTheMonth, endOfMonth);
    }

    public EntriesCategories getMonthYearExpenses(int month, int year) throws ParseException, IOException {
        String startOfTheMonth = String.format("1/%s/%s", month, year);
        String endOfMonth = String.format("31/%s/%s", month, year);

        return getEntriesCategories(startOfTheMonth, endOfMonth);
    }

    public EntriesCategories getMonthYearExpenses(int monthOrYear) throws ParseException, IOException {
        if (String.valueOf(monthOrYear).length() <= 2)
            return getMonthlyExpenses(monthOrYear);

        return getYearlyExpenses(monthOrYear);

    }

    private EntriesCategories getMonthlyExpenses(int month) throws ParseException, IOException {
        Calendar cal = Calendar.getInstance();
        String startOfTheMonth = String.format("1/%s/%s", month, cal.get(Calendar.YEAR));
        String endOfMonth = String.format("31/%s/%s", month, cal.get(Calendar.YEAR));

        return getEntriesCategories(startOfTheMonth, endOfMonth);
    }

    private EntriesCategories getYearlyExpenses(int year) throws ParseException, IOException {
        ArrayList<Entry> entries = new ArrayList<>();
        ArrayList<Category>  categories= new ArrayList<>();

        for (int month = 1; month <= 12; month++) {
            String startOfTheMonth = String.format("1/%s/%s", month, year);
            String endOfMonth = String.format("31/%s/%s", month, year);

            var monthsExpenses = getEntriesCategories(startOfTheMonth, endOfMonth);
            entries.addAll(monthsExpenses.getEntries());
            categories.addAll(monthsExpenses.getCategories());
        }

        return new EntriesCategories(entries, categories);
    }

    private EntriesCategories getEntriesCategories(String startOfTheMonth, String endOfMonth) throws ParseException, IOException {
        ArrayList<Entry> entriesInRange = new ArrayList<>(Arrays.asList(getEntryManager().FilterByDateInRange(startOfTheMonth, endOfMonth)));

        ArrayList<Category> categories = new ArrayList<>();

        for (var entry : entriesInRange) {
            var category = getCategoryManager().getCategoryById(entry.getCategory());
            if (!categories.contains(category))
                categories.add(category);
        }

        return new EntriesCategories(entriesInRange, categories);
    }

    public static class EntriesCategories {
        private final ArrayList<Entry> entries;
        private final ArrayList<Category> categories;

        public EntriesCategories(ArrayList<Entry> entries, ArrayList<Category> categories) {
            this.entries = entries;
            this.categories = categories;
        }

        public ArrayList<Entry> getEntries() {
            return entries;
        }

        public ArrayList<Category> getCategories() {
            return categories;
        }
    }
}

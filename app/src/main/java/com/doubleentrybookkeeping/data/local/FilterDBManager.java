package com.doubleentrybookkeeping.data.local;

import com.doubleentrybookkeeping.domain.models.DbObject;
import com.doubleentrybookkeeping.domain.models.Entry;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kotlin.NotImplementedError;

public class FilterDBManager extends DataBaseManager{

    public FilterDBManager(String pathToDb) throws FileNotFoundException {
        super(pathToDb);
    }

    public Entry[] FilterByCategory(int categoryID) {
        List<Entry> foundEntries = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(getDbPath()))) {
                String line;
                br.readLine();

                while((line = br.readLine()) != null) {
                    String[] values = line.split(",");

                    Entry posibleEntry = toEntry(values);

                    if (posibleEntry.getCategory() == categoryID) {
                        foundEntries.add(posibleEntry);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        return foundEntries.toArray(new Entry[0]);
    }

    public Entry[] FilterByDate(Date date) {

        return null;
    }

    public Entry[] FilterByType(Type type) {

        return null;
    }

    private Entry toEntry(String[] values) {
        return new Entry(
                Integer.parseInt(values[0]),
                Integer.parseInt(values[2]),
                Integer.parseInt(values[3]),
                values[4],
                Float.parseFloat(values[5]),
                new Date(Long.parseLong(values[6])),
                values[7],
                values[8]
        );
    }

    public enum Type {
        EXPENSES,
        REVENUE
    }

}

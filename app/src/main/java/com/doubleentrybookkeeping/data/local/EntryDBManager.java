package com.doubleentrybookkeeping.data.local;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;
import com.doubleentrybookkeeping.domain.models.DbObject;
import com.doubleentrybookkeeping.domain.models.Entry;
import com.doubleentrybookkeeping.interfaces.ICategoryDeletedListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

public class EntryDBManager implements ICategoryDeletedListener {

    private final File dbFile;

    public EntryDBManager(String pathToDb) throws FileNotFoundException {
        checkDataBasePathIsEmpty(pathToDb);

        dbFile = new File(pathToDb);
        checkDataBaseFileExists();
    }

    public void checkDataBasePathIsEmpty(String pathToDb) {
        if (pathToDb.isEmpty())
            throw new NullPointerException(EnumErrors.DataBase.PATH_EMPTY.message);
    }

    public void checkDataBaseFileExists() throws FileNotFoundException {
        if (!entryDbExists())
            throw new FileNotFoundException(EnumErrors.DataBase.FILE_NOT_EXISTS.message);
    }

    public boolean entryDbExists() {
        return dbFile.exists();
    }

    public String getDbPath() {
        return dbFile.getPath();
    }

    private String entryToCsvInfo(Entry entry) {
        return entry.getId() + "," +
                entry.getCategory() + "," +
                entry.getType() + "," +
                entry.getPayMethod() + "," +
                entry.getAmount() + "," +
                entry.getDate() + "," +
                entry.getConcept() + ",";
    }

    public DbObject addDbObject(DbObject dbObject) {
        Entry entry = (Entry) dbObject;
        try (FileWriter fW = new FileWriter(getDbPath(), true);
             BufferedWriter bW = new BufferedWriter(fW);
             PrintWriter out = new PrintWriter(bW)) {
            out.println(entryToCsvInfo(entry));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return entry;
    }

    public DbObject modifyDbObject(DbObject dbObject) {
        Entry entry = (Entry) dbObject;
        List<String> lines = new ArrayList<>();
        File dbFile = new File(getDbPath());

        try (BufferedReader bR = new BufferedReader(new FileReader(dbFile))) {
            String line;
            while ((line = bR.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 0 && values[0].equals(String.valueOf(entry.getId()))) {
                    lines.add(entryToCsvInfo(entry));
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter pW = new PrintWriter(new FileWriter(dbFile))) {
            for (String line : lines) {
                pW.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entry;
    }

    public void deleteDbObject(DbObject dbObject) {
        Entry entry = (Entry) dbObject;
        List<String> lines = new ArrayList<>();
        File dbFile = new File(getDbPath());

        try (BufferedReader bR = new BufferedReader(new FileReader(dbFile))) {
            String line;
            while ((line = bR.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length > 0 && values[0].equals(String.valueOf(entry.getId()))) {
                    continue;
                }
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (PrintWriter pW = new PrintWriter(new FileWriter(dbFile))) {
            for (String line : lines) {
                pW.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Entry[] FilterByID(int ID) {
        return getEntriesByFiltrating(entry -> entry.getId() == ID);
    }

    public Entry[] FilterByCategoryID(int categoryID) {
        return getEntriesByFiltrating(entry -> entry.getCategory() == categoryID);
    }

    public Entry[] FilterByType(Entry.Type type) {
        return getEntriesByFiltrating(entry -> entry.getType() == type);
    }

    public Entry[] FilterByPayMethod(Entry.PayMethod payMethod) {
        return getEntriesByFiltrating(entry -> entry.getPayMethod() == payMethod);
    }

    public Entry[] FilterByDate(String date) {
        return getEntriesByFiltrating(entry -> entry.getDate().equals(date));
    }

    public Entry[] FilterByDateInRange(String startDate, String endDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date start = sdf.parse(startDate);
        Date end = sdf.parse(endDate);

        return getEntriesByFiltrating(entry -> {
            Date entryDate = null;
            try {
                entryDate = sdf.parse(entry.getDate());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            return entryDate.after(start) && entryDate.before(end);
        });
    }

    public Entry[] FilterByAmount(float amount) {
        return getEntriesByFiltrating(entry -> entry.getAmount() == amount);
    }

    public Entry[] FilterByAmountInRange(float maxAmount, float minAmount) {
        return getEntriesByFiltrating(entry -> entry.getAmount() <= maxAmount && entry.getAmount() >= minAmount);
    }

    public Entry[] getEntriesByFiltrating(Predicate<Entry> condition) {
        List<Entry> foundEntries = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(getDbPath()))) {
            String line;
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");

                Entry posibleEntry = toEntry(values);

                if (condition.test(posibleEntry)) {
                    foundEntries.add(posibleEntry);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return foundEntries.toArray(new Entry[0]);
    }

    // Todo: REMOVE Temporary fix to missing Entry values
    private Entry toEntry(String[] values) {
        return new Entry(
                Integer.parseInt(values[0]),
                Integer.parseInt(values[1]),
                Entry.Type.valueOf(values[2]),
                Entry.PayMethod.valueOf(values[3]),
                Float.parseFloat(values[4]),
                // Change start
                values.length >= 6 ? values[5] : null,
                values.length >= 7 ? values[6] : null,
                values.length >= 8 ? values[7] : null
                // Change end
        );
    }

    @Override
    public void deleted_category(int deletedCategory) {
        var entriesToModify = FilterByCategoryID(deletedCategory);
        for (var entry : entriesToModify) {
            entry.setCategory(0);
            modifyDbObject(entry);
        }
    }
}
package com.doubleentrybookkeeping.data.local;

import com.doubleentrybookkeeping.domain.models.DbObject;
import com.doubleentrybookkeeping.domain.models.Entry;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class EntryDBManager extends DataBaseManager {

    public EntryDBManager(String pathToDb) throws FileNotFoundException {
        super(pathToDb);
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

    @Override
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

    @Override
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

    @Override
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
}
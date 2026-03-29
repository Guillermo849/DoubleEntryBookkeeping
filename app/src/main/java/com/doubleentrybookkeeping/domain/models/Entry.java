package com.doubleentrybookkeeping.domain.models;

import java.util.Date;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;

public class Entry extends DbObject {
    private final int id;
    private Category category;
    private String type;
    private String method;
    private double amount;
    private Date date;
    private String concept;

    public Entry(int id, Category category, String type, String method, double amount, Date date, String concept) {
        this.id = id;
        this.category = category;
        this.type = type;
        this.method = method;
        this.amount = amount;
        this.date = date;
        this.concept = concept;
    }

    public int getEntryId() {
        return id;
    }

    public Category getEntryCategory() {
        return category;
    }

    public void setEntryCategory(Category category) {
        this.category = category;
    }

    public String getEntryType() {
        return type;
    }

    public void setEntryType(String type) {
        this.type = type;
    }

    public String getEntryMethod() {
        return method;
    }

    public void setEntryMethod(String method) {
        this.method = method;
    }

    public double getEntryAmount() {
        return amount;
    }

    public void setEntryAmount(double amount) {
        this.amount = amount;
    }

    public Date getEntryDate() {
        return date;
    }

    public void setEntryDate(Date date) {
        this.date = date;
    }

    public String getEntryConcept() {
        return concept;
    }

    public void setEntryConcept(String concept) {
        this.concept = concept;
    }

    private void ParametersAreValid(int id, Category category, String type, String method, double amount, Date date,
                                    String concept) {
        if (id <= 0 || category == null || type.isEmpty() || method.isEmpty() || amount <= 0 || date == null
                || concept.isEmpty())
            throw new NullPointerException(EnumErrors.Models.ENTRY_CREATION.message);
    }
}

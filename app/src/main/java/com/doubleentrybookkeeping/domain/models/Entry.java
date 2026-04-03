package com.doubleentrybookkeeping.domain.models;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;

import java.util.Date;

public class Entry extends  DbObject{

    private final int id;
    private int category;
    private Type type;
    private PayMethod payMethod;
    private float amount;
    private Date date;
    private String concept;
    private String image;

    public Entry(int id, int category, Type type, PayMethod method, float amount) {
        this.id = id;
        this.category = category;
        this.type = type;
        this.payMethod = method;
        this.amount = amount;
    }

    public Entry(int id, int category, Type type, PayMethod method, float amount, Date date, String concept, String image) {
        this.id = id;
        this.category = category;
        this.type = type;
        this.payMethod = method;
        this.amount = amount;
        this.date = date;
        this.concept = concept;
        this.image = image;
    }

    private void ParametersAreValid(int id) {
        if (id <= 0)
            throw new NullPointerException(EnumErrors.Models.CATEGORY_CREATION.message);
    }

    public int getId() {
        return id;
    }

    public int getCategory() {
        return category;
    }

    public Type getType() {
        return type;
    }

    public PayMethod getPayMethod() {
        return payMethod;
    }

    public float getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getConcept() {
        return concept;
    }

    public String getImage() {
        return image;
    }

    public enum Type {
        EXPENSES,
        REVENUE
    }

    public enum PayMethod {
        CARD,
        CASH
    }
}


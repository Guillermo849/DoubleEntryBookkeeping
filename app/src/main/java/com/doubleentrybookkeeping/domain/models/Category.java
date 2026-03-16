package com.doubleentrybookkeeping.domain.models;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;

public class Category {
    private final int id;
    private int baseCategoryId;
    private String name;

    public Category(int id, String name) {
        ParametersAreValid(id, name);
        this.id = id;
        this.name = name;
    }

    public Category(int id, int baseCategoryId, String name) {
        ParametersAreValid(id, name);
        this.id = id;
        this.baseCategoryId = baseCategoryId;
        this.name = name;
    }

    private void ParametersAreValid(int id, String name) {
        if (id <= 0 || name.isEmpty())
            throw new NullPointerException(EnumErrors.Models.CATEGORY_CREATION.message);
    }

    public int getCategoryId() {
        return id;
    }

    public void setBaseCategoryId(int newbBaseCategoryId) {
        this.baseCategoryId = newbBaseCategoryId;
    }

    public int getBaseCategoryId() {
        return baseCategoryId;
    }

    public String getCategoryName() {
        return name;
    }

    public void setCategoryName(String newName) {
        this.name = newName;
    }
}

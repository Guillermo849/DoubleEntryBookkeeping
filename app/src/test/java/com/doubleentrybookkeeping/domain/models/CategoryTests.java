package com.doubleentrybookkeeping.domain.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.doubleentrybookkeeping.domain.exceptions.EnumErrors;

import org.junit.Test;

public class CategoryTests {
    @Test
    public void base_category_object_is_created_properly() {
        var catergory = new Category(1, "Shopping");
        assertEquals(1, catergory.getCategoryId());
        assertEquals(0, catergory.getBaseCategoryId());
        assertEquals("Shopping", catergory.getCategoryName());
    }

    @Test
    public void base_category_object_is_created_with_an_empty_name(){
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Category(1, "");
        });
        assertEquals(EnumErrors.Models.CATEGORY_CREATION.message, exception.getMessage());
    }

    @Test
    public void base_category_object_is_created_with_an_invalid_id(){
        Exception exception = assertThrows(NullPointerException.class, () -> {
            new Category(-3, "");
        });
        assertEquals(EnumErrors.Models.CATEGORY_CREATION.message, exception.getMessage());
    }
}

package com.doubleentrybookkeeping.domain.exceptions;

public class EnumErrors {
    public enum Models {
        CATEGORY_CREATION("Category must contain a valid id and name."),
        ENTRY_CREATION("Entry must contain a valid id, category, type, method, amount, date, and concept.");

        public final String message;

        Models(String message) {
            this.message = message;
        }
    }

    public enum DataBase {
        PATH_EMPTY("DataBase path is empty."),
        FILE_NOT_EXISTS("File doesn't exists"),
        INVALID_COLUMN_VALUE_TYPE("Value type is not valid for column: "),
        DB_OBJECT_DOES_NOT_EXIST("The %s %s does not exist in the %s DataBase");
        public final String message;

        DataBase(String message) {
            this.message = message;
        }
    }

    public enum CategoryDbManagerActions {
        CATEGORY_EXISTS("Category already exists");
        public final String message;
        CategoryDbManagerActions(String message){this.message = message;}
    }
}

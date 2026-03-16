package com.doubleentrybookkeeping.domain.exceptions;

public class EnumErrors {
    public enum Models {
        CATEGORY_CREATION("Category must contain a valid id and name.");

        public final String message;

        Models(String message) {
            this.message = message;
        }
    }

    public enum DataBase {
        PATH_EMPTY("DataBase path is empty."), FILE_NOT_EXISTS("File doesn't exists");
        public final String message;

        DataBase(String message) {
            this.message = message;
        }
    }
}

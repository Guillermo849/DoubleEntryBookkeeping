package com.doubleentrybookkeeping.data.local;

public class EnumErrors {
    public enum Models {
        CATEGORY_CREATION("Category must contain a valid id and name.");

        public final String message;
        private Models(String message) {
            this.message = message;
        }
    }
}

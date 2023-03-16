package com.dangol.dangolsonnimbackend.store.enumeration;

public enum CategoryType {
    NONE(0),
    KOREAN(1),
    JAPANESE(2),
    CHINESE(3),
    WESTERN(4),
    BUNSIK(5),
    CAFE(6);

    private int categoryId;

    CategoryType(int categoryId) {
        this.categoryId = categoryId;
    }
}

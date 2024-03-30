package ru.teamscore.java23.books.model.enums;

import lombok.Getter;

@Getter
public enum CatalogSortOption {
    TITLE("title"),
    PRICE("price"),
    YEAR("year"),
    RELEVANCE("relevance");

    private final String columnName;

    CatalogSortOption(String columnName) {
        this.columnName = columnName;
    }
}

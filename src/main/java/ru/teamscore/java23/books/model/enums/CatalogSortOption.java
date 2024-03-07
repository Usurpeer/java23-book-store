package ru.teamscore.java23.books.model.enums;

import lombok.Getter;
import ru.teamscore.java23.books.model.entities.Book;

import java.util.Comparator;

@Getter
public enum CatalogSortOption {
    TITLE("title"),
    PRICE("price"),
    YEAR("year");

    private final String columnName;
    CatalogSortOption(String columnName){
        this.columnName = columnName;
    }
}

package ru.teamscore.java23.books.model.enums;

import lombok.Getter;
import ru.teamscore.java23.books.model.entities.Book;

import java.util.Comparator;

public enum CatalogSortOption {
    TITLE(Comparator.comparing(Book::getTitle)),
    PRICE(Comparator.comparing(Book::getPrice)),
    YEAR(Comparator.comparing(Book::getYear));

    @Getter
    private Comparator <Book> comparator;
    CatalogSortOption(Comparator<Book> comparator){
        this.comparator = comparator;
    }
}

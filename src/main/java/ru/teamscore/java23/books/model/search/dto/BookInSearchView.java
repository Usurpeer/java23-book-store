package ru.teamscore.java23.books.model.search.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@RequiredArgsConstructor
public class BookInSearchView {
    @Setter(AccessLevel.NONE)
    private long bookId;
    private String bookToString;
    private double relevanceScore;

    public BookInSearchView(long bookId, String bookToString) {
        this.bookId = bookId;
        this.bookToString = bookToString;
    }
}

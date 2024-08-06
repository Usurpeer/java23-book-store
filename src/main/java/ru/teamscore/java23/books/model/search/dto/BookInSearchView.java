package ru.teamscore.java23.books.model.search.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookInSearchView {
    private long bookId;
    private double relevanceScore;

    public BookInSearchView(long bookId, double relevanceScore) {
        this.bookId = bookId;
        this.relevanceScore = relevanceScore;
    }
}

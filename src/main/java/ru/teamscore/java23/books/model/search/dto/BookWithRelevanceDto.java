package ru.teamscore.java23.books.model.search.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.teamscore.java23.books.model.entities.Book;

@Data
@AllArgsConstructor
public class BookWithRelevanceDto {
    private Book book;
    private double relevanceScore;
}

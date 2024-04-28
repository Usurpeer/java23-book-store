package ru.teamscore.java23.books.model.search.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.teamscore.java23.books.model.entities.Book;

import java.util.List;

@Data
@RequiredArgsConstructor
public class SearchResult {
    private List<Book> books;
    private long quantity;
}

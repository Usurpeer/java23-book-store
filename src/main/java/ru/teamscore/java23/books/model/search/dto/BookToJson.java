package ru.teamscore.java23.books.model.search.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@RequiredArgsConstructor
public class BookToJson {
    private long id;
    private String title;
    private String description;
    private String publisher;
    private double price;
    private int year;
    private List<String> authors;
    private List<String> genres;
}

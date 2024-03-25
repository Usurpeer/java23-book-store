package ru.teamscore.java23.books.controllers.dto.catalog;

import lombok.Data;

import java.util.Set;

@Data
public class FiltersRequestDto {
    private String minPrice;
    private String maxPrice;
    private String minYear;
    private String maxYear;
    private Set<Long> authors; // хранятся id автора
    private Set<Long> genres; // хранятся id жанра
    private Set<String> publishers;
}

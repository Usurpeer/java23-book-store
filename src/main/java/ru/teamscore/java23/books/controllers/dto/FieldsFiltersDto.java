package ru.teamscore.java23.books.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FieldsFiltersDto {
    private List<String> allPublishers;
    private List<GenreDto> allGenres;
    private List<AuthorDto> allAuthors;
}

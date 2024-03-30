package ru.teamscore.java23.books.controllers.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.teamscore.java23.books.controllers.dto.AuthorDto;
import ru.teamscore.java23.books.controllers.dto.GenreDto;

import java.util.List;

@Data
@AllArgsConstructor
public class FieldsFiltersDto {
    private List<String> allPublishers;
    private List<GenreDto> allGenres;
    private List<AuthorDto> allAuthors;
}

package ru.teamscore.java23.books.controllers.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.teamscore.java23.books.controllers.dto.AuthorDto;
import ru.teamscore.java23.books.controllers.dto.GenreDto;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CatalogBookDto {
    private long id;
    private String title;
    private String description;
    private BigDecimal price;
    private List<GenreDto> genres;
    private List<AuthorDto> authors;
    private String imageName;
    private int year;
}

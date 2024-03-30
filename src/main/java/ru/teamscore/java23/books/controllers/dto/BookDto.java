package ru.teamscore.java23.books.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookDto {
    private long id;
    private String title;
    private String description;
    private String status;
    private BigDecimal price;
    private List<GenreDto> genres;
    private List<AuthorDto> authors;
    private String imageName;
    private int year;
}

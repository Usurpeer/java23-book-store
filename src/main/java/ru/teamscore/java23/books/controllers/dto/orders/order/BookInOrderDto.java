package ru.teamscore.java23.books.controllers.dto.orders.order;

import lombok.Data;
import ru.teamscore.java23.books.controllers.dto.AuthorDto;
import ru.teamscore.java23.books.controllers.dto.GenreDto;

import java.util.List;

@Data
public class BookInOrderDto {
    private long id;
    private String title;
    private List<GenreDto> genres;
    private List<AuthorDto> authors;
    private String imageName;
    private int year;
}

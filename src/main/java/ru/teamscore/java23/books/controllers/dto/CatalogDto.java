package ru.teamscore.java23.books.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CatalogDto {
    private List<BookDto> books;
    private FieldsFiltersDto filters;
    private long booksQuantity;
}

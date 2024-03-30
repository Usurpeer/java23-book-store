package ru.teamscore.java23.books.controllers.dto.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CatalogDto {
    private List<CatalogBookDto> books;
    private FieldsFiltersDto filters;
    private long booksQuantity;
}

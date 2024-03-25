package ru.teamscore.java23.books.controllers.dto.catalog;

import lombok.Data;

@Data
public class CatalogRequestDto {
    private int page;
    private int pageSize;
    private String field;
    private Boolean asc;
    private String search;
    private FiltersRequestDto filters;
}

package ru.teamscore.java23.books.controllers.rest;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.teamscore.java23.books.controllers.dto.*;
import ru.teamscore.java23.books.model.Catalog;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;
import ru.teamscore.java23.books.model.enums.CatalogSortOption;
import ru.teamscore.java23.books.model.search.SearchFilter;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/catalog")
public class CatalogController {
    @Autowired
    private final Catalog catalog;
    @Autowired
    private final ModelMapper modelMapper;

    @PostMapping
    public CatalogDto getCatalog(@RequestBody CatalogRequestDto request) {
        // инициализация фильтрации
        SearchFilter searchFilter = parseFiltersRequest(request.getFilters());
        // фильтрация всех книг по параметрам
        var filteredBooks = searchFilter.filter();
        // сортировка и пагинация отфильтрованных книг
        var sortedBooks = catalog.getSorted(request.getField() != null ?
                        CatalogSortOption.valueOf(request.getField().toUpperCase()) : CatalogSortOption.TITLE,
                request.getAsc() != null ? request.getAsc() : false,
                request.getSearch(),
                request.getPage(),
                request.getPageSize(), filteredBooks);
        
        // преобразование в DTO объекты к клиенту
        var filteringFields = new FieldsFiltersDto(getAllPublishers(), getAllGenres(), getAllAuthors());
        var booksDto = sortedBooks.stream().map(this::mapCatalogBook).toList();

        return new CatalogDto(booksDto, filteringFields, filteredBooks.size());
    }

    public SearchFilter parseFiltersRequest(FiltersRequestDto filters) {
        return SearchFilter.builder()
                .minPrice(parseBigDecimal(filters.getMinPrice()))
                .maxPrice(parseBigDecimal(filters.getMaxPrice()))
                .minYear(parseInteger(filters.getMinYear()))
                .maxYear(parseInteger(filters.getMaxYear()))
                .authors(parseAuthors(filters.getAuthors()))
                .genres(parseGenres(filters.getGenres()))
                .publishers(new HashSet<>(filters.getPublishers()))
                .books(catalog.getOpenBooks())
                .build();
    }

    private Optional<BigDecimal> parseBigDecimal(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return Optional.of(new BigDecimal(value));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private Optional<Integer> parseInteger(String value) {
        if (value != null && !value.isEmpty()) {
            try {
                return Optional.of(Integer.parseInt(value));
            } catch (NumberFormatException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private Set<Author> parseAuthors(Set<Long> authorIds) {
        if (authorIds == null || authorIds.isEmpty()) {
            return new HashSet<>();
        }
        var manager = catalog.getAuthorManager();
        return authorIds.stream()
                .map(manager::getAuthor) // Получение Optional<Author> по id из менеджера
                .filter(Optional::isPresent) // Оставляем только непустые Optional
                .map(Optional::get) // Извлекаем значение из Optional
                .collect(Collectors.toSet()); // Собираем в Set<Author>
    }

    private Set<Genre> parseGenres(Set<Long> genreIds) {
        if (genreIds == null || genreIds.isEmpty()) {
            return new HashSet<>();
        }
        var manager = catalog.getGenreManager();
        return genreIds.stream()
                .map(manager::getGenre)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toSet());
    }

    /*@GetMapping
    public CatalogDto getCatalog(@RequestParam int page,
                                 @RequestParam int pageSize,
                                 @RequestParam(required = false) String sortingField,
                                 @RequestParam(required = false) Boolean sortingDesc,
                                 @RequestParam(required = false) String search) {

        var books = catalog.getSorted(
                sortingField != null ? CatalogSortOption.valueOf(sortingField.toUpperCase()) : CatalogSortOption.TITLE,
                sortingDesc != null ? sortingDesc : false,
                search,
                page,
                pageSize
        );

        var filters = new FieldsFiltersDto(getAllPublishers(), getAllGenres(), getAllAuthors());
        var booksDto = books.stream().map(this::mapCatalogBook).toList();
        return new CatalogDto(booksDto, filters);
    }*/

    private BookDto mapCatalogBook(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    private List<GenreDto> getAllGenres() {
        var genres = catalog.getGenreManager().getAllGenres();
        return genres.stream().map(this::mapGenreDto).toList();
    }

    private GenreDto mapGenreDto(Genre genre) {
        return modelMapper.map(genre, GenreDto.class);
    }

    private List<AuthorDto> getAllAuthors() {
        var authors = catalog.getAuthorManager().getAllAuthors();
        return authors.stream().map(this::mapAuthorDto).toList();
    }

    private AuthorDto mapAuthorDto(Author author) {
        return modelMapper.map(author, AuthorDto.class);
    }

    private List<String> getAllPublishers() {
        return catalog.getAllPublishers();
    }

    /*@GetMapping("count")
    public CatalogCountDto getCount() {
        return new CatalogCountDto(catalog.getBooksCount());
    }*/
}

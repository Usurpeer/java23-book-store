package ru.teamscore.java23.books.controllers.rest;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.teamscore.java23.books.controllers.dto.AuthorDto;
import ru.teamscore.java23.books.controllers.dto.GenreDto;
import ru.teamscore.java23.books.controllers.dto.catalog.*;
import ru.teamscore.java23.books.model.Catalog;
import ru.teamscore.java23.books.model.RestToPythonService;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;
import ru.teamscore.java23.books.model.search.SearchFilter;
import ru.teamscore.java23.books.model.search.SearchManager;

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
    @Autowired
    private final RestToPythonService pythonService;

    @PostMapping
    public CatalogDto getCatalogPost(@RequestBody CatalogRequestDto request) {
        // инициализация фильтрации
        SearchFilter searchFilter = parseFiltersRequest(request.getFilters());

        // фильтрация всех книг по параметрам
        var filteredBooks = searchFilter.filter();

        // поиск, сортировка, пагинация отфильтрованных книг
        SearchManager searchManager = new SearchManager(request, filteredBooks, pythonService);
        var sortedBooks = searchManager.getBooks();

        // преобразование в DTO объекты к клиенту
        var filteringFields = new FieldsFiltersDto(getAllPublishers(), getAllGenres(), getAllAuthors());
        var booksDto = sortedBooks.stream().map(this::mapCatalogBook).toList();

        return new CatalogDto(booksDto, filteringFields, searchManager.getBooksInSearchQuantity());
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

    private CatalogBookDto mapCatalogBook(Book book) {
        return modelMapper.map(book, CatalogBookDto.class);
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
}

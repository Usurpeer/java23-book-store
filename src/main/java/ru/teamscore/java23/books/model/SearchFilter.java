package ru.teamscore.java23.books.model;

import lombok.*;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

// тут непонятно final ли поля, каждый раз будет создаваться новый объект или редактироваться при изменении параметров
//
@Getter
@Setter
@AllArgsConstructor
public class SearchFilter {
    private final BigDecimal minPrice;
    private final BigDecimal maxPrice;
    private final int minYear;
    private final int maxYear;
    private final Set<Author> authors;
    private final Set<Genre> genres;
    private final String publisher;
    private final Catalog catalog;

    // он возвращает книгу или может возвращать Catalog
    private Catalog filter() {
        Catalog filteredCatalog = filterOnPrice(catalog);

        filteredCatalog = filterOnYear(filteredCatalog);
        filteredCatalog = filterOnPublisher(filteredCatalog);

        return filteredCatalog;
    }

    public Catalog filterOnPrice(Catalog catalog) {
        return new Catalog(
                catalog.getBooks().stream()
                        .filter(book -> book.getPrice().compareTo(minPrice) >= 0 &&
                                book.getPrice().compareTo(maxPrice) <= 0)
                        .toList());
    }

    public Catalog filterOnYear(Catalog catalog) {
        return new Catalog(
                catalog.getBooks().stream()
                        .filter(book -> book.getYear() >= maxYear && book.getYear() <= maxYear)
                        .toList());
    }

    public Catalog filterOnPublisher(Catalog catalog) {
        return new Catalog(
                catalog.getBooks().stream()
                        .filter(book -> book.getPublisher().equals(publisher))
                        .toList());
    }

    // метод группировки книг по жанрам
    public Map<Genre, List<Book>> groupByGenre(Catalog catalog) {
        return catalog.getBooks().stream()
                .flatMap(book -> book.getGenres().stream().map(genre -> Map.entry(genre, book)))
                .filter(entry -> genres.contains(entry.getKey()))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue,
                        Collectors.toList())));
    }

    // метод группировки книг по авторам
    public Map<Author, List<Book>> groupByAuthors(Catalog catalog) {
        return catalog.getBooks().stream()
                .flatMap(book -> book.getAuthors().stream().map(author -> Map.entry(author, book)))
                .filter(entry -> authors.contains(entry.getKey()))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue,
                        Collectors.toList())));
    }
}

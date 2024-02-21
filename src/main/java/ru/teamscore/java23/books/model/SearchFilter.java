package ru.teamscore.java23.books.model;

import lombok.*;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

// тут непонятно final ли поля, каждый раз будет создаваться новый объект или редактироваться при изменении параметров
//
@Getter
@Setter
@AllArgsConstructor
public class SearchFilter {
    private final Optional<BigDecimal> minPrice;
    private final Optional<BigDecimal> maxPrice;
    private final Optional<Integer> minYear;
    private final Optional<Integer> maxYear;
    private final Set<Author> authors;
    private final Set<Genre> genres;
    private final Optional<String> publisher; // ИЛИ СДЕЛАТЬ СПИСОК ИЗДАТЕЛЬСТВ
    private final List<Book> books; // тут был Catalog


    public List<Book> filter() {
        if (this.books == null || this.books.isEmpty()) {
            return Collections.emptyList();
        }

        List<Book> filteredBooks = new ArrayList<>(this.books);

        // Фильтрация по цене
        if (minPrice != null && maxPrice != null && minPrice.isPresent() && maxPrice.isPresent()) {
            filteredBooks = filterOnPrice(filteredBooks);
        }

        // Фильтрация по году
        if (minYear != null && maxYear != null && minYear.isPresent() && maxYear.isPresent()) {
            filteredBooks = filterOnYear(filteredBooks);
        }

        // Фильтрация по издателю
        if (publisher != null && publisher.isPresent()) {
            filteredBooks = filterOnPublisher(filteredBooks);
        }

        // Фильтрация по жанрам
        if (genres != null && !genres.isEmpty()) {
            filteredBooks = filterOnGenre(filteredBooks);
        }

        // Фильтрация по авторам
        if (authors != null && !authors.isEmpty()) {
            filteredBooks = filterOnAuthor(filteredBooks);
        }

        return filteredBooks;
    }


    public List<Book> filterOnPrice() {
        return filterOnPrice(this.books);
    }

    public List<Book> filterOnPrice(List<Book> books) {
        // эти варианты не работают, выкидывает Exception
        // if (!minPrice.isPresent() || !maxPrice.isPresent() || books == null) {
        // if (minPrice.isEmpty() || maxPrice.isEmpty() || books == null) {
        if (minPrice == null || maxPrice == null || minPrice.isEmpty() || maxPrice.isEmpty() || books == null) {
            return Collections.emptyList();
        }

        return books.stream()
                .filter(book -> {
                    BigDecimal bookPrice = book.getPrice();
                    return bookPrice != null && bookPrice.compareTo(minPrice.get()) >= 0 &&
                            bookPrice.compareTo(maxPrice.get()) <= 0;
                })
                .toList();
    }

    public List<Book> filterOnYear() {
        return filterOnYear(this.books);
    }

    public List<Book> filterOnYear(List<Book> books) {
        if (minYear == null || maxYear == null || minYear.isEmpty() || maxYear.isEmpty() || books == null) {
            return Collections.emptyList();
        }

        return books.stream()
                .filter(book -> book.getYear() >= minYear.get() && book.getYear() <= maxYear.get())
                .toList();
    }

    public List<Book> filterOnPublisher() {
        return filterOnPublisher(this.books);
    }

    public List<Book> filterOnPublisher(List<Book> books) {
        if (publisher == null || publisher.isEmpty() || books == null) {
            return Collections.emptyList();
        }

        return books.stream()
                .filter(book -> !book.getPublisher().isEmpty() && book.getPublisher().equals(publisher.get()))
                .toList();
    }


    public List<Book> filterOnGenre() {
        return filterOnGenre(this.books);
    }

    // если книга содержит хотя бы один жанр, то она включается
    public List<Book> filterOnGenre(List<Book> books) {
        if (genres == null || genres.isEmpty() || books == null) {
            return Collections.emptyList();
        }

        return books.stream()
                .filter(book -> !Collections.disjoint(book.getGenres(), genres))
                .toList();
    }


    public List<Book> filterOnAuthor() {
        return filterOnAuthor(this.books);
    }

    // если книга содержит хотя бы одного автора, то она включается
    public List<Book> filterOnAuthor(List<Book> books) {
        if (authors == null || authors.isEmpty() || books == null) {
            return Collections.emptyList();
        }

        return books.stream()
                .filter(book -> !Collections.disjoint(book.getAuthors(), authors))
                .toList();
    }

    // Оставлю методы, раз написал

    // метод группировки книг по жанрам
    private Map<Genre, List<Book>> groupByGenre(List<Book> books) {
        return books.stream()
                .flatMap(book -> book.getGenres().stream().map(genre -> Map.entry(genre, book)))
                .filter(entry -> genres.contains(entry.getKey()))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue,
                        Collectors.toList())));
    }

    // метод группировки книг по авторам
    private Map<Author, List<Book>> groupByAuthors(List<Book> books) {
        return books.stream()
                .flatMap(book -> book.getAuthors().stream().map(author -> Map.entry(author, book)))
                .filter(entry -> authors.contains(entry.getKey()))
                .collect(Collectors.groupingBy(Map.Entry::getKey, Collectors.mapping(Map.Entry::getValue,
                        Collectors.toList())));
    }
}

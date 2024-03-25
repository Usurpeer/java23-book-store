package ru.teamscore.java23.books.model.search;

import lombok.*;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@Builder
@NonNull
public class SearchFilter {
    @Builder.Default // так, чтобы не работала передача null в параметры и при этом по умолчанию было не null
    private Optional<BigDecimal> minPrice = Optional.empty();
    @Builder.Default
    private Optional<BigDecimal> maxPrice = Optional.empty();
    @Builder.Default
    private Optional<Integer> minYear = Optional.empty();
    @Builder.Default
    private Optional<Integer> maxYear = Optional.empty();
    @Builder.Default
    private Set<Author> authors = new HashSet<>();
    @Builder.Default
    private Set<Genre> genres = new HashSet<>();
    @Builder.Default
    private Set<String> publishers = new HashSet<>();
    @Builder.Default
    private List<Book> books = new ArrayList<>();


    public List<Book> filter() {
        if (this.books.isEmpty()) {
            return Collections.emptyList();
        }

        List<Book> filteredBooks = new ArrayList<>(this.books);

        // Фильтрация по цене
        if (minPrice.isPresent() && maxPrice.isPresent()) {
            filteredBooks = filterOnPrice();
        }

        // Фильтрация по году
        if (minYear.isPresent() && maxYear.isPresent()) {
            filteredBooks = filterOnYear();
        }

        // Фильтрация по издателю
        if (!publishers.isEmpty()) {
            filteredBooks = filterOnPublishers();
        }

        // Фильтрация по жанрам
        if (!genres.isEmpty()) {
            filteredBooks = filterOnGenre();
        }

        // Фильтрация по авторам
        if (!authors.isEmpty()) {
            filteredBooks = filterOnAuthor();
        }

        return filteredBooks;
    }


    public List<Book> filterOnPrice() {
        if (minPrice.isEmpty() || maxPrice.isEmpty() || books.isEmpty()) {
            return Collections.emptyList();
        }

        this.books = books.stream()
                .filter(book -> {
                    BigDecimal bookPrice = book.getPrice();
                    return bookPrice.compareTo(minPrice.get()) >= 0 &&
                            bookPrice.compareTo(maxPrice.get()) <= 0;
                })
                .toList();
        return this.books;
    }


    public List<Book> filterOnYear() {
        if (minYear.isEmpty() || maxYear.isEmpty() || books.isEmpty()) {
            return Collections.emptyList();
        }

        this.books = books.stream()
                .filter(book -> book.getYear() >= minYear.get() && book.getYear() <= maxYear.get())
                .toList();
        return this.books;
    }


    public List<Book> filterOnPublishers() {
        if (publishers.isEmpty() || books.isEmpty()) {
            return Collections.emptyList();
        }

        this.books = books.stream()
                .filter(book -> publishers.contains(book.getPublisher()))
                .toList();
        return this.books;
    }


    // если книга содержит хотя бы один жанр, то она включается
    public List<Book> filterOnGenre() {
        if (genres.isEmpty() || books.isEmpty()) {
            return Collections.emptyList();
        }

        this.books = books.stream()
                .filter(book -> !Collections.disjoint(book.getGenres(), genres))
                .toList();
        return this.books;
    }


    // если книга содержит хотя бы одного автора, то она включается
    public List<Book> filterOnAuthor() {
        if (authors.isEmpty() || books.isEmpty()) {
            return Collections.emptyList();
        }

        this.books = books.stream()
                .filter(book -> !Collections.disjoint(book.getAuthors(), authors))
                .toList();
        return this.books;
    }

}

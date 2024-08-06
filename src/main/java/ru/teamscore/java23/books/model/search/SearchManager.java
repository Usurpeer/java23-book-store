package ru.teamscore.java23.books.model.search;

import lombok.Getter;
import ru.teamscore.java23.books.controllers.dto.catalog.CatalogRequestDto;
import ru.teamscore.java23.books.model.Catalog;
import ru.teamscore.java23.books.model.PythonService;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;
import ru.teamscore.java23.books.model.enums.CatalogSortOption;
import ru.teamscore.java23.books.model.search.dto.BookInSearchView;
import ru.teamscore.java23.books.model.search.dto.BookToJson;
import ru.teamscore.java23.books.model.search.dto.BookWithRelevanceDto;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class SearchManager {
    private final CatalogSortOption option;
    private final boolean asc;
    private final String search;
    private final int page;
    private final int pageSize;
    private final String searchType;
    private List<BookWithRelevanceDto> books;
    private final PythonService pythonService;
    private final Catalog catalog;

    @Getter
    private long booksInSearchQuantity;

    public SearchManager(CatalogRequestDto request, List<Book> books, PythonService pythonService, Catalog catalog) {
        this.asc = request.getAsc() != null ? request.getAsc() : false;
        this.search = request.getSearch() != null ? request.getSearch() : "";
        this.page = request.getPage();
        this.pageSize = request.getPageSize();
        this.books = books.stream().map((b) -> new BookWithRelevanceDto(b, 0)).toList();
        this.searchType = request.getSearchType() != null ? request.getSearchType() : "java-search";

        if (search.isEmpty() && CatalogSortOption.valueOf(request.getField().toUpperCase()) == CatalogSortOption.RELEVANCE) {
            this.option = CatalogSortOption.TITLE;
        } else {
            this.option = request.getField() != null ?
                    CatalogSortOption.valueOf(request.getField().toUpperCase()) : CatalogSortOption.TITLE;
        }
        this.booksInSearchQuantity = books.size();
        this.pythonService = pythonService;
        this.catalog = catalog;
    }


    public List<Book> getBooks() {
        if (!search.isEmpty()) {
            switch (searchType) {
                case "pyth-1-search":
                    return pyth1Search();
                case "pyth-2-search":
                    return pyth2Search();
                case "combo":
                    return combineSearch();
                default:
                    return javaSearch();
            }
        }
        // выполнить сортировку и пагинацию
        return sortingAndPagination().stream().map(BookWithRelevanceDto::getBook).toList();
    }

    private List<Book> pyth1Search() {
        var bookWithoutRev = books.stream().map(BookWithRelevanceDto::getBook).toList();
        List<BookToJson> jsonBooks = mapToJsonView(bookWithoutRev);
        Map<Long, Double> searchRes = pythonService.sendToPyth1(search, jsonBooks);
        List<Book> alLBooksOnSearch = searchRes.keySet()
                .stream()
                .map(catalog::getBook)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        this.booksInSearchQuantity = alLBooksOnSearch.size();
        this.books = alLBooksOnSearch.stream()
                .map((book -> new BookWithRelevanceDto(book, searchRes.get(book.getId()))))
                .toList();

        return sortingAndPagination().stream().map(BookWithRelevanceDto::getBook).toList();
    }

    private List<Book> pyth2Search() {
        var bookWithoutRev = books.stream().map(BookWithRelevanceDto::getBook).toList();
        List<BookToJson> jsonBooks = mapToJsonView(bookWithoutRev);
        Map<Long, Double> searchRes = pythonService.sendToPyth2(search, jsonBooks);

        List<Book> alLBooksOnSearch = searchRes.keySet()
                .stream()
                .map(catalog::getBook)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        this.booksInSearchQuantity = alLBooksOnSearch.size();
        this.books = alLBooksOnSearch.stream()
                .map((book -> new BookWithRelevanceDto(book, searchRes.get(book.getId()))))
                .toList();

        return sortingAndPagination().stream().map(BookWithRelevanceDto::getBook).toList();
    }

    private List<BookToJson> mapToJsonView(List<Book> bookWithoutRev) {
        List<BookToJson> jsonBooks = new ArrayList<>();
        for (Book book : bookWithoutRev) {
            BookToJson bookToJson = new BookToJson();

            bookToJson.setId(book.getId());
            bookToJson.setTitle(book.getTitle());
            bookToJson.setDescription(book.getDescription());
            bookToJson.setPublisher(book.getPublisher());
            bookToJson.setPrice(book.getPrice().doubleValue());
            bookToJson.setYear(book.getYear());

            List<String> authors = new ArrayList<>();
            for (Author author : book.getAuthors()) {
                authors.add(author.toString());
            }
            bookToJson.setAuthors(authors);

            List<String> genres = new ArrayList<>();
            for (Genre genre : book.getGenres()) {
                genres.add(genre.toString());
            }
            bookToJson.setGenres(genres);

            jsonBooks.add(bookToJson);
        }
        return jsonBooks;
    }

    private List<Book> javaSearch() {
        // выполнить поиск
        var bookWithoutRev = books.stream().map(BookWithRelevanceDto::getBook).toList();
        var booksObjAfterSearch = SearchEngine.searchByAll(search, bookWithoutRev);
        booksInSearchQuantity = booksObjAfterSearch.size();

        // заполнить relevanceScore
        books = books.stream()
                .filter(dto -> booksObjAfterSearch.stream()
                        .anyMatch(view -> view.getBookId() == dto.getBook().getId()))
                .peek(dto -> {
                    Optional<BookInSearchView> matchingBook = booksObjAfterSearch.stream()
                            .filter(view -> view.getBookId() == dto.getBook().getId())
                            .findFirst();
                    matchingBook.ifPresent(bObj -> dto.setRelevanceScore(bObj.getRelevanceScore()));
                })
                .toList();

        return sortingAndPagination().stream().map(BookWithRelevanceDto::getBook).toList();
    }

    private List<BookWithRelevanceDto> sortingAndPagination() {
        List<BookWithRelevanceDto> mutableBooks = new ArrayList<>(books);

        Map<CatalogSortOption, Comparator<BookWithRelevanceDto>> comparators = Map.of(
                CatalogSortOption.TITLE, Comparator.comparing(b -> b.getBook().getTitle(), String.CASE_INSENSITIVE_ORDER),
                CatalogSortOption.PRICE, Comparator.comparing(b -> b.getBook().getPrice()),
                CatalogSortOption.YEAR, Comparator.comparingInt(b -> b.getBook().getYear()),
                CatalogSortOption.RELEVANCE, Comparator.comparingDouble(BookWithRelevanceDto::getRelevanceScore)
        );

        Comparator<BookWithRelevanceDto> comparator = comparators.getOrDefault(option, Comparator
                .comparing(dto -> dto.getBook().getTitle(), String.CASE_INSENSITIVE_ORDER)
        );

        // порядок сортировки
        comparator = asc ? comparator : comparator.reversed();

        // доп сортировка по id
        mutableBooks.sort(comparator.thenComparingLong(b -> b.getBook().getId()));

        // пагинация
        int fromIndex = Math.min(page * pageSize, mutableBooks.size());
        int toIndex = Math.min((page + 1) * pageSize, mutableBooks.size());
        return mutableBooks.subList(fromIndex, toIndex);
    }


    private List<Book> combineSearch() {
        // Создаем CompletableFuture для выполнения двух поисковых запросов параллельно
        CompletableFuture<List<BookInSearchView>> javaSearchFuture = CompletableFuture.supplyAsync(this::isolatedJavaSearch);
        CompletableFuture<List<BookInSearchView>> pythSearchFuture = CompletableFuture.supplyAsync(this::isolatedpyth2Search);

        // Дожидаемся завершения обоих CompletableFuture
        CompletableFuture.allOf(javaSearchFuture, pythSearchFuture).join();

        // Получаем результаты поиска из CompletableFuture
        List<BookInSearchView> booksJavaSearch = javaSearchFuture.join();
        List<BookInSearchView> booksPyth = pythSearchFuture.join();

        List<BookInSearchView> combinedResults = new ArrayList<>();
        combinedResults.addAll(booksJavaSearch);
        combinedResults.addAll(booksPyth);

        // Суммируем значения релевантности для каждой книги
        Map<Long, Double> summedRelevance = new HashMap<>();
        for (BookInSearchView bookInSearchView : combinedResults) {
            long bookId = bookInSearchView.getBookId();
            double relevance = summedRelevance.getOrDefault(bookId, 0.0);
            summedRelevance.put(bookId, relevance + bookInSearchView.getRelevanceScore());
        }

        // Находим максимальное значение суммы релевантности
        double maxSum = summedRelevance.values().stream()
                .mapToDouble(Double::doubleValue)
                .max().orElse(0.0);

        // Нормализуем значения релевантности
        Map<Long, Double> normalizedRelevance = summedRelevance.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey,
                        entry -> entry.getValue() / maxSum));

        // Отфильтровываем значения релевантности по условию > 0.6
        normalizedRelevance = normalizedRelevance.entrySet().stream()
                .filter(entry -> entry.getValue() > 0.65)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<Book> alLBooksOnSearch = normalizedRelevance.keySet()
                .stream()
                .map(catalog::getBook)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();

        this.booksInSearchQuantity = alLBooksOnSearch.size();
        Map<Long, Double> finalNormalizedRelevance = normalizedRelevance;
        this.books = alLBooksOnSearch.stream()
                .map((book -> new BookWithRelevanceDto(book, finalNormalizedRelevance.get(book.getId()))))
                .toList();

        return sortingAndPagination().stream().map(BookWithRelevanceDto::getBook).toList();
    }

    private List<BookInSearchView> isolatedpyth2Search() {
        var bookWithoutRev = books.stream().map(BookWithRelevanceDto::getBook).toList();
        List<BookToJson> jsonBooks = mapToJsonView(bookWithoutRev);
        Map<Long, Double> searchRes = pythonService.sendToPyth2(search, jsonBooks);

        return searchRes.entrySet().stream()
                .map(entry -> new BookInSearchView(entry.getKey(), entry.getValue()))
                .toList();
    }

    private List<BookInSearchView> isolatedJavaSearch() {
        // выполнить поиск
        var bookWithoutRev = books.stream().map(BookWithRelevanceDto::getBook).toList();
        return SearchEngine.searchByAll(search, bookWithoutRev);
    }
}

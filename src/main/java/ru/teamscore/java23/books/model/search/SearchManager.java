package ru.teamscore.java23.books.model.search;

import ru.teamscore.java23.books.controllers.dto.catalog.CatalogRequestDto;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.enums.CatalogSortOption;
import ru.teamscore.java23.books.model.search.dto.BookInSearchView;
import ru.teamscore.java23.books.model.search.dto.BookWithRelevanceDto;

import java.util.*;

public class SearchManager {
    private final CatalogSortOption option;
    private final boolean asc;
    private final String search;
    private final int page;
    private final int pageSize;
    private List<BookWithRelevanceDto> books;

    // критерий - пороговое значение попадания в результат поиска rel < RTH - не попадает
    private static final double RELEVANCE_THRESHOLD_SCORE = 0.000001;

    public SearchManager(CatalogRequestDto request, List<Book> books) {
        this.asc = request.getAsc() != null ? request.getAsc() : false;
        this.search = request.getSearch() != null ? request.getSearch() : "";
        this.page = request.getPage();
        this.pageSize = request.getPageSize();
        this.books = books.stream().map((b) -> new BookWithRelevanceDto(b, 0)).toList();

        if (search.isEmpty() && CatalogSortOption.valueOf(request.getField().toUpperCase()) == CatalogSortOption.RELEVANCE) {
            this.option = CatalogSortOption.TITLE;
        } else {
            this.option = request.getField() != null ?
                    CatalogSortOption.valueOf(request.getField().toUpperCase()) : CatalogSortOption.TITLE;
        }
    }


    public List<Book> getBooks() {
        if (!search.isEmpty()) {
            // выполнить поиск
            var bookWithoutRev = books.stream().map(BookWithRelevanceDto::getBook).toList();
            var booksObjAfterSearch = SearchEngine.searchByAll(search, bookWithoutRev);

            // заполнить relevanceScore и отфильтровать по критерию
            books = books.stream()
                    .peek(dto -> {
                        Optional<BookInSearchView> matchingBook = booksObjAfterSearch.stream()
                                .filter(view -> view.getBookId() == dto.getBook().getId())
                                .findFirst();
                        matchingBook.ifPresent(bObj -> dto.setRelevanceScore(bObj.getRelevanceScore()));
                    })
                    .filter(bookWithRelevanceDto -> bookWithRelevanceDto.getRelevanceScore() >= RELEVANCE_THRESHOLD_SCORE)
                    .toList();
        }
        // выполнить сортировку и пагинацию
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

}

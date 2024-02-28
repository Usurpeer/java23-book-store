package ru.teamscore.java23.books.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.enums.CatalogSortOption;

import java.util.*;

@Getter
@RequiredArgsConstructor
public class Catalog {
    private final List<Book> books = new ArrayList<>();

    public int getBooksCount() {
        return books.size();
    }

    public Optional<Book> getBook(long id) {
        return books.stream().filter(i -> i.getId() == id).findFirst();
    }

    public void addBook(@NonNull Book book) {
        if (getBook(book.getId()).isEmpty()) {
            books.add(book);
        }
    }

    public Collection<Book> getSorted(CatalogSortOption option, boolean desc, int page, int pageSize) {
        Comparator<Book> comparator = option.getComparator();
        if (desc) {
            comparator = comparator.reversed();
        }
        return books.stream()
                .sorted(comparator)
                .skip((long) page * pageSize)
                .limit(pageSize)
                .toList();
    }
}

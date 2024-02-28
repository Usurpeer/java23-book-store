package ru.teamscore.java23.books.model;

import org.junit.jupiter.api.Test;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;
import ru.teamscore.java23.books.model.enums.BookStatus;
import ru.teamscore.java23.books.model.enums.CatalogSortOption;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogTest {


    @Test
    void addItem() {
        Catalog catalog = new Catalog();
        catalog.addBook(testItems[0]);

        assertEquals(1, catalog.getBooksCount());
        catalog.addBook(testItems[0]);
        assertEquals(1, catalog.getBooksCount());
        catalog.addBook(testItems[1]);
        assertEquals(2, catalog.getBooksCount());
    }

    @Test
    void getItem() {
        Catalog catalog = new Catalog();
        catalog.addBook(testItems[1]);
        catalog.addBook(testItems[0]);
        catalog.addBook(testItems[2]);

        testHasItem(testItems[0], catalog);
        testHasItem(testItems[1], catalog);
        testHasItem(testItems[2], catalog);
    }

    private void testHasItem(Book expectedItem, Catalog catalog) {
        var result = catalog.getBook(expectedItem.getId());
        assertTrue(result.isPresent());
        assertEquals(expectedItem, result.get());
    }

    @Test
    void getSorted() {
        Catalog catalog = new Catalog();
        for (Book item : testItems) {
            catalog.addBook(item);
        }

        var result = catalog.getSorted(CatalogSortOption.TITLE, false, 0, 10);
        assertItems(testItems, result);

        result = catalog.getSorted(CatalogSortOption.TITLE, true, 0, 10);
        assertItems(new Book[]{testItems[3], testItems[2], testItems[1], testItems[0]}, result);

        result = catalog.getSorted(CatalogSortOption.TITLE, false, 0, 3);
        assertItems(new Book[]{testItems[0], testItems[1], testItems[2]}, result);

        result = catalog.getSorted(CatalogSortOption.TITLE, false, 1, 3);
        assertItems(new Book[]{testItems[3]}, result);

        result = catalog.getSorted(CatalogSortOption.PRICE, false, 0, 10);
        assertItems(new Book[]{testItems[1], testItems[2], testItems[0], testItems[3]}, result);
    }

    private void assertItems(Book[] expectedItems, Collection<Book> result) {
        assertEquals(expectedItems.length, result.size(), "Wrong length");
        for (Book item : expectedItems) {
            assertTrue(result.contains(item), "Item missed " + item.getId());
        }
    }

    public static Set<Genre> getGenres1() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(1, "Фэнтези"));
        genres.add(new Genre(2, "Триллер"));
        genres.add(new Genre(3, "Романтика"));
        genres.add(new Genre(4, "Научная фантастика"));
        genres.add(new Genre(5, "Мистика"));
        genres.add(new Genre(6, "Хоррор"));

        return genres;
    }

    public static Set<Genre> getGenres2() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(7, "Комедия"));
        genres.add(new Genre(8, "Драма"));
        genres.add(new Genre(9, "Приключения"));
        genres.add(new Genre(10, "Исторический роман"));

        return genres;
    }

    public static Set<Author> getAuthors1() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(1L, "John", "Doe", "Michael", "JD"));
        authors.add(new Author(2L, "Jane", "Doe", "Alice", "JDoe"));
        authors.add(new Author(3L, "Alex", "Smith"));
        authors.add(new Author(4L, "Emily", "Johnson"));

        return authors;
    }

    public static Set<Author> getAuthors2() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(5L, "Adam", "Smith", "David", "AS"));
        authors.add(new Author(6L, "Eva", "Brown", "Sophie", "EB"));
        authors.add(new Author(7L, "Michael", "Jackson"));
        authors.add(new Author(8L, "Jennifer", "Lopez"));

        return authors;
    }

    public static Book[] generateTestBooks() {
        Set<Genre> genres1 = getGenres1();
        Set<Genre> genres2 = getGenres2();
        Set<Author> authors1 = getAuthors1();
        Set<Author> authors2 = getAuthors2();

        Book book1 = Book.load(1, "Book 1", BookStatus.OPEN, new BigDecimal("99.99"),
                "Description 1", "Publisher A", 2000, genres1,
                authors1);
        Book book2 = Book.load(2, "Book 2", BookStatus.CLOSED, new BigDecimal("29.99"),
                "Description 2", "Publisher B", 2010, genres2, authors2);
        Book book3 = Book.load(3, "Book 3", BookStatus.HIDDEN, new BigDecimal("50.05"),
                "Description 3", "Publisher C", 2020, genres1, authors2);
        Book book4 = Book.load(4, "Book 3", BookStatus.HIDDEN, new BigDecimal("50.05"),
                "Description 3", "Publisher C", 2020, genres1, authors2);

        return new Book[]{book1, book2, book3, book4};
    }

    Book[] testItems = generateTestBooks();
}
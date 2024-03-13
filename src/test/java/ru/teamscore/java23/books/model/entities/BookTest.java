package ru.teamscore.java23.books.model.entities;

import org.junit.jupiter.api.Test;
import ru.teamscore.java23.books.model.enums.BookStatus;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class BookTest {

    @Test
    public void testConstructorAndGetters() {
        Set<Genre> genres = new HashSet<>();
        Set<Author> authors = new HashSet<>();

        Book book = Book.load(1, null, null, BookStatus.CLOSED, BigDecimal.valueOf(999),
                "Penguin Books", 1925, genres, authors, "default.png");

        assertEquals(1, book.getId());
        assertNull(book.getTitle());
        assertEquals(BookStatus.CLOSED, book.getStatus());
        assertEquals(BigDecimal.valueOf(999), book.getPrice());
        assertNull(book.getDescription());
        assertEquals("Penguin Books", book.getPublisher());
        assertEquals(1925, book.getYear());
        assertEquals(genres, book.getGenres());
        assertEquals(authors, book.getAuthors());
    }

    @Test
    public void testAllArgsConstructor() {
        Set<Genre> genres = new HashSet<>();
        Set<Author> authors = new HashSet<>();
        BigDecimal price = BigDecimal.valueOf(20.99);

        Book book = Book.load(1, "The Great Gatsby", "A classic novel", BookStatus.OPEN, price,
                "Penguin Books", 1925, genres, authors, "default.png");

        assertEquals(1, book.getId());
        assertEquals("The Great Gatsby", book.getTitle());
        assertEquals(BookStatus.OPEN, book.getStatus());
        assertEquals(price, book.getPrice());
        assertEquals("A classic novel", book.getDescription());
        assertEquals("Penguin Books", book.getPublisher());
        assertEquals(1925, book.getYear());
        assertEquals(genres, book.getGenres());
        assertEquals(authors, book.getAuthors());
    }

    @Test
    public void testEqualsAndHashCode() {
        Set<Genre> genres1 = new HashSet<>();
        genres1.add(new Genre(1, "Fantasy", new HashSet<>()));
        Set<Author> authors1 = new HashSet<>();
        authors1.add(new Author(1, "J.R.R.", "Tolkien",
                null, null, new HashSet<>()));

        Book book1 = Book.load(1, "The Hobbit", "Houghton Mifflin Harcourt",
                BookStatus.CLOSED, BigDecimal.valueOf(999), null,
                1937, genres1,
                authors1, "default.png");


        Set<Genre> genres2 = new HashSet<>();
        genres2.add(new Genre(1, "Fantasy", new HashSet<>()));
        Set<Author> authors2 = new HashSet<>();
        authors2.add(new Author(1, "J.R.R.", "Tolkien", null, null, new HashSet<>()));

        Book book2 = Book.load(1, "The Hobbit", "Houghton Mifflin Harcourt", BookStatus.CLOSED,
                BigDecimal.valueOf(999), null, 1937, genres2, authors2, "default.png");

        assertEquals(book1, book2);
        assertEquals(book1.hashCode(), book2.hashCode());

        Book book3 = Book.load(2, "The Lord of the Rings", "George Allen & Unwin",
                BookStatus.CLOSED, BigDecimal.valueOf(999),
                null, 1954, genres1, authors1, "default.png");
        assertNotEquals(book1, book3);
        assertNotEquals(book1.hashCode(), book3.hashCode());
    }


    @Test
    public void testSetters() {
        Set<Genre> genres = new HashSet<>();
        Set<Author> authors = new HashSet<>();

        Book book = Book.load(1, "To Kill a Mockingbird", null, BookStatus.CLOSED, BigDecimal.TEN,
                "J. B. Lippincott & Co.", 1960, genres, authors, "default.png");

        BigDecimal price = BigDecimal.valueOf(15.99);
        book.setPrice(price);
        assertEquals(price, book.getPrice());

        book.setDescription("A novel by Harper Lee");
        assertEquals("A novel by Harper Lee", book.getDescription());

        book.open();
        assertEquals(BookStatus.OPEN, book.getStatus());

        book.close();
        assertEquals(BookStatus.CLOSED, book.getStatus());

        book.hide();
        assertEquals(BookStatus.HIDDEN, book.getStatus());
    }

    @Test
    public void testFilledAuthorsAndGenresLists() {
        Set<Genre> genres = new HashSet<>();
        genres.add(new Genre(1, "Fantasy", new HashSet<>()));
        genres.add(new Genre(2, "Adventure", new HashSet<>()));

        Set<Author> authors = new HashSet<>();
        authors.add(new Author(1, "J.R.R.", "Tolkien", null, null, new HashSet<>()));
        authors.add(new Author(2, "George", "Martin", null, null, new HashSet<>()));

        // Создаем объект Book с этими списками
        Book book = Book.load(1,null,"Houghton Mifflin Harcourt",
                BookStatus.CLOSED, BigDecimal.valueOf(999), null,  1937,
                genres, authors,
                "default.png");

        // Проверяем, что списки авторов и жанров корректно заполнены
        assertEquals(genres, book.getGenres());
        assertEquals(authors, book.getAuthors());
    }
}
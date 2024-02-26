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

        Book book = new Book(1, BigDecimal.valueOf(999),"Penguin Books", 1925, genres, authors);

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

        Book book = Book.load(1, "The Great Gatsby", BookStatus.OPEN, price,
                "A classic novel", "Penguin Books", 1925, genres, authors);

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
        genres1.add(new Genre(1, "Fantasy"));
        Set<Author> authors1 = new HashSet<>();
        authors1.add(new Author(1, "J.R.R.", "Tolkien"));

        Book book1 = Book.load(1, "The Hobbit", BookStatus.CLOSED, BigDecimal.valueOf(999), null,
                "Houghton Mifflin Harcourt", 1937, genres1,
                authors1);


        Set<Genre> genres2 = new HashSet<>();
        genres2.add(new Genre(1, "Fantasy"));
        Set<Author> authors2 = new HashSet<>();
        authors2.add(new Author(1, "J.R.R.", "Tolkien"));

        Book book2 = Book.load(1, "The Hobbit", BookStatus.CLOSED, BigDecimal.valueOf(999), null,
                "Houghton Mifflin Harcourt", 1937, genres2, authors2);

        assertEquals(book1, book2);
        assertEquals(book1.hashCode(), book2.hashCode());

        Book book3 = Book.load(2, "The Lord of the Rings", BookStatus.CLOSED, BigDecimal.valueOf(999),
                null, "George Allen & Unwin", 1954, genres1, authors1);
        assertNotEquals(book1, book3);
        assertNotEquals(book1.hashCode(), book3.hashCode());
    }


    @Test
    public void testSetters() {
        Set<Genre> genres = new HashSet<>();
        Set<Author> authors = new HashSet<>();

        Book book = Book.load(1, "To Kill a Mockingbird", BookStatus.CLOSED, BigDecimal.TEN, null,
                "J. B. Lippincott & Co.", 1960, genres, authors);

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
        genres.add(new Genre(1, "Fantasy"));
        genres.add(new Genre(2, "Adventure"));

        Set<Author> authors = new HashSet<>();
        authors.add(new Author(1, "J.R.R.", "Tolkien"));
        authors.add(new Author(2, "George", "Martin"));

        // Создаем объект Book с этими списками
        Book book = new Book(1,  BigDecimal.valueOf(999),"Houghton Mifflin Harcourt", 1937, genres, authors);

        // Проверяем, что списки авторов и жанров корректно заполнены
        assertEquals(genres, book.getGenres());
        assertEquals(authors, book.getAuthors());
    }
}
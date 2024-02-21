package ru.teamscore.java23.books.model.entities;

import org.junit.jupiter.api.Test;
import ru.teamscore.java23.books.model.enums.BookStatus;
import ru.teamscore.java23.books.model.enums.OrderStatus;
import ru.teamscore.java23.books.model.exceptions.OrderSetStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
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

        return new Book[]{book1, book2, book3};
    }

    Book[] books = generateTestBooks();
    Order.OrderBook[] testOrderBooks = new Order.OrderBook[]{
            Order.OrderBook.load(books[0], 1, books[0].getPrice()),
            Order.OrderBook.load(books[1], 100, BigDecimal.TEN),
            Order.OrderBook.load(books[2], 9, books[2].getPrice())
    };

    private Order testGetOrderWithBooks() {
        return Order.load(1,
                LocalDateTime.now(),
                new Customer(1L, "firstNameCustomer", "lastNameCustomer"),
                OrderStatus.PROCESSING,
                new ArrayList<>(Arrays.stream(testOrderBooks).toList())
        );
    }

    @Test
    void close() {
        Order order = testGetOrderWithBooks();
        assertEquals(OrderStatus.PROCESSING, order.getStatus());

        order.close();
        assertEquals(OrderStatus.CLOSED, order.getStatus());
        order.close();
        assertEquals(OrderStatus.CLOSED, order.getStatus());

        OrderSetStatusException ex = assertThrows(OrderSetStatusException.class, order::cancel);
        assertEquals(OrderStatus.CLOSED, ex.getOldStatus());
        assertEquals(OrderStatus.CANCELED, ex.getNewStatus());
    }

    @Test
    void cancel() {
        Order order = testGetOrderWithBooks();
        assertEquals(OrderStatus.PROCESSING, order.getStatus());
        order.cancel();
        assertEquals(OrderStatus.CANCELED, order.getStatus());
        order.cancel();
        assertEquals(OrderStatus.CANCELED, order.getStatus());
        OrderSetStatusException ex = assertThrows(OrderSetStatusException.class, order::close);
        assertEquals(OrderStatus.CANCELED, ex.getOldStatus());
        assertEquals(OrderStatus.CLOSED, ex.getNewStatus());
    }

    @Test
    void getBooks() {
        Order order = testGetOrderWithBooks();

        var booksAll = order.getBooks();
        assertEquals(testOrderBooks.length, booksAll.size());
        for (var book : booksAll) {
            assertTrue(Arrays.stream(testOrderBooks).anyMatch((oi) ->
                    oi.getBook() == book.getBook() &&
                            oi.getQuantity() == book.getQuantity() &&
                            oi.getPrice().equals(book.getPrice())
            ));
        }
    }

    @Test
    void getOrderBookAmount() {
        assertEquals("99.99", testOrderBooks[0].getAmount().toPlainString());
        assertEquals("1000.00", testOrderBooks[1].getAmount().toPlainString());
        String a = testOrderBooks[2].getAmount().toPlainString();
        BigDecimal ab = testOrderBooks[2].getAmount();
        Order.OrderBook b = testOrderBooks[2];
        assertEquals("450.45", testOrderBooks[2].getAmount().toPlainString());
    }

    @Test
    void getTotalAmount() {
        Order order = testGetOrderWithBooks();
        assertEquals("1550.44", order.getTotalAmount().toPlainString());
    }

    @Test
    void getBooksCount() {
        Order order = testGetOrderWithBooks();
        assertEquals(testOrderBooks.length, order.getBooksCount());
    }

    @Test
    void getBook() {
        Order order = testGetOrderWithBooks();
        long id = testOrderBooks[1].getBook().getId();

        var orderBook = order.getBook(id);
        assertTrue(orderBook.isPresent());
        assertEquals(testOrderBooks[1], orderBook.get());
    }

    @Test
    void addBook() {
        Order order = testGetOrderWithBooks();

        Book bookToAdd = Book.load(4, "Book 3", BookStatus.HIDDEN, new BigDecimal("50.05"),
                "Description 3", "Publisher C", 2020, new HashSet<>(), new HashSet<>());

        Order.OrderBook addedBook = order.addBook(bookToAdd, 2);

        assertEquals(testOrderBooks.length + 1, order.getBooks().size());
        assertEquals(2, addedBook.getQuantity());
        addedBook = order.addBook(bookToAdd);
        assertEquals(testOrderBooks.length + 1, order.getBooks().size());
        assertEquals(3, addedBook.getQuantity());
    }

    @Test
    void removeBook() {
        Order order = testGetOrderWithBooks();

        Book bookToRemove = books[0];

        var removedBook = order.removeBook(bookToRemove.getId());

        assertEquals(testOrderBooks.length - 1, order.getBooks().size());
        assertTrue(removedBook.isPresent());
        assertEquals(bookToRemove, removedBook.get().getBook());
    }
}
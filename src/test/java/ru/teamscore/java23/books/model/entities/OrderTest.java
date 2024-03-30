package ru.teamscore.java23.books.model.entities;

import org.junit.jupiter.api.Test;
import ru.teamscore.java23.books.model.enums.BookStatus;
import ru.teamscore.java23.books.model.enums.OrderStatus;
import ru.teamscore.java23.books.model.exceptions.OrderSetStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTest {
    public static Set<Genre> getGenres1() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(1, "Фэнтези", new HashSet<>()));
        genres.add(new Genre(2, "Триллер", new HashSet<>()));
        genres.add(new Genre(3, "Романтика", new HashSet<>()));
        genres.add(new Genre(4, "Научная фантастика", new HashSet<>()));
        genres.add(new Genre(5, "Мистика", new HashSet<>()));
        genres.add(new Genre(6, "Хоррор", new HashSet<>()));

        return genres;
    }

    public static Set<Genre> getGenres2() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(7, "Комедия", new HashSet<>()));
        genres.add(new Genre(8, "Драма", new HashSet<>()));
        genres.add(new Genre(9, "Приключения", new HashSet<>()));
        genres.add(new Genre(10, "Исторический роман", new HashSet<>()));

        return genres;
    }

    public static Set<Author> getAuthors1() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(1L, "John", "Doe", "Michael", "JD", new HashSet<>()));
        authors.add(new Author(2L, "Jane", "Doe", "Alice", "JDoe", new HashSet<>()));
        authors.add(new Author(3L, "Alex", "Smith", null, null, new HashSet<>()));
        authors.add(new Author(4L, "Emily", "Johnson", null, null, new HashSet<>()));

        return authors;
    }

    public static Set<Author> getAuthors2() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(5L, "Adam", "Smith", "David", "AS", new HashSet<>()));
        authors.add(new Author(6L, "Eva", "Brown", "Sophie", "EB", new HashSet<>()));
        authors.add(new Author(7L, "Michael", "Jackson", null, null, new HashSet<>()));
        authors.add(new Author(8L, "Jennifer", "Lopez", null, null, new HashSet<>()));

        return authors;
    }

    public static Book[] generateTestBooks() {
        Set<Genre> genres1 = getGenres1();
        Set<Genre> genres2 = getGenres2();
        Set<Author> authors1 = getAuthors1();
        Set<Author> authors2 = getAuthors2();

        Book book1 = Book.load(1, "Book 1", "Description 1", BookStatus.OPEN, new BigDecimal("99.99"),
                "Publisher A", 2000, genres1,
                authors1, "default.png");
        Book book2 = Book.load(2, "Book 2", "Description 2", BookStatus.CLOSED, new BigDecimal("29.99"),
                "Publisher B", 2010, genres2, authors2, "default.png");
        Book book3 = Book.load(3, "Book 3", "Description 3", BookStatus.HIDDEN, new BigDecimal("50.05"),
                "Publisher C", 2020, genres1, authors2, "default.png");

        return new Book[]{book1, book2, book3};
    }

    Book[] books = generateTestBooks();
    OrdersBooks[] testOrdersBooks = new OrdersBooks[]{
            new OrdersBooks(books[0], new Order(), 10),
            new OrdersBooks(books[1], new Order(), 1),
            new OrdersBooks(books[2], new Order(), 5)
    };

    {
        testOrdersBooks[0].setPrice(BigDecimal.ONE);
        testOrdersBooks[1].setPrice(BigDecimal.ONE);
        testOrdersBooks[2].setPrice(BigDecimal.TEN);
    }

    private Order testGetOrderWithBooks() {
        return Order.load(1,
                LocalDateTime.now(),
                new Customer(1L, "firstNameCustomer", "lastNameCustomer", null,
                        null, new HashSet<>()),
                OrderStatus.PROCESSING,
                new ArrayList<>(Arrays.stream(testOrdersBooks).toList())
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
        assertEquals(testOrdersBooks.length, booksAll.size());
        for (var book : booksAll) {
            assertTrue(Arrays.stream(testOrdersBooks).anyMatch((oi) ->
                    oi.getBook() == book.getBook() &&
                            oi.getQuantity() == book.getQuantity() &&
                            oi.getPrice().equals(book.getPrice())
            ));
        }
    }

    @Test
    void getOrderBookAmount() {
        assertEquals("10.00", testOrdersBooks[0].getAmount().toPlainString());
        assertEquals("1.00", testOrdersBooks[1].getAmount().toPlainString());
        String a = testOrdersBooks[2].getAmount().toPlainString();
        BigDecimal ab = testOrdersBooks[2].getAmount();
        OrdersBooks b = testOrdersBooks[2];
        assertEquals("50.00", testOrdersBooks[2].getAmount().toPlainString());
    }

    @Test
    void getTotalAmount() {
        Order order = testGetOrderWithBooks();
        assertEquals("61.00", order.getTotalAmount().toPlainString());
    }

    @Test
    void getBooksCount() {
        Order order = testGetOrderWithBooks();
        assertEquals(testOrdersBooks.length, order.getBooksCount());
    }

    @Test
    void getBook() {
        Order order = testGetOrderWithBooks();
        long id = testOrdersBooks[1].getBook().getId();

        var orderBook = order.getBook(id);
        assertTrue(orderBook.isPresent());
        assertEquals(testOrdersBooks[1], orderBook.get());
    }

    @Test
    void addBook() {
        Order order = testGetOrderWithBooks();

        Book bookToAdd = Book.load(4, "Book 3", "Description 3", BookStatus.HIDDEN, new BigDecimal("50.05"),
                "Publisher C", 2020, new HashSet<>(), new HashSet<>(), "default.png");

        OrdersBooks addedBook = order.addBook(bookToAdd, 2);

        assertEquals(testOrdersBooks.length + 1, order.getBooks().size());
        assertEquals(2, addedBook.getQuantity());
        addedBook = order.addBook(bookToAdd);
        assertEquals(testOrdersBooks.length + 1, order.getBooks().size());
        assertEquals(3, addedBook.getQuantity());
    }

    @Test
    void removeBook() {
        Order order = testGetOrderWithBooks();

        Book bookToRemove = books[0];

        var removedBook = order.removeBook(bookToRemove.getId());

        assertEquals(testOrdersBooks.length - 1, order.getBooks().size());
        assertTrue(removedBook.isPresent());
        assertEquals(bookToRemove, removedBook.get().getBook());
    }
}
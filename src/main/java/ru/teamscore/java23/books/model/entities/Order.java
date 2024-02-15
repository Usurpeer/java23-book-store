package ru.teamscore.java23.books.model.entities;

import lombok.*;
import ru.teamscore.java23.books.model.enums.OrderStatus;
import ru.teamscore.java23.books.model.exceptions.OrderSetStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Data // toString, equals, hashcode, get/set (при чем set не создается для final полей)
@EqualsAndHashCode(of = "id") // только по полю id
@AllArgsConstructor(staticName = "load")
public class Order {

    private final long id;

    private final LocalDateTime created; // дата время заказа

    private final Customer customer;

    private OrderStatus status = OrderStatus.PROCESSING;

    private final List<OrderBook> books;

    public Order(int id, Customer customer) {
        this.id = id;
        created = LocalDateTime.now();
        books = new ArrayList<>();
        this.customer = customer;
    }

    public BigDecimal getTotalAmount() {
        return books.stream().map(OrderBook::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public int getTotalQuantity() {
        return books.stream()
                .map(OrderBook::getQuantity)
                .reduce(0, Integer::sum);
    }

    // копия для предотвращения изменений
    public Collection<OrderBook> getBooks() {
        return books.stream().toList();
    }

    public int getBooksCount() {
        return books.size();
    }

    public Optional<OrderBook> getBook(long id) {
        return books.stream()
                .filter(i -> i.getBook().getId() == id)
                .findFirst();
    }

    public Optional<OrderBook> getBook(Book book) {
        return books.stream()
                .filter(i -> i.getBook().equals(book))
                .findFirst();
    }

    public OrderBook addBook(@NonNull Book book, int quantity) {
        // если такая книга уже есть
        Optional<Order.OrderBook> existingBook = getBook(book.getId());
        if (existingBook.isPresent()) {
            existingBook.get().addQuantity(quantity);
            return existingBook.get();
        }

        // новая книга
        OrderBook orderBook = new OrderBook(book, quantity);
        books.add(orderBook);
        return orderBook;
    }

    public OrderBook addBook(@NonNull Book book) {
        return addBook(book, 1);
    }

    public Optional<OrderBook> removeBook(long id){
        Optional<Order.OrderBook> existingBook = getBook(id);
        // если такая книга есть
        if (existingBook.isPresent()) {
            books.remove(existingBook.get());
        }
        return existingBook;
    }
    public Optional<OrderBook> removeBook(Book book){
        Optional<Order.OrderBook> existingBook = getBook(book);
        // если такая книга есть
        if (existingBook.isPresent()) {
            books.remove(existingBook.get());
        }
        return existingBook;
    }
    public void close() {
        if (status == OrderStatus.CANCELED) {
            throw new OrderSetStatusException("Нельзя завершить отмененный заказ", status, OrderStatus.CLOSED);
        }
        status = OrderStatus.CLOSED;
    }

    // Чтобы отменить заказ, нужно чтобы он был в работе. Я так это понимаю,
    // То есть заказ в процессе - он либо завершен и всё, либо он "Отменен" - его можно завершить
    public void cancel() {
        if (status == OrderStatus.PROCESSING) {
            status = OrderStatus.CANCELED;
        }
        throw new OrderSetStatusException("Отменить можно только заказ, который в работе.", status,
                OrderStatus.CANCELED);
    }

    @Getter
    @Setter
    @AllArgsConstructor(staticName = "load")
    public static class OrderBook {
        private final Book book;
        private int quantity = 1;
        private final BigDecimal price;

        public BigDecimal getAmount() {
            return price.multiply(new BigDecimal(quantity))
                    .setScale(2, RoundingMode.HALF_UP);
        }

        public int addQuantity(int quantity) {
            this.quantity += quantity;
            return this.quantity;
        }

        public OrderBook(@NonNull Book book, int quantity) {
            this.book = book;
            this.quantity = quantity;
            this.price = book.getPrice();
        }
    }
}

package ru.teamscore.java23.books.model.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import ru.teamscore.java23.books.model.enums.OrderStatus;
import ru.teamscore.java23.books.model.exceptions.OrderSetStatusException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;


@NoArgsConstructor
@AllArgsConstructor(staticName = "load") // все поля

@Entity
@Table(name = "order", schema = "orders")
public class Order {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDateTime created = LocalDateTime.now(); // дата время заказа

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Enumerated
    @Column(nullable = false, length = 15)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private OrderStatus status = OrderStatus.PROCESSING;

    @OneToMany(mappedBy = "pk.order", cascade = CascadeType.ALL)
    private List<OrderBook> books = new ArrayList<>();

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

    public Optional<OrderBook> getBook(@NonNull Book book) {
        return books.stream()
                .filter(i -> i.getBook().equals(book))
                .findFirst();
    }

    public OrderBook addBook(@NonNull Book book, int quantity) {
        // если такая книга уже есть
        Optional<OrderBook> existingBook = getBook(book.getId());
        if (existingBook.isPresent()) {
            existingBook.get().addQuantity(quantity);
            return existingBook.get();
        }

        // новая книга
        OrderBook orderBook = new OrderBook(book, this, quantity);
        books.add(orderBook);
        return orderBook;
    }

    public OrderBook addBook(@NonNull Book book) {
        return addBook(book, 1);
    }

    public Optional<OrderBook> removeBook(long id) {
        Optional<OrderBook> existingBook = getBook(id);
        // если такая книга есть
        if (existingBook.isPresent()) {
            books.remove(existingBook.get());
        }
        return existingBook;
    }

    public Optional<OrderBook> removeBook(Book book) {
        Optional<OrderBook> existingBook = getBook(book);
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

    public void cancel() {
        if (status == OrderStatus.CLOSED) {
            throw new OrderSetStatusException("Нельзя отменить закрытый заказ", status, OrderStatus.CANCELED);
        }
        status = OrderStatus.CANCELED;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return Objects.equals(id, order.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

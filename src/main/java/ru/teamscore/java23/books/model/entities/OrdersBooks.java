package ru.teamscore.java23.books.model.entities;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;

@NoArgsConstructor
@Entity
@Table(name = "order_book", schema = "orders")
public class OrdersBooks {

    @Setter
    @Getter
    @Embeddable
    static class OrderBookPK {
        @JsonBackReference
        @ManyToOne
        @JoinColumn(name = "book_id")
        private Book book;


        @JsonManagedReference
        @ManyToOne
        @JoinColumn(name = "order_id")
        private Order order;
    }

    @Getter
    @EmbeddedId
    private final OrderBookPK pk = new OrderBookPK();

    public OrdersBooks(@NonNull Book book, @NonNull Order order, int quantity) {
        pk.book = book;
        pk.order = order;
        this.price = book.getPrice();
        this.quantity = quantity;
    }

    @NonNull
    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "decimal(10,2)")
    private BigDecimal price;

    @Getter
    @Setter
    @Column(nullable = false)
    private int quantity = 1;

    @Transient // не сериализуется тк ключ
    public Book getBook() {
        return pk.getBook();
    }

    @Transient
    public Order getOrder() {
        return pk.getOrder();
    }

    public BigDecimal getAmount() {
        return price.multiply(new BigDecimal(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public int addQuantity(int quantity) {
        this.quantity += quantity;
        return this.quantity;
    }
}

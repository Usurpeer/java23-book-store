package ru.teamscore.java23.books.model.entities;


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
public class OrderBook {
    @Setter
    @Getter
    @Embeddable
    static class OrderBookPK {
        @ManyToOne
        @JoinColumn(name = "book_id")
        private Book book;

        @ManyToOne
        @JoinColumn(name = "order_id")
        private Order order;
    }

    @EmbeddedId
    private final OrderBookPK pk = new OrderBookPK();

    public OrderBook(@NonNull Book book, @NonNull Order order, int quantity) {
        pk.book = book;
        pk.order = order;
        this.price = book.getPrice();
        this.quantity = quantity;
    }

    @Transient // не сериализуется тк ключ
    public Book getBook(){
        return pk.getBook();
    }
    @Transient
    public Order getOrder(){
        return pk.getOrder();
    }

    @Getter
    @Setter
    @Column(nullable = false, columnDefinition = "decimal(10,2)")
    private BigDecimal price;

    @Getter
    @Setter
    @Column(nullable = false)
    private int quantity = 1;

    public BigDecimal getAmount() {
        return price.multiply(new BigDecimal(quantity))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public int addQuantity(int quantity) {
        this.quantity += quantity;
        return this.quantity;
    }
}

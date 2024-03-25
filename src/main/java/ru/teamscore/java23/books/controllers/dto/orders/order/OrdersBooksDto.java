package ru.teamscore.java23.books.controllers.dto.orders.order;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrdersBooksDto {
    private BookInOrderDto book;
    private BigDecimal price;
    private long quantity;
}

package ru.teamscore.java23.books.controllers.dto.orders.order;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
public class OrderDto {
    private Set<OrdersBooksDto> books;
    private BigDecimal totalAmount;
}

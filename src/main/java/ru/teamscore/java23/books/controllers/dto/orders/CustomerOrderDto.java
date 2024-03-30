package ru.teamscore.java23.books.controllers.dto.orders;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CustomerOrderDto {
    private long id;
    private LocalDate created;
    private BigDecimal amount;
    private long quantityBooks;
    private String status;
}

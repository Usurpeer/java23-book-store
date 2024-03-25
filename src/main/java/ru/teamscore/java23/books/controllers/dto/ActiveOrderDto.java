package ru.teamscore.java23.books.controllers.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ActiveOrderDto {
    private long id;
    private LocalDateTime created;
    private BigDecimal amount;
    private long quantityBooks;
}

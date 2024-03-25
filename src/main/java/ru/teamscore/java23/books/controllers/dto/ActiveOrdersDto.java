package ru.teamscore.java23.books.controllers.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
public class ActiveOrdersDto {
    private List<ActiveOrderDto> orders;
    private BigDecimal totalAmount;
}

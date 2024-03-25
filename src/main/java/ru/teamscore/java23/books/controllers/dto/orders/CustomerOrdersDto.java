package ru.teamscore.java23.books.controllers.dto.orders;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@AllArgsConstructor
public class CustomerOrdersDto {
    private Set<CustomerOrderDto> orders;
    private BigDecimal totalAmount;
}

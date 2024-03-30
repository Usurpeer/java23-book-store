package ru.teamscore.java23.books.controllers.dto.cart;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartBookDto {
    @Min(1)
    private long quantity;
    private Long id;
}

package ru.teamscore.java23.books.controllers.dto.cart;

import lombok.Data;

import java.util.List;

@Data
public class CartDto {
    private List<CartBookDto> books;
}

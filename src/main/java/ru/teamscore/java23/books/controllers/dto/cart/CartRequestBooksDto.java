package ru.teamscore.java23.books.controllers.dto.cart;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CartRequestBooksDto {
    private List<CartRequestBookDto> books;
}

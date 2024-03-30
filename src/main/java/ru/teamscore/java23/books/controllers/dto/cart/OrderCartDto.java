package ru.teamscore.java23.books.controllers.dto.cart;

import lombok.Data;

import java.util.List;

@Data
public class OrderCartDto {
    private List<CartBookDto> books;
    private String login;
}

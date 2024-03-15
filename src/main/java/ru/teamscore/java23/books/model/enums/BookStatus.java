package ru.teamscore.java23.books.model.enums;

import lombok.Getter;

@Getter
public enum BookStatus {
    OPEN("Доступно"),
    CLOSED("Нет в наличии"),
    HIDDEN("Скрыто");

    private final String available;

    BookStatus(String available) {
        this.available = available;
    }
}

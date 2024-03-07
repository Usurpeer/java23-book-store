package ru.teamscore.java23.books.model.enums;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PROCESSING("В работе"),
    CLOSED("Выполнен"),
    CANCELED("Отменён");

    private final String title;

    OrderStatus(String title) {
        this.title = title;
    }
}

package ru.teamscore.java23.books.model.enums;

import lombok.Getter;

public enum OrderStatus {
    PROCESSING("В работе"),
    CLOSED("Выполнен"),
    CANCELED("Отменён");

    @Getter
    private final String title;

    OrderStatus(String title) {
        this.title = title;
    }
}

package ru.teamscore.java23.books.model.enums;

import lombok.Getter;

public enum BookStatus {
    OPEN("Доступно"),
    CLOSED("Нет в наличии"),
    HIDDEN("Скрыто");

    @Getter
    private String avaliable;

    BookStatus(String avaliable) {
        this.avaliable = avaliable;
    }
}

package ru.teamscore.java23.books.model.exceptions;

import lombok.Getter;
import ru.teamscore.java23.books.model.enums.OrderStatus;

public class OrderSetStatusException extends RuntimeException {
    @Getter
    private OrderStatus oldStatus;
    @Getter
    private OrderStatus newStatus;

    public OrderSetStatusException(String message, OrderStatus oldStatus, OrderStatus newStatus) {
        super(message);
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}

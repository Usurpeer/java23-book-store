package ru.teamscore.java23.books.model;

import lombok.RequiredArgsConstructor;
import ru.teamscore.java23.books.model.entities.Order;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class OrdersManager {
    private final List<Order> orders = new ArrayList<>();

    public int getOrdersCount() {
        return orders.size();
    }

    public Order[] getOrdersAll() {
        return orders.toArray(Order[]::new);
    }

    public Optional<Order> getOrder(long id) {
        return orders.stream()
                .filter(o -> o.getId() == id)
                .findFirst();
    }

    public void addOrder(Order order) {
        if (getOrder(order.getId()).isEmpty()) {
            orders.add(order);
        }
    }

}

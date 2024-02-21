package ru.teamscore.java23.books.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.teamscore.java23.books.model.entities.Customer;
import ru.teamscore.java23.books.model.entities.Order;
import ru.teamscore.java23.books.model.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class OrdersManagerTest {
    // Набор заказов
    List<Order> orders = new ArrayList<>();

    {
        orders.add(Order.load(1,
                LocalDateTime.now(),
                new Customer(1L, "n1", "f1"),
                OrderStatus.PROCESSING,
                new ArrayList<>()
        ));
        orders.add(Order.load(2,
                LocalDateTime.now(),
                new Customer(2L, "n2", "f2"),
                OrderStatus.PROCESSING,
                new ArrayList<>()
        ));
        orders.add(Order.load(3,
                LocalDateTime.now(),
                new Customer(3L, "n3", "f3"),
                OrderStatus.PROCESSING,
                new ArrayList<>()
        ));
    }

    OrdersManager ordersManager;

    // Для каждого теста - свой экземпляр
    @BeforeEach
    void setUp() {
        ordersManager = new OrdersManager();
        for (Order o : orders) {
            ordersManager.addOrder(o);
        }
    }

    @Test
    void getOrdersCount() {
        assertEquals(orders.size(), ordersManager.getOrdersCount());
    }

    @Test
    void getOrdersAll() {
        var allOrders = ordersManager.getOrdersAll();
        assertEquals(orders.size(), allOrders.length);

        for (Order order : orders) {
            assertTrue(Arrays.asList(allOrders).contains(order));
        }
    }

    @Test
    void getOrder() {
        for (Order order : orders) {
            assertTrue(ordersManager.getOrder(order.getId()).isPresent());
            assertEquals(order, ordersManager.getOrder(order.getId()).get());
        }
    }
}
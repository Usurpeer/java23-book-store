package ru.teamscore.java23.books.controllers.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.teamscore.java23.books.controllers.dto.orders.CustomerOrderDto;
import ru.teamscore.java23.books.controllers.dto.orders.CustomerOrdersDto;
import ru.teamscore.java23.books.controllers.dto.orders.order.OrderDto;
import ru.teamscore.java23.books.controllers.dto.orders.order.OrdersBooksDto;
import ru.teamscore.java23.books.model.OrdersManager;
import ru.teamscore.java23.books.model.entities.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrdersController {
    private final OrdersManager ordersManager;

    private final ModelMapper modelMapper;

    @Autowired
    public OrdersController(OrdersManager ordersManager, ModelMapper modelMapper) {
        this.ordersManager = ordersManager;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<CustomerOrdersDto> getOrdersById(@RequestParam String login) {
        List<Order> orders = ordersManager.getActiveOrdersByCustomer(login);

        if (!orders.isEmpty()) {
            Set<CustomerOrderDto> ordersDto = orders.stream()
                    .map(this::mapOrder)
                    .collect(Collectors.toSet());

            BigDecimal totalAmount = ordersDto.stream()
                    .map(CustomerOrderDto::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            return ResponseEntity.ok(new CustomerOrdersDto(ordersDto, totalAmount));
        }
        return ResponseEntity.notFound().build();
    }


    private CustomerOrderDto mapOrder(Order order) {
        return modelMapper.map(order, CustomerOrderDto.class);
    }

    @GetMapping("/order")
    public ResponseEntity<OrderDto> getOrderById(@RequestParam long id) {
        Optional<Order> orderOptional = ordersManager.getOrder(id);

        if (orderOptional.isPresent()) {
            var order = orderOptional.get();
            Set<OrdersBooksDto> ordersBooksDto = order.getBooks().stream()
                    .map(element -> modelMapper.map(element, OrdersBooksDto.class))
                    .collect(Collectors.toSet());
            return ResponseEntity.ok(new OrderDto(ordersBooksDto, order.getTotalAmount()));
        }
        return ResponseEntity.notFound().build();
    }
}

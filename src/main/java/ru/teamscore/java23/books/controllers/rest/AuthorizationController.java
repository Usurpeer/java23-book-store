package ru.teamscore.java23.books.controllers.rest;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.teamscore.java23.books.controllers.dto.auth.CustomerDto;
import ru.teamscore.java23.books.model.OrdersManager;


@RestController
public class AuthorizationController {

    private final OrdersManager ordersManager;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthorizationController(OrdersManager ordersManager, ModelMapper modelMapper) {
        this.ordersManager = ordersManager;
        this.modelMapper = modelMapper;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginIn(@RequestBody String login) {
        CustomerDto customerDto = getCustomerIfExist(login);
        if (customerDto != null) {
            return ResponseEntity.ok(customerDto);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Пользователь не найден");
        }
    }

    private CustomerDto getCustomerIfExist(String login) {
        var customer = ordersManager.getCustomerManager().getCustomer(login);

        return customer.map(c -> modelMapper.map(c, CustomerDto.class)).orElse(null);
    }
}

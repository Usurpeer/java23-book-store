package ru.teamscore.java23.books.controllers.rest;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.teamscore.java23.books.controllers.dto.cart.*;
import ru.teamscore.java23.books.model.Catalog;
import ru.teamscore.java23.books.model.OrdersManager;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Customer;
import ru.teamscore.java23.books.model.entities.Order;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private final Catalog catalog;
    @Autowired
    private final OrdersManager ordersManager;
    @Autowired
    private final ModelMapper modelMapper;

    @PostMapping("/getBooks")
    public ResponseEntity<CartRequestBooksDto> getBooksPost(@RequestBody CartDto request) {
        var books = mapDtoToBooks(request.getBooks());

        Map<Long, Long> idToQuantityMap = request.getBooks().stream()
                .collect(Collectors.toMap(CartBookDto::getId, CartBookDto::getQuantity));

        var booksDto = books.stream()
                .map(book -> mapCatalogBook(book, idToQuantityMap))
                .toList();

        CartRequestBooksDto cartRequestBooksDto = new CartRequestBooksDto(booksDto);

        return ResponseEntity.ok(cartRequestBooksDto);
    }

    private CartRequestBookDto mapCatalogBook(Book book, Map<Long, Long> idToQuantityMap) {
        CartRequestBookDto cartRequestBookDto = modelMapper.map(book, CartRequestBookDto.class);
        cartRequestBookDto.setQuantity(idToQuantityMap.getOrDefault(book.getId(), 0L));
        return cartRequestBookDto;
    }

    private List<Book> mapDtoToBooks(List<CartBookDto> booksDto) {
        return booksDto.stream()
                .map(cartBookDto -> catalog.getBook(cartBookDto.getId()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    @PostMapping("/createOrder")
    public ResponseEntity<?> createOrder(@RequestBody OrderCartDto request) {
        List<Book> books = mapDtoToBooks(request.getBooks());

        Map<Long, Long> idToQuantityMap = request.getBooks().stream()
                .collect(Collectors.toMap(CartBookDto::getId, CartBookDto::getQuantity));

        Optional<Customer> customerOptional = ordersManager.getCustomerManager().getCustomer(request.getLogin());

        if (customerOptional.isPresent()) {
            Order newOrder = new Order();
            newOrder.setCustomer(customerOptional.get());

            for (Book book : books) {
                Long quantity = idToQuantityMap.getOrDefault(book.getId(), 1L);
                newOrder.addBook(book, quantity.intValue());
            }

            ordersManager.addOrder(newOrder);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}

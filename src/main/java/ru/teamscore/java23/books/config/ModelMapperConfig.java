package ru.teamscore.java23.books.config;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.teamscore.java23.books.controllers.dto.book.BookDto;
import ru.teamscore.java23.books.controllers.dto.cart.CartRequestBookDto;
import ru.teamscore.java23.books.controllers.dto.catalog.CatalogBookDto;
import ru.teamscore.java23.books.controllers.dto.orders.CustomerOrderDto;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Order;
import ru.teamscore.java23.books.model.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);


        // сопоставления Book с DTO классами
        TypeMap<Book, CatalogBookDto> typeMapCatalogBook = modelMapper.createTypeMap(Book.class, CatalogBookDto.class);
        typeMapCatalogBook.addMappings(mapping -> {
            mapping.map(Book::getAuthors, CatalogBookDto::setAuthors);
            mapping.map(Book::getGenres, CatalogBookDto::setGenres);
        });
        TypeMap<Book, BookDto> typeMapBook = modelMapper.createTypeMap(Book.class, BookDto.class);
        typeMapBook.addMappings(mapping -> {
            mapping.map(Book::getAuthors, BookDto::setAuthors);
            mapping.map(Book::getGenres, BookDto::setGenres);
        });
        TypeMap<Book, CartRequestBookDto> cartRequestBookDtoTypeMap = modelMapper.createTypeMap(Book.class, CartRequestBookDto.class);
        cartRequestBookDtoTypeMap.addMappings(mapping -> {
            mapping.map(Book::getAuthors, CartRequestBookDto::setAuthors);
            mapping.map(Book::getGenres, CartRequestBookDto::setGenres);
        });

        // Orders - сопоставления Order и CustomerOrderDto
        Converter<LocalDateTime, LocalDate> localDateTimeToLocalDateConverter = context ->
                context.getSource() == null ? null : context.getSource().toLocalDate();
        Converter<OrderStatus, String> orderStatusStringConverter = context ->
                context.getSource() == null ? null : context.getSource().getTitle();
        TypeMap<Order, CustomerOrderDto> typeMapActiveOrder = modelMapper.createTypeMap(Order.class, CustomerOrderDto.class);
        typeMapActiveOrder.addMappings(mapping -> {
            mapping.using(localDateTimeToLocalDateConverter).map(Order::getCreated, CustomerOrderDto::setCreated);
            mapping.using(orderStatusStringConverter).map(Order::getStatus, CustomerOrderDto::setStatus);

        });
        typeMapActiveOrder.addMapping(Order::getTotalAmount, CustomerOrderDto::setAmount);
        typeMapActiveOrder.addMapping(Order::getTotalQuantity, CustomerOrderDto::setQuantityBooks);

        modelMapper.addConverter(orderStatusStringConverter);


        // CartRequestBookDto Book
        return modelMapper;
    }
}

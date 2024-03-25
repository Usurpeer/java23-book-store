package ru.teamscore.java23.books;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.teamscore.java23.books.controllers.dto.book.BookDto;
import ru.teamscore.java23.books.controllers.dto.cart.CartRequestBookDto;
import ru.teamscore.java23.books.controllers.dto.catalog.CatalogBookDto;
import ru.teamscore.java23.books.controllers.dto.orders.CustomerOrderDto;
import ru.teamscore.java23.books.model.entities.*;
import ru.teamscore.java23.books.model.enums.OrderStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@SpringBootApplication
public class BooksApplication {
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(BooksApplication.class, args);
    }

    @Bean
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return new org.hibernate.cfg.Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Book.class)
                .addAnnotatedClass(Author.class)
                .addAnnotatedClass(Genre.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(OrdersBooks.class)
                .buildSessionFactory();
    }

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

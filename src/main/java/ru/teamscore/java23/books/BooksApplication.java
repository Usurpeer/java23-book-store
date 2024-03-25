package ru.teamscore.java23.books;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ru.teamscore.java23.books.controllers.dto.AuthorDto;
import ru.teamscore.java23.books.controllers.dto.BookDto;
import ru.teamscore.java23.books.controllers.dto.GenreDto;
import ru.teamscore.java23.books.model.entities.*;
import ru.teamscore.java23.books.model.enums.BookStatus;

@SpringBootApplication
public class BooksApplication {
    public static void main(String[] args) {
        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(BooksApplication.class, args);
    }

    @Bean
    public EntityManagerFactory entityManagerFactory() {
        return new Configuration()
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
    public EntityManager entityManager(EntityManagerFactory entityManagerFactory) {
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        Converter<String, BookStatus> statusConverter = context -> {
            return context.getSource() == null ? null : BookStatus.valueOf(context.getSource());
        };

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setMatchingStrategy(MatchingStrategies.LOOSE);

        modelMapper.addConverter(statusConverter);

        // Настройка сопоставления между классами Author и AuthorDto
        TypeMap<Author, AuthorDto> authorTypeMap = modelMapper.createTypeMap(Author.class, AuthorDto.class);
        TypeMap<Genre, GenreDto> genreTypeMap = modelMapper.createTypeMap(Genre.class, GenreDto.class);

        // Настройка сопоставления между классами Book и BookDto
        TypeMap<Book, BookDto> typeMap = modelMapper.createTypeMap(Book.class, BookDto.class);
        typeMap.addMappings(mapping -> {
            // Настройки маппинга между полями Book и BookDto
            mapping.map(Book::getAuthors, BookDto::setAuthors);
            mapping.map(Book::getGenres, BookDto::setGenres);
        });

        return modelMapper;
    }

}

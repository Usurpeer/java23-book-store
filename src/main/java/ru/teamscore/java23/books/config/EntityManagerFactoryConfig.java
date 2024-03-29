package ru.teamscore.java23.books.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.teamscore.java23.books.model.entities.*;

@Configuration
public class EntityManagerFactoryConfig {
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
}

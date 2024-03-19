package ru.teamscore.java23.books.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.teamscore.java23.books.model.entities.*;
import ru.teamscore.java23.books.model.entities.Order;
import ru.teamscore.java23.books.model.enums.OrderStatus;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrdersManagerTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private OrdersManager ordersManager;

    @BeforeAll
    public static void setup() throws IOException {
        entityManagerFactory = new Configuration()
                .configure("hibernate-postgres.cfg.xml")
                .addAnnotatedClass(Book.class)
                .addAnnotatedClass(OrdersBooks.class)
                .addAnnotatedClass(Order.class)
                .addAnnotatedClass(Customer.class)
                .addAnnotatedClass(Genre.class)
                .addAnnotatedClass(Author.class)
                .buildSessionFactory();

        SqlScripts.runFromFile(entityManagerFactory, "createSchema.sql");
    }

    @AfterAll
    public static void tearDown() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

    @BeforeEach
    public void openSession() throws IOException {
        SqlScripts.runFromFile(entityManagerFactory, "insertBooks.sql");
        SqlScripts.runFromFile(entityManagerFactory, "InsertOrders.sql");
        SqlScripts.runFromFile(entityManagerFactory, "insertAuthorAndGenre.sql");
        SqlScripts.runFromFile(entityManagerFactory, "insertLinkBooksAndGenresAuthors.sql");
        entityManager = entityManagerFactory.createEntityManager();
        ordersManager = new OrdersManager(entityManager);
    }

    @AfterEach
    public void closeSession() throws IOException {
        if (entityManager != null) {
            entityManager.close();
        }
        SqlScripts.runFromFile(entityManagerFactory, "clearOrdersData.sql");
        SqlScripts.runFromFile(entityManagerFactory, "clearCatalogData.sql");
    }

    @Test
    void getOrdersCount() {
        assertEquals(7, ordersManager.getOrdersCount());
    }

    @Test
    void getOrdersAll() throws JsonProcessingException {
        var allOrders = ordersManager.getOrdersAll();
        assertEquals(7, allOrders.length);
        for (int i = 1; i <= allOrders.length; i++) {
            int finalId = i;
            assertTrue(Arrays.stream(allOrders).anyMatch(o -> o.getId() == finalId),
                    finalId + " id is missing");
        }

        // тут вывод json
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        System.out.println(mapper.writeValueAsString(allOrders));

    }
    @Test
    void getOpenBooks() throws JsonProcessingException {
        var activeOrders = ordersManager.getActiveOrdersByCustomer(1L);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        System.out.println("12345");
        System.out.println(mapper.writeValueAsString(activeOrders));

    }
    @ParameterizedTest
    @ValueSource(longs = {1, 2, 5})
    void getOrder(long id) {
        var order = ordersManager.getOrder(id);
        assertTrue(order.isPresent());
        assertEquals(id, order.get().getId());
    }

    @Test
    void addOrder() {
        long bookId = 2;
        Catalog catalog = new Catalog(entityManager);

        var bookToAddOptional = catalog.getBook(bookId);
        assertTrue(bookToAddOptional.isPresent(), "Book to insert not found");
        var bookToAdd = bookToAddOptional.get();

        // create order
        OrdersManager order = new OrdersManager(entityManager);
        var customerToAddOptional = order.getCustomerManager().getCustomer(3);

        Order orderToAdd = new Order();
        assertTrue(customerToAddOptional.isPresent());
        orderToAdd.setCustomer(customerToAddOptional.get());
        orderToAdd.addBook(bookToAdd, 1);
        // save order
        long count = ordersManager.getOrdersCount();
        ordersManager.addOrder(orderToAdd);
        assertEquals(count + 1, orderToAdd.getId());
        // assert
        var addedOrder = ordersManager.getOrder(orderToAdd.getId());
        assertTrue(addedOrder.isPresent());
        assertEquals(orderToAdd.getCustomer().getFirstName(), addedOrder.get().getCustomer().getFirstName());
        assertEquals(orderToAdd.getStatus(), addedOrder.get().getStatus());
        var addedBook = addedOrder.get().getBook(bookToAdd.getId());
        assertTrue(addedBook.isPresent());
        assertEquals(bookToAdd.getPrice(), addedBook.get().getPrice());
        assertEquals(1, addedBook.get().getQuantity());
    }

    @Test
    void updateOrder() {
        long orderId = 1;
        long bookId = 2;
        int newQuantity = 10000;

        var existingOrder = ordersManager.getOrder(orderId);
        assertTrue(existingOrder.isPresent(), "Order with id = " + orderId + " should exist");
        Order order = existingOrder.get();
        // change item in order
        var orderBookToEdit = order.getBook(bookId);
        assertTrue(orderBookToEdit.isPresent(), "Book with id = " + bookId + " should exist in order");
        orderBookToEdit.get().setQuantity(newQuantity);
        var oldStatus = order.getStatus();
        // change order
        order.close();
        // save changes
        ordersManager.updateOrder(order);

        // reload from DB and assert changes
        var orderAfterUpdate = ordersManager.getOrder(orderId);
        assertTrue(orderAfterUpdate.isPresent(), "Order with id = " + orderId + " disappeared after update");

        var orderBookAfterUpdate = order.getBook(bookId);
        assertTrue(orderBookAfterUpdate.isPresent(), "Book with id = " + bookId + " disappeared after update");

        assertEquals(OrderStatus.CLOSED, orderAfterUpdate.get().getStatus());
        assertEquals(newQuantity, orderBookAfterUpdate.get().getQuantity());
    }
}
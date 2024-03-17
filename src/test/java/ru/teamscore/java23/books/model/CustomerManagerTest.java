package ru.teamscore.java23.books.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.teamscore.java23.books.model.entities.*;
import ru.teamscore.java23.books.model.entities.Order;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CustomerManagerTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private OrdersManager.CustomerManager customerManager;

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
        entityManager = entityManagerFactory.createEntityManager();
        OrdersManager ordersManager = new OrdersManager(entityManager);
        customerManager = ordersManager.getCustomerManager();
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
    void getCustomersCount() {
        assertEquals(3, customerManager.getCustomersCount());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 2, 3})
    void getCustomer(long id) {
        var order = customerManager.getCustomer(id);
        assertTrue(order.isPresent());
        assertEquals(id, order.get().getId());
    }

    @Test
    void addCustomer() {
        Customer[] customersToAdd = new Customer[]{
                new Customer(0, "cust1", "last1", null, null, new HashSet<>()),
                new Customer(0, "cust2", "last2", "", "", new HashSet<>())
        };

        long startCount = customerManager.getCustomersCount();

        assertTrue(customerManager.getCustomer(customersToAdd[0].getId()).isEmpty());
        assertTrue(customerManager.getCustomer(customersToAdd[1].getId()).isEmpty());

        customerManager.addCustomer(customersToAdd[0]);
        assertEquals(startCount + 1, customerManager.getCustomersCount());
        assertTrue(customerManager.getCustomer(customersToAdd[0].getId()).isPresent());

        customerManager.addCustomer(customersToAdd[0]); // второй раз один объект не добавляет
        assertEquals(startCount + 1, customerManager.getCustomersCount());

        customerManager.addCustomer(customersToAdd[1]);
        assertEquals(startCount + 2, customerManager.getCustomersCount());
    }

    @Test
    void updateCustomer() {
        long customerId = 1;
        String newFirstname = "newFirstname";

        var existingCustomer = customerManager.getCustomer(customerId);
        assertTrue(existingCustomer.isPresent(), "Customer with id = " + customerId + " should exist");
        Customer customer = existingCustomer.get(); // получили название
        String oldTitle = customer.getFirstName();

        customer.setFirstName(newFirstname); // изменяем название
        customerManager.updateCustomer(customer);

        var genreAfterUpdate = customerManager.getCustomer(customerId);
        assertTrue(genreAfterUpdate.isPresent(), "Genre with id = " + customerId + " disappeared after update");
        assertEquals(customer.getFirstName(), genreAfterUpdate.get().getFirstName());
        // сравнение старого и нового названия
        assertNotEquals(newFirstname, oldTitle);
    }

    @Test
    public void testGetOrdersCustomer() {
        long id = 1;
        var customerOptional = customerManager.getCustomer(id);
        assertTrue(customerOptional.isPresent());
        var customer = customerOptional.get();

        Set<Order> orderSet = customer.getOrders();
        assertFalse(orderSet.isEmpty());

        Order[] orders = orderSet.toArray(orderSet.toArray(new Order[0]));

        // проверить на соответствие
        Set<Long> booksId = new HashSet<>();
        booksId.add(1L);
        booksId.add(2L);
        booksId.add(3L);
        booksId.add(6L);
        booksId.add(7L);

        for (Order order : orders) {
            assertTrue(booksId.contains(order.getId()));
        }
    }
}

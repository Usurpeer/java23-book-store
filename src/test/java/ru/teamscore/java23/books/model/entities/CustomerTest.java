package ru.teamscore.java23.books.model.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    public void testConstructorAndGetters() {
        Customer customer = new Customer(1, "John", "Doe");
        assertEquals(1, customer.getId());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertNull(customer.getMiddleName());
        assertNull(customer.getEmail());
    }

    @Test
    public void testAllArgsConstructor() {
        Customer customer = new Customer(1, "John", "Doe", "Middle", "john.doe@example.com");
        assertEquals(1, customer.getId());
        assertEquals("John", customer.getFirstName());
        assertEquals("Doe", customer.getLastName());
        assertEquals("Middle", customer.getMiddleName());
        assertEquals("john.doe@example.com", customer.getEmail());
    }

    @Test
    public void testEquals() {
        Customer customer1 = new Customer(1, "John", "Doe");
        Customer customer2 = new Customer(1, "John", "Doe");
        assertEquals(customer1, customer2);
    }
}
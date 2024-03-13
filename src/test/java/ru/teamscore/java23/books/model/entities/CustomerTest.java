package ru.teamscore.java23.books.model.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    void correctCreation() {
        String str1 = "tom";
        String str2 = "ford";

        Customer customer = new Customer();
        customer.setFirstName(str1);
        customer.setLastName(str2);

        assertEquals("tom", customer.getFirstName());
        assertEquals("ford", customer.getLastName());

        assertNull(customer.getMiddleName());
        assertNull(customer.getLogin());

        str1 = "tom";
        str2 = "ford";
        String str3 = "lastname";
        String str4 = "email";

        Customer customer2 = new Customer();
        customer2.setFirstName(str1);
        customer2.setMiddleName(str2);
        customer2.setLastName(str3);
        customer2.setLogin(str4);

        assertEquals("tom", customer2.getFirstName());
        assertEquals("lastname", customer2.getLastName());
        assertEquals("ford", customer2.getMiddleName());
        assertEquals("email", customer2.getLogin());
    }

    @Test
    void correctEquals() {
        String str1 = "tom";
        String str2 = "ford";

        Customer customer = new Customer();
        customer.setFirstName(str1);
        customer.setLastName(str2);

        str1 = "tom1";
        str2 = "ford1";

        Customer customer2 = new Customer();
        customer2.setFirstName(str1);
        customer2.setLastName(str2);

        assertEquals(customer2, customer);
    }
}
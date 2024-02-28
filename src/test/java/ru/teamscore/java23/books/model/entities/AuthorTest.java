package ru.teamscore.java23.books.model.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {
    @Test
    void testAllFieldsConstructor() {
        Author author = new Author(1L, "John", "Doe", "Michael", "JD");

        assertEquals(1L, author.getId());
        assertEquals("John", author.getFirstName());
        assertEquals("Doe", author.getLastName());
        assertEquals("Michael", author.getMiddleName());
        assertEquals("JD", author.getPseudonym());
    }

    @Test
    void testEquals() {
        Author author1 = new Author(1L, "John", "Doe");
        Author author2 = new Author(1L, "John", "Doe");
        Author author3 = new Author(2L, "Jane", "Doe");

        assertEquals(author1, author2);
        assertNotEquals(author1, author3);
    }

    @Test
    void testGettersAndSetters() {
        Author author = new Author(1L, "John", "Doe");

        assertEquals(1L, author.getId());
        assertEquals("John", author.getFirstName());
        assertEquals("Doe", author.getLastName());

        assertNull(author.getMiddleName());
        assertNull(author.getPseudonym());

        author.setMiddleName("Michael");
        author.setPseudonym("JD");

        assertEquals("Michael", author.getMiddleName());
        assertEquals("JD", author.getPseudonym());
    }
}
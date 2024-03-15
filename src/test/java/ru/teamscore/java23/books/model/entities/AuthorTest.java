package ru.teamscore.java23.books.model.entities;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorTest {
    @Test
    void correctCreation() {
        String str1 = "tom";
        String str2 = "ford";

        Author author = new Author();
        author.setFirstName(str1);
        author.setLastName(str2);

        assertEquals("tom", author.getFirstName());
        assertEquals("ford", author.getLastName());

        assertNull(author.getMiddleName());
        assertNull(author.getPseudonym());
        assertEquals(new HashSet<>(), author.getBooks());

        str1 = "tom";
        str2 = "ford";
        String str3 = "lastname";
        String str4 = "psev";

        Author author2 = new Author();
        author2.setFirstName(str1);
        author2.setMiddleName(str2);
        author2.setLastName(str3);
        author2.setPseudonym(str4);

        assertEquals("tom", author2.getFirstName());
        assertEquals("ford", author2.getMiddleName());
        assertEquals("lastname", author2.getLastName());
        assertEquals("psev", author2.getPseudonym());
    }

    @Test
    void correctEquals() {
        String str1 = "tom";
        String str2 = "ford";

        Author author = new Author();
        author.setFirstName(str1);
        author.setLastName(str2);

        str1 = "tom1";
        str2 = "ford1";

        Author author2 = new Author();
        author.setFirstName(str1);
        author.setLastName(str2);

        assertEquals(author2, author);
    }
}
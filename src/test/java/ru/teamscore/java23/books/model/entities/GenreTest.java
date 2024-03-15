package ru.teamscore.java23.books.model.entities;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {
    @Test
    void correctCreation() {
        String str1 = "title";

        Genre genre = new Genre();
        genre.setTitle(str1);

        assertEquals("title", genre.getTitle());
        assertEquals(new HashSet<>(), genre.getBooks());
    }

    @Test
    void correctEquals() {
        String str1 = "title";
        String str2 = "title2";

        Genre genre = new Genre();
        genre.setTitle(str1);

        Genre genre1 = new Genre();
        genre1.setTitle(str2);

        assertEquals(genre1, genre);
    }
}
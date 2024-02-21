package ru.teamscore.java23.books.model.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GenreTest {
    @Test
    public void testConstructorAndGetters() {
        Genre genre = new Genre(1, "Science Fiction");
        assertEquals(1, genre.getId());
        assertEquals("Science Fiction", genre.getTitle());
    }

    @Test
    public void testAllArgsConstructor() {
        Genre genre = new Genre(1, "Fantasy");
        assertEquals(1, genre.getId());
        assertEquals("Fantasy", genre.getTitle());
    }

    @Test
    public void testEqualsAndHashCode() {
        Genre genre1 = new Genre(1, "Thriller");
        Genre genre2 = new Genre(1, "Mystery");
        assertEquals(genre1, genre2);
        assertEquals(genre1.hashCode(), genre2.hashCode());

        Genre genre3 = new Genre(2, "Fantasy");
        assertNotEquals(genre1, genre3);
        assertNotEquals(genre1.hashCode(), genre3.hashCode());
    }

    @Test
    public void testSetters() {
        Genre genre = new Genre(1, "Action");
        // Проверяем, что для поля title сеттер генерируется
        genre.setTitle("Adventure");
        assertEquals("Adventure", genre.getTitle());
    }
}
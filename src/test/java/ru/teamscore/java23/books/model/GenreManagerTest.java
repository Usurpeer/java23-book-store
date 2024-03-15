package ru.teamscore.java23.books.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class GenreManagerTest {
    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;
    private Catalog catalog;

    @BeforeAll
    public static void setup() throws IOException {
        entityManagerFactory = new Configuration()
                .configure("hibernate-postgres.cfg.xml")
                .addAnnotatedClass(Book.class)
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
        SqlScripts.runFromFile(entityManagerFactory, "insertAuthorAndGenre.sql");
        SqlScripts.runFromFile(entityManagerFactory, "insertLinkBooksAndGenresAuthors.sql");
        entityManager = entityManagerFactory.createEntityManager();
        catalog = new Catalog(entityManager);
    }

    @AfterEach
    public void closeSession() throws IOException {
        if (entityManager != null) {
            entityManager.close();
        }
        SqlScripts.runFromFile(entityManagerFactory, "clearCatalogData.sql");
    }
    @Test
    public void getCount(){
        Catalog.GenreManager genreManager = catalog.getGenreManager();
        assertEquals(86, genreManager.getGenresCount());
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 86, 50})
    public void getGenreExists(long id){
        Catalog.GenreManager genreManager = catalog.getGenreManager();

        var result = genreManager.getGenre(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 1000, 2500})
    void getGenreNotExists(long id) {
        Catalog.GenreManager genreManager = catalog.getGenreManager();

        var result = genreManager.getGenre(id);
        assertTrue(result.isEmpty());
    }

    @Test
    void addGenre() {
        Catalog.GenreManager genreManager = catalog.getGenreManager();
        Genre[] genresToAdd = new Genre[]{
                new Genre(0, "title1", new HashSet<>()),
                new Genre(0, "title2", new HashSet<>())
        };

        long startCount = genreManager.getGenresCount();

        assertTrue(genreManager.getGenre(genresToAdd[0].getId()).isEmpty());
        assertTrue(genreManager.getGenre(genresToAdd[1].getId()).isEmpty());

        genreManager.addGenre(genresToAdd[0]);
        assertEquals(startCount + 1, genreManager.getGenresCount());
        assertTrue(genreManager.getGenre(genresToAdd[0].getId()).isPresent());

        genreManager.addGenre(genresToAdd[0]); // второй раз один объект не добавляет
        assertEquals(startCount + 1, genreManager.getGenresCount());

        genreManager.addGenre(genresToAdd[1]);
        assertEquals(startCount + 2, genreManager.getGenresCount());
    }

    @Test
    void updateGenre() {
        long genreId = 1;
        String newTitle = "newTitle";
        Catalog.GenreManager genreManager = catalog.getGenreManager();

        var existingGenre = genreManager.getGenre(genreId);
        assertTrue(existingGenre.isPresent(), "Genre with id = " + genreId + " should exist");
        Genre genre = existingGenre.get(); // получили название
        String oldTitle = genre.getTitle();

        genre.setTitle(newTitle); // изменяем название
        genreManager.updateGenre(genre);

        var genreAfterUpdate = genreManager.getGenre(genreId);
        assertTrue(genreAfterUpdate.isPresent(), "Genre with id = " + genreId + " disappeared after update");
        assertEquals(genre.getTitle(), genreAfterUpdate.get().getTitle());
        // сравнение старого и нового названия
        assertNotEquals(newTitle, oldTitle);
    }

    @Test
    public void testGetBooksAuthor() {
        long genreId = 1;
        Catalog.GenreManager genreManager = catalog.getGenreManager();

        var result = genreManager.getGenre(genreId);

        assertTrue(result.isPresent());
        Set<Book> bookSet = result.get().getBooks();
        assertFalse(bookSet.isEmpty());

        Book[] books = bookSet.toArray(bookSet.toArray(new Book[0]));

        // проверить на соответствие
        Set<Long> booksId = new HashSet<>();
        booksId.add(47L);
        booksId.add(48L);
        booksId.add(49L);

        for (Book book : books) {
            assertTrue(booksId.contains(book.getId()));
        }
    }
}

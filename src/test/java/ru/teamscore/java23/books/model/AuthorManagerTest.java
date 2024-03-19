package ru.teamscore.java23.books.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

public class AuthorManagerTest {
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
        Catalog.AuthorManager authorManager = catalog.getAuthorManager();
        assertEquals(102, authorManager.getAuthorsCount());
    }
    @Test
    void getAllAuthors() throws JsonProcessingException {
        Catalog.AuthorManager authorManager = catalog.getAuthorManager();

        var allAuthors = authorManager.getAllAuthors();

        ObjectMapper mapper = new ObjectMapper();
        Author[] newArray = new Author[20];

        for (int i = 0; i < 20 && i < allAuthors.length; i++) {
            newArray[i] = allAuthors[i];
        }
        System.out.println(mapper.writeValueAsString(newArray));
    }
    @ParameterizedTest
    @ValueSource(longs = {1, 102, 50})
    public void getAuthorExists(long id){
        Catalog.AuthorManager authorManager = catalog.getAuthorManager();

        var result = authorManager.getAuthor(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());

    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 1000, 2500})
    void getAuthorNotExists(long id) {
        Catalog.AuthorManager authorManager = catalog.getAuthorManager();

        var result = authorManager.getAuthor(id);
        assertTrue(result.isEmpty());
    }

    @Test
    void addAuthor() {
        Catalog.AuthorManager authorManager = catalog.getAuthorManager();
        Author[] authorsToAdd = new Author[]{
                new Author(0, "title1", "last", "midl", "Pseu", new HashSet<>()),
                new Author(0, "title2", "last2", "midl2", "Pseu2", new HashSet<>())
        };

        long startCount = authorManager.getAuthorsCount();

        assertTrue(authorManager.getAuthor(authorsToAdd[0].getId()).isEmpty());
        assertTrue(authorManager.getAuthor(authorsToAdd[1].getId()).isEmpty());

        authorManager.addAuthor(authorsToAdd[0]);
        assertEquals(startCount + 1, authorManager.getAuthorsCount());
        assertTrue(authorManager.getAuthor(authorsToAdd[0].getId()).isPresent());

        authorManager.addAuthor(authorsToAdd[0]); // второй раз один объект не добавляет
        assertEquals(startCount + 1, authorManager.getAuthorsCount());

        authorManager.addAuthor(authorsToAdd[1]);
        assertEquals(startCount + 2, authorManager.getAuthorsCount());
    }

    @Test
    void updateAuthor() {
        long authorId = 1;
        String newPseu = "newPseu";
        Catalog.AuthorManager authorManager = catalog.getAuthorManager();

        var existingAuthor = authorManager.getAuthor(authorId);
        assertTrue(existingAuthor.isPresent(), "Author with id = " + authorId + " should exist");
        Author author = existingAuthor.get(); // получили автора
        String oldPseu = author.getPseudonym();

        author.setPseudonym(newPseu); // изменяем псевдоним
        authorManager.updateAuthor(author);

        var authorAfterUpdate = authorManager.getAuthor(authorId);
        assertTrue(authorAfterUpdate.isPresent(), "Author with id = " + authorId + " disappeared after update");
        assertEquals(author.getPseudonym(), authorAfterUpdate.get().getPseudonym());
        // сравнение старого и нового псевдонима
        assertNotEquals(newPseu, oldPseu);
    }

    @Test
    public void testGetBooksAuthor() {
        long authorId = 1;
        Catalog.AuthorManager authorManager = catalog.getAuthorManager();

        var result = authorManager.getAuthor(authorId);

        assertTrue(result.isPresent());
        Set<Book> bookSet = result.get().getBooks();
        assertFalse(bookSet.isEmpty());

        Book[] books = bookSet.toArray(bookSet.toArray(new Book[0]));

        // проверить на соответствие
        Set<Long> booksId = new HashSet<>();
        booksId.add(193L);
        booksId.add(194L);
        booksId.add(195L);

        for (Book book : books) {
            assertTrue(booksId.contains(book.getId()));
        }
    }
}

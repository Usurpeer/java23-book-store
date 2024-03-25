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
import ru.teamscore.java23.books.model.enums.BookStatus;
import ru.teamscore.java23.books.model.enums.CatalogSortOption;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.function.BiPredicate;

import static org.junit.jupiter.api.Assertions.*;

class CatalogTest {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

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
    }

    @AfterEach
    public void closeSession() throws IOException {
        if (entityManager != null) {
            entityManager.close();
        }
        SqlScripts.runFromFile(entityManagerFactory, "clearCatalogData.sql");
    }

    @Test
    void getOpenBooks() throws JsonProcessingException {
        Catalog catalog = new Catalog(entityManager);
        var openBooks = catalog.getOpenBooks();

        ObjectMapper mapper = new ObjectMapper();

        System.out.println(mapper.writeValueAsString(openBooks));
    }

    @Test
    void getAllPublishers() throws JsonProcessingException {
        Catalog catalog = new Catalog(entityManager);
        var allPublishers = catalog.getAllPublishers();

        ObjectMapper mapper = new ObjectMapper();

        System.out.println(mapper.writeValueAsString(allPublishers));
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 100, 250})
    void getBookExists(long id) {
        Catalog catalog = new Catalog(entityManager);
        var result = catalog.getBook(id);
        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @ParameterizedTest
    @ValueSource(longs = {-1, 1000, 2500})
    void getBookNotExists(long id) {
        Catalog catalog = new Catalog(entityManager);
        var result = catalog.getBook(id);
        assertTrue(result.isEmpty());
    }

    @Test
    void addBook() {
        Catalog catalog = new Catalog(entityManager);
        Book[] booksToAdd = new Book[]{
                Book.load(0, "title1", null, BookStatus.OPEN, BigDecimal.valueOf(999),
                        "Penguin Books", 1925, new HashSet<>(), new HashSet<>(), "default.png"),
                Book.load(0, "title2", null, BookStatus.OPEN, BigDecimal.valueOf(10),
                        "Penguin Books", 1955, new HashSet<>(), new HashSet<>(), "default.png")
        };

        long startCount = catalog.getBooksCount();

        assertTrue(catalog.getBook(booksToAdd[0].getId()).isEmpty());
        assertTrue(catalog.getBook(booksToAdd[1].getId()).isEmpty());

        catalog.addBook(booksToAdd[0]);
        assertEquals(startCount + 1, catalog.getBooksCount());
        assertTrue(catalog.getBook(booksToAdd[0].getId()).isPresent());

        catalog.addBook(booksToAdd[0]); // второй раз один объект не добавляет
        assertEquals(startCount + 1, catalog.getBooksCount());

        catalog.addBook(booksToAdd[1]);
        assertEquals(startCount + 2, catalog.getBooksCount());
    }

    @Test
    void updateBook() {
        long bookId = 1;
        BigDecimal priceToAdd = BigDecimal.valueOf(10);
        Catalog catalog = new Catalog(entityManager);

        var existingBook = catalog.getBook(bookId);
        assertTrue(existingBook.isPresent(), "Book with id = " + bookId + " should exist");
        Book book = existingBook.get(); // получили книгу
        BigDecimal oldPrice = book.getPrice();

        book.setPrice(book.getPrice().add(priceToAdd)); // добавляем 10 к цене
        catalog.updateBook(book);

        var bookAfterUpdate = catalog.getBook(bookId);
        assertTrue(bookAfterUpdate.isPresent(), "Book with id = " + bookId + " disappeared after update");
        assertEquals(book.getPrice(), bookAfterUpdate.get().getPrice());
        // сравнение старой цены и новой
        assertEquals(oldPrice.add(priceToAdd), bookAfterUpdate.get().getPrice());
    }

    @Test
    void getSorted() {
        Catalog catalog = new Catalog(entityManager);

        testSorted(catalog, CatalogSortOption.TITLE, false, 0, 10,
                (b1, b2) -> b1.getTitle().compareTo(b2.getTitle()) >= 0);
        testSorted(catalog, CatalogSortOption.TITLE, true, 0, 10,
                (b1, b2) -> b1.getTitle().compareTo(b2.getTitle()) <= 0);

        testSorted(catalog, CatalogSortOption.PRICE, false, 1, 3,
                (b1, b2) -> b1.getPrice().compareTo(b2.getPrice()) >= 0);
        testSorted(catalog, CatalogSortOption.PRICE, true, 5, 5,
                (b1, b2) -> b1.getPrice().compareTo(b2.getPrice()) <= 0);

        testSorted(catalog, CatalogSortOption.YEAR, false, 1, 3,
                (b1, b2) -> b1.getYear() >= b2.getYear());
        testSorted(catalog, CatalogSortOption.YEAR, true, 5, 5,
                (b1, b2) -> b1.getYear() <= b2.getYear());
    }

    private void testSorted(Catalog catalog, CatalogSortOption option, boolean desc, int page, int pageSize,
                            BiPredicate<Book, Book> compare) {
        Collection<Book> result = catalog.getSorted(option, desc, "", page, pageSize);

        assertEquals(pageSize, result.size());
        Book prevBook = null;
        for (Book currentBook : result) {
            if (prevBook != null) {
                assertTrue(compare.test(currentBook, prevBook));
            }
            prevBook = currentBook;
        }
    }

    @Test
    public void testGetAuthorsBooks() {
        long id = 256;
        Catalog catalog = new Catalog(entityManager);
        var result = catalog.getBook(id);

        assertTrue(result.isPresent());
        Set<Author> authorSet = result.get().getAuthors();
        assertFalse(authorSet.isEmpty());

        Author[] authors = authorSet.toArray(authorSet.toArray(new Author[0]));

        // проверить на соответствие
        Set<Long> idAuthors = new HashSet<>();
        idAuthors.add(100L);
        idAuthors.add(101L);
        idAuthors.add(102L);

        for (Author author : authors) {
            assertTrue(idAuthors.contains(author.getId()));
        }
    }

    @Test
    public void testGetGenresBooks() {
        long id = 3;
        Catalog catalog = new Catalog(entityManager);
        var result = catalog.getBook(id);

        assertTrue(result.isPresent());
        Set<Genre> genreSet = result.get().getGenres();
        assertFalse(genreSet.isEmpty());

        Genre[] genres = genreSet.toArray(genreSet.toArray(new Genre[0]));

        // проверить на соответствие
        Set<Long> genresId = new HashSet<>();
        genresId.add(36L);
        genresId.add(9L);
        genresId.add(78L);

        for (Genre genre : genres) {
            assertTrue(genresId.contains(genre.getId()));
        }
    }
}
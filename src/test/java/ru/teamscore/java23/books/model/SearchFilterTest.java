package ru.teamscore.java23.books.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;
import ru.teamscore.java23.books.model.enums.BookStatus;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class SearchFilterTest {
    List<Book> books = new ArrayList<>();
    SearchFilter normalSearchFilter;
    SearchFilter almostNormalSearchFilter;
    SearchFilter wrongSearchFilter;

    @BeforeEach
    void setUp() {
        books.addAll(generateTestBooks());

        normalSearchFilter = new SearchFilter(
                Optional.of(BigDecimal.ONE), Optional.of(BigDecimal.TEN),
                Optional.of(2000), Optional.of(2020),
                getAuthors1(), getGenres1(),
                Optional.of("Publisher B"),
                books);

        almostNormalSearchFilter = new SearchFilter(
                Optional.of(BigDecimal.valueOf(999)), Optional.of(BigDecimal.valueOf(1000)),
                Optional.of(3000), Optional.of(3020),
                getAuthors3(), getGenres3(),
                Optional.of("Publisher E"),
                books);

        wrongSearchFilter = new SearchFilter(
                Optional.of(BigDecimal.valueOf(-10)), Optional.of(BigDecimal.valueOf(-20)),
                Optional.of(-2000), Optional.of(-2020),
                getAuthors4(), getGenres4(),
                null,
                books);
        noAuthorsFilter = new SearchFilter(
                Optional.of(BigDecimal.valueOf(-10)), Optional.of(BigDecimal.valueOf(100000)),
                Optional.of(0), Optional.of(3000),
                null, getGenres1(),
                Optional.of("Publisher B"),
                books);
        emptyGenresFilter = new SearchFilter(
                Optional.of(BigDecimal.valueOf(-10)), Optional.of(BigDecimal.valueOf(100000)),
                Optional.of(0), Optional.of(3000),
                getAuthors1(), null,
                Optional.of("Publisher B"),
                books);
    }


    @Test
    public void testExceptionFilterOnPrice() {
        // Проверка, когда minPrice пустой
        SearchFilter searchFilter = new SearchFilter(null, null,
                null, null, null, null, null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnPrice());

        // Проверка, когда maxPrice пустой
        searchFilter = new SearchFilter(Optional.of(BigDecimal.TEN), null,
                null, null, null, null, null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnPrice());

        // Проверка, когда список книг пустой
        searchFilter = new SearchFilter(Optional.of(BigDecimal.TEN), Optional.of(BigDecimal.TEN),
                null, null, null, null, null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnPrice());
    }

    @Test
    public void testFilterOnPrice() {
        // обычная ситуация, когда max > min и есть результаты
        List<Book> normalFiltered = normalSearchFilter.filterOnPrice();

        assertEquals(1, normalFiltered.size());
        assertTrue(normalFiltered.contains(books.get(1)));

        // ситуация, когда нет результатов
        normalFiltered = almostNormalSearchFilter.filterOnPrice();
        assertEquals(0, normalFiltered.size());


        // когда max < min
        normalFiltered = wrongSearchFilter.filterOnPrice();
        assertEquals(0, normalFiltered.size());
    }


    @Test
    public void testExceptionFilterOnYear() {
        // Проверка, когда minYear пустой
        SearchFilter searchFilter = new SearchFilter(null, null,
                null, null,
                null, null, null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnYear());

        // Проверка, когда maxYear пустой
        searchFilter = new SearchFilter(null, null,
                Optional.of(2), null,
                null, null, null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnYear());

        // Проверка, когда список книг пустой
        searchFilter = new SearchFilter(null, null,
                Optional.of(2), Optional.of(3),
                null, null, null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnYear());
    }

    @Test
    public void testFilterOnYear() {
        // обычная ситуация, когда max > min и есть результаты
        List<Book> normalFiltered = normalSearchFilter.filterOnYear();

        assertEquals(3, normalFiltered.size());
        assertFalse(normalFiltered.contains(books.get(3)));

        // ситуация, когда нет результатов
        normalFiltered = almostNormalSearchFilter.filterOnYear();
        assertEquals(0, normalFiltered.size());


        // когда max < min
        normalFiltered = wrongSearchFilter.filterOnYear();
        assertEquals(0, normalFiltered.size());
    }


    @Test
    public void testExceptionFilterOnPublisher() {
        // Проверка, когда publisher пустой
        SearchFilter searchFilter = new SearchFilter(null, null,
                null, null,
                null, null, null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnPublisher());


        // Проверка, когда список книг пустой
        searchFilter = new SearchFilter(null, null,
                Optional.of(2), Optional.of(3),
                null, null, null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnPublisher());
    }

    @Test
    public void testFilterOnPublisher() {
        // обычная ситуация, когда есть результаты
        List<Book> normalFiltered = normalSearchFilter.filterOnPublisher();

        assertEquals(2, normalFiltered.size());
        assertTrue(normalFiltered.contains(books.get(1)));
        assertTrue(normalFiltered.contains(books.get(3)));


        // ситуация, когда нет результатов
        normalFiltered = almostNormalSearchFilter.filterOnPublisher();
        assertEquals(0, normalFiltered.size());


        // также нет результатов, при null строке
        normalFiltered = wrongSearchFilter.filterOnPublisher();
        assertEquals(0, normalFiltered.size());
    }

    @Test
    public void testExceptionFilterOnGenre() {
        // когда список жанров нулевой
        SearchFilter searchFilter = new SearchFilter(null, null, null, null, null,
                null,
                null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnGenre());

        // список жанров пустой
        searchFilter = new SearchFilter(null, null, null, null, null,
                new HashSet<>(),
                null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnGenre());

        // список книг null
        searchFilter = new SearchFilter(null, null, null, null, null,
                null, null,
                null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnGenre());
    }

    @Test
    public void testFilterOnGenre() {
        // есть пересечения
        List<Book> normalFiltered = normalSearchFilter.filterOnGenre();
        assertEquals(4, normalFiltered.size());

        // результат есть, но не со всеми и всего 1 пересечение
        normalFiltered = almostNormalSearchFilter.filterOnGenre();
        assertEquals(2, normalFiltered.size());
        assertTrue(normalFiltered.contains(books.get(1)));
        assertTrue(normalFiltered.contains(books.get(3)));

        // когда нет пересечений, нет результата
        normalFiltered = wrongSearchFilter.filterOnGenre();
        assertEquals(0, normalFiltered.size());
    }

    @Test
    public void testExceptionFilterOnAuthor() {
        // когда список авторов нулевой
        SearchFilter searchFilter = new SearchFilter(null, null, null, null,
                null,
                null, null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnAuthor());

        // список авторов пустой
        searchFilter = new SearchFilter(null, null, null, null,
                new HashSet<>(),
                null, null, null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnAuthor());

        // список книг null
        searchFilter = new SearchFilter(null, null, null, null,
                null, null, null,
                null);
        assertEquals(Collections.emptyList(), searchFilter.filterOnAuthor());
    }

    @Test
    public void testFilterOnAuthor() {
        // есть пересечения
        List<Book> normalFiltered = normalSearchFilter.filterOnAuthor();
        assertEquals(4, normalFiltered.size());

        // результат есть, но не со всеми и всего 1 пересечение
        normalFiltered = almostNormalSearchFilter.filterOnAuthor();
        assertEquals(2, normalFiltered.size());
        assertTrue(normalFiltered.contains(books.get(1)));
        assertTrue(normalFiltered.contains(books.get(2)));

        // когда нет пересечений, нет результата
        normalFiltered = wrongSearchFilter.filterOnAuthor();
        assertEquals(0, normalFiltered.size());
    }

    @Test
    public void testExceptionFilter() {
        // когда books null
        SearchFilter searchFilter = new SearchFilter(null, null,
                null, null,
                null, null, null, null);
        assertEquals(Collections.emptyList(), searchFilter.filter());

        // когда books пустой
        searchFilter = new SearchFilter(null, null,
                null, null,
                null, null, null, new ArrayList<>());
        assertEquals(Collections.emptyList(), searchFilter.filter());


        // когда жанры не заданы, остальное должно отрабатывать нормально
    }


    // когда всё задано
    @Test
    public void testFilterFullField() {
        List<Book> normalFiltered = normalSearchFilter.filter();

        assertEquals(1, normalFiltered.size());
        assertTrue(normalFiltered.contains(books.get(1)));
    }

    // когда цена не задана, остальное должно отрабатывать нормально
    @Test
    public void testFilterNoPriceField() {
        // цена задана, но неправильно, значит фильтрация неправильная и ничего нет
        List<Book> normalFiltered = almostNormalSearchFilter.filter();

        assertEquals(0, normalFiltered.size());

        // цена null, она не учитывается, список исходный
        SearchFilter excep = new SearchFilter(null, null,
                null, null, null, null, null, books);

        normalFiltered = excep.filter();

        assertEquals(4, normalFiltered.size());
    }

    SearchFilter noAuthorsFilter;
    SearchFilter emptyGenresFilter;

    // когда жанры не заданы, остальное должно отрабатывать нормально
    @Test
    public void testFilterNoGenresAndAuthors() {
        // авторы null, они не учитывается, список исходный (ну только те, которые с издательством B)
        List<Book> normalFiltered = noAuthorsFilter.filter();
        assertEquals(2, normalFiltered.size());
        assertTrue(normalFiltered.contains(books.get(1)));
        assertTrue(normalFiltered.contains(books.get(3)));

        // жанры empty
        // авторы null, они не учитывается, список исходный (ну только те, которые с издательством B)
        normalFiltered = emptyGenresFilter.filter();
        assertEquals(2, normalFiltered.size());
        assertTrue(normalFiltered.contains(books.get(1)));
        assertTrue(normalFiltered.contains(books.get(3)));


        SearchFilter excep = new SearchFilter(null, null,
                null, null, null, null, null, books);
    }

    public static List<Book> generateTestBooks() {
        Set<Genre> genres1 = getGenres1();
        Set<Genre> genres2 = getGenres2();
        Set<Author> authors1 = getAuthors1();
        Set<Author> authors2 = getAuthors2();

        Book book1 = Book.load(1, "Book 1", BookStatus.OPEN, new BigDecimal("0.99"),
                "Description 1", "Publisher A", 2000, genres1, authors1);

        Book book2 = Book.load(2, "Book 2", BookStatus.CLOSED, new BigDecimal("9.05"),
                "Description 2", "Publisher B", 2010, genres2, authors2);

        Book book3 = Book.load(3, "Book 3", BookStatus.HIDDEN, new BigDecimal("99.99"),
                "Description 3", "Publisher C", 2020, genres1, authors2);

        Book book4 = Book.load(4, "Book 4", BookStatus.OPEN, new BigDecimal("50"),
                "Description 4", "Publisher B", 2030, genres2, authors1);

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);

        return books;
    }

    public static Set<Genre> getGenres1() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(1, "Фэнтези"));
        genres.add(new Genre(2, "Триллер"));
        genres.add(new Genre(3, "Романтика"));
        genres.add(new Genre(4, "Научная фантастика"));
        genres.add(new Genre(5, "Мистика"));
        genres.add(new Genre(6, "Хоррор"));
        genres.add(new Genre(7, "Комедия"));

        return genres;
    }

    public static Set<Genre> getGenres2() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(7, "Комедия"));
        genres.add(new Genre(8, "Драма"));
        genres.add(new Genre(9, "Приключения"));
        genres.add(new Genre(10, "Исторический роман"));

        return genres;
    }

    public static Set<Genre> getGenres3() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(10, "Исторический роман"));
        genres.add(new Genre(11, "Роман"));

        return genres;
    }

    public static Set<Genre> getGenres4() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(11, "Роман"));

        return genres;
    }


    public static Set<Author> getAuthors1() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(1L, "John", "Doe", "Michael", "JD"));
        authors.add(new Author(2L, "Jane", "Doe", "Alice", "JDoe"));
        authors.add(new Author(3L, "Alex", "Smith"));
        authors.add(new Author(4L, "Emily", "Johnson"));
        authors.add(new Author(5L, "Adam", "Smith", "David", "AS"));

        return authors;
    }

    public static Set<Author> getAuthors2() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(5L, "Adam", "Smith", "David", "AS"));
        authors.add(new Author(6L, "Eva", "Brown", "Sophie", "EB"));
        authors.add(new Author(7L, "Michael", "Jackson"));
        authors.add(new Author(8L, "Jennifer", "Lopez"));

        return authors;
    }

    public static Set<Author> getAuthors3() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(8L, "Jennifer", "Lopez"));
        authors.add(new Author(9, "9", "9"));

        return authors;
    }

    public static Set<Author> getAuthors4() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(9, "9", "9"));

        return authors;
    }

}
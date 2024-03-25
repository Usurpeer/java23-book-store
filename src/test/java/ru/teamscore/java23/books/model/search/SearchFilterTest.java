package ru.teamscore.java23.books.model.search;

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

        normalSearchFilter = SearchFilter.builder()
                .minPrice(Optional.of(BigDecimal.ONE))
                .maxPrice(Optional.of(BigDecimal.TEN))
                .minYear(Optional.of(2000))
                .maxYear(Optional.of(2020))
                .authors(getAuthors1())
                .genres(getGenres1())
                .publishers(Optional.of("Publisher B"))
                .books(books)
                .build();

        almostNormalSearchFilter = SearchFilter.builder()
                .minPrice(Optional.of(BigDecimal.valueOf(999)))
                .maxPrice(Optional.of(BigDecimal.valueOf(1000)))
                .minYear(Optional.of(3000))
                .maxYear(Optional.of(3020))
                .authors(getAuthors3())
                .genres(getGenres3())
                .publishers(Optional.of("Publisher E"))
                .books(books)
                .build();

        wrongSearchFilter = SearchFilter.builder()
                .minPrice(Optional.of(BigDecimal.valueOf(-10)))
                .maxPrice(Optional.of(BigDecimal.valueOf(-20)))
                .minYear(Optional.of(-2000))
                .maxYear(Optional.of(-2020))
                .authors(getAuthors4())
                .genres(getGenres4())
                .publishers(Optional.empty())
                .books(books)
                .build();

        noAuthorsFilter = SearchFilter.builder()
                .minPrice(Optional.of(BigDecimal.valueOf(-10)))
                .maxPrice(Optional.of(BigDecimal.valueOf(1000000)))
                .minYear(Optional.of(0))
                .maxYear(Optional.of(3000))
                .genres(getGenres1())
                .publishers(Optional.of("Publisher B"))
                .books(books)
                .build();

        emptyGenresFilter = SearchFilter.builder()
                .minPrice(Optional.of(BigDecimal.valueOf(-10)))
                .maxPrice(Optional.of(BigDecimal.valueOf(1000000)))
                .minYear(Optional.of(0))
                .maxYear(Optional.of(3000))
                .authors(getAuthors1())
                .publishers(Optional.of("Publisher B"))
                .books(books)
                .build();
    }


    @Test
    public void testExceptionFilterOnPrice() {

        // Проверка, когда minPrice пустой
        SearchFilter searchFilter = SearchFilter.builder().books(books).build();
        assertEquals(Collections.emptyList(), searchFilter.filterOnPrice());

        // Проверка, когда maxPrice пустой
        searchFilter = SearchFilter.builder()
                .minPrice(Optional.of(BigDecimal.TEN))
                .books(books)
                .build();
        assertEquals(Collections.emptyList(), searchFilter.filterOnPrice());

        // Проверка, когда список книг пустой
        searchFilter = SearchFilter.builder()
                .minPrice(Optional.of(BigDecimal.TEN))
                .maxPrice(Optional.of(BigDecimal.TEN))
                .books(new ArrayList<>())
                .build();
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
        SearchFilter searchFilter = SearchFilter.builder().build();
        assertEquals(Collections.emptyList(), searchFilter.filterOnYear());

        // Проверка, когда maxYear пустой
        searchFilter = SearchFilter.builder()
                .minYear(Optional.of(2))
                .build();
        assertEquals(Collections.emptyList(), searchFilter.filterOnYear());

        // Проверка, когда список книг пустой
        searchFilter = SearchFilter.builder()
                .minYear(Optional.of(2))
                .maxYear(Optional.of(3))
                .books(new ArrayList<>())
                .build();
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
        SearchFilter searchFilter = SearchFilter.builder().build();
        assertEquals(Collections.emptyList(), searchFilter.filterOnPublishers());


        // Проверка, когда список книг пустой
        searchFilter = SearchFilter.builder()
                .publishers(Optional.of("Pub"))
                .build();
        assertEquals(Collections.emptyList(), searchFilter.filterOnPublishers());
    }

    @Test
    public void testFilterOnPublisher() {
        // обычная ситуация, когда есть результаты
        List<Book> normalFiltered = normalSearchFilter.filterOnPublishers();

        assertEquals(2, normalFiltered.size());
        assertTrue(normalFiltered.contains(books.get(1)));
        assertTrue(normalFiltered.contains(books.get(3)));


        // ситуация, когда нет результатов
        normalFiltered = almostNormalSearchFilter.filterOnPublishers();
        assertEquals(0, normalFiltered.size());


        // также нет результатов, при Optional.empty() строке
        normalFiltered = wrongSearchFilter.filterOnPublishers();
        assertEquals(0, normalFiltered.size());
    }

    @Test
    public void testExceptionFilterOnGenre() {
        // когда список жанров пустой
        SearchFilter searchFilter = SearchFilter.builder().build();
        assertEquals(Collections.emptyList(), searchFilter.filterOnGenre());

        // список книг пустой список
        searchFilter = SearchFilter.builder()
                .genres(getGenres1())
                .books(new ArrayList<>())
                .build();
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
        // когда список авторов пустой
        SearchFilter searchFilter = SearchFilter.builder().build();
        assertEquals(Collections.emptyList(), searchFilter.filterOnAuthor());

        // список книг пустой
        searchFilter = SearchFilter.builder()
                .authors(getAuthors1())
                .build();
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
        // когда books пустой
        SearchFilter searchFilter = SearchFilter.builder().build();
        assertEquals(Collections.emptyList(), searchFilter.filter());
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

        // цена пустая, она не учитывается, будут учитываться только введенные критерии, то есть издательство
        SearchFilter excep = SearchFilter.builder()
                .publishers(Optional.of("Publisher B"))
                .books(books)
                .build();

        normalFiltered = excep.filter();

        assertEquals(2, normalFiltered.size());
        assertTrue(normalFiltered.contains(books.get(1)));
        assertTrue(normalFiltered.contains(books.get(3)));
    }

    SearchFilter noAuthorsFilter;
    SearchFilter emptyGenresFilter;

    // когда жанры не заданы, остальное должно отрабатывать нормально
    @Test
    public void testFilterNoGenresAndAuthors() {
        // авторы Optional.empty(), они не учитывается, список исходный (только те, которые с издательством B)
        List<Book> normalFiltered = noAuthorsFilter.filter();
        assertEquals(2, normalFiltered.size());
        assertTrue(normalFiltered.contains(books.get(1)));
        assertTrue(normalFiltered.contains(books.get(3)));

        // жанры empty
        // авторы Optional.empty(), они не учитывается, список исходный (ну только те, которые с издательством B)
        normalFiltered = emptyGenresFilter.filter();
        assertEquals(2, normalFiltered.size());
        assertTrue(normalFiltered.contains(books.get(1)));
        assertTrue(normalFiltered.contains(books.get(3)));
    }

    public static List<Book> generateTestBooks() {
        Set<Genre> genres1 = getGenres1();
        Set<Genre> genres2 = getGenres2();
        Set<Author> authors1 = getAuthors1();
        Set<Author> authors2 = getAuthors2();

        Book book1 = Book.load(1, "Book 1", "Description 1", BookStatus.OPEN, new BigDecimal("0.99"),
                "Publisher A", 2000, genres1, authors1, "default.png");

        Book book2 = Book.load(2, "Book 2", "Description 2", BookStatus.CLOSED, new BigDecimal("9.05"),
                "Publisher B", 2010, genres2, authors2, "default.png");

        Book book3 = Book.load(3, "Book 3", "Description 3", BookStatus.HIDDEN, new BigDecimal("99.99"),
                "Publisher C", 2020, genres1, authors2, "default.png");

        Book book4 = Book.load(4, "Book 4", "Description 4", BookStatus.OPEN, new BigDecimal("50"),
                "Publisher B", 2030, genres2, authors1, "default.png");

        List<Book> books = new ArrayList<>();
        books.add(book1);
        books.add(book2);
        books.add(book3);
        books.add(book4);

        return books;
    }

    public static Set<Genre> getGenres1() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(1, "Фэнтези", new HashSet<>()));
        genres.add(new Genre(2, "Триллер", new HashSet<>()));
        genres.add(new Genre(3, "Романтика", new HashSet<>()));
        genres.add(new Genre(4, "Научная фантастика", new HashSet<>()));
        genres.add(new Genre(5, "Мистика", new HashSet<>()));
        genres.add(new Genre(6, "Хоррор", new HashSet<>()));
        genres.add(new Genre(7, "Комедия", new HashSet<>()));

        return genres;
    }

    public static Set<Genre> getGenres2() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(7, "Комедия", new HashSet<>()));
        genres.add(new Genre(8, "Драма", new HashSet<>()));
        genres.add(new Genre(9, "Приключения", new HashSet<>()));
        genres.add(new Genre(10, "Исторический роман", new HashSet<>()));

        return genres;
    }

    public static Set<Genre> getGenres3() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(10, "Исторический роман", new HashSet<>()));
        genres.add(new Genre(11, "Роман", new HashSet<>()));

        return genres;
    }

    public static Set<Genre> getGenres4() {
        Set<Genre> genres = new HashSet<>();

        genres.add(new Genre(11, "Роман", new HashSet<>()));

        return genres;
    }


    public static Set<Author> getAuthors1() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(1L, "John", "Doe", "Michael", "JD", new HashSet<>()));
        authors.add(new Author(2L, "Jane", "Doe", "Alice", "JDoe", new HashSet<>()));
        authors.add(new Author(3L, "Alex", "Smith", null, null, new HashSet<>()));
        authors.add(new Author(4L, "Emily", "Johnson", null, null, new HashSet<>()));
        authors.add(new Author(5L, "Adam", "Smith", "David", "AS", new HashSet<>()));

        return authors;
    }

    public static Set<Author> getAuthors2() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(5L, "Adam", "Smith", "David", "AS", new HashSet<>()));
        authors.add(new Author(6L, "Eva", "Brown", "Sophie", "EB", new HashSet<>()));
        authors.add(new Author(7L, "Michael", "Jackson", null, null, new HashSet<>()));
        authors.add(new Author(8L, "Jennifer", "Lopez", null, null, new HashSet<>()));

        return authors;
    }

    public static Set<Author> getAuthors3() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(8L, "Jennifer", "Lopez", null, null, new HashSet<>()));
        authors.add(new Author(9, "9", "9", null, null, new HashSet<>()));

        return authors;
    }

    public static Set<Author> getAuthors4() {
        Set<Author> authors = new HashSet<>();

        authors.add(new Author(9, "9", "9", null, null, new HashSet<>()));

        return authors;
    }

}
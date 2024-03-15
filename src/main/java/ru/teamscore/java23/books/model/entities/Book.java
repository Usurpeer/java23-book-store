package ru.teamscore.java23.books.model.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.teamscore.java23.books.model.enums.BookStatus;

import java.math.BigDecimal;
import java.util.*;

@Getter
@Setter
@AllArgsConstructor(staticName = "load") // все поля

@ToString
@Entity
@Table(name = "book", schema = "catalog")
@NoArgsConstructor
@NamedQuery(name = "booksCount", query = "SELECT count(*) from Book")
@NamedQuery(name = "bookById", query = "from Book as b where b.id = :id")
public class Book {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String title; // название книги

    private String description; // описание книги

    @NonNull
    @Setter(AccessLevel.NONE) // Пропустить сеттер
    @Column(nullable = false, length = 15)
    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.CLOSED; // статус книги в продаже / нет

    @NonNull
    @Column(columnDefinition = "decimal(10,2)", nullable = false)
    private BigDecimal price; // цена книги

    private String publisher; // издательство книги

    @Column(name = "year_publication")
    private int year; // год публикации книги

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "book_genres",
            schema = "catalog", // я потратил 1000 часов, чтобы понять, что без этого не работает
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>(); // список жанров*/

    @ToString.Exclude
    @ManyToMany
    @JoinTable(
            name = "book_authors",
            schema = "catalog",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    private Set<Author> authors = new HashSet<>(); // список авторов книги

    @NonNull
    @Column(name = "image_name", nullable = false)
    private String imageName = "default_book.png";

    /*
    @ToString.Exclude
    @OneToMany(mappedBy = "pk.book", cascade = CascadeType.ALL)
    private List<OrdersBooks> orders = new ArrayList<>();
    */
    public void open() {
        status = BookStatus.OPEN;
    }

    public void close() {
        status = BookStatus.CLOSED;
    }

    public void hide() {
        status = BookStatus.HIDDEN;
    }

    public void addAuthor(@NonNull Author author) {
        if (authors == null) {
            authors = new HashSet<>();
        }
        authors.add(author);
    }

    public void addAuthor(@NonNull Set<Author> authors) {
        if (this.authors == null) {
            this.authors = new HashSet<>();
        }
        this.authors.addAll(authors);
    }

    public void addGenre(@NonNull Genre genre) {
        if (genres == null) {
            genres = new HashSet<>();
        }
        genres.add(genre);
    }

    public void addGenre(@NonNull Set<Genre> genres) {
        if (this.genres == null) {
            this.genres = new HashSet<>();
        }
        this.genres.addAll(genres);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Book book)) return false;
        return Objects.equals(id, book.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

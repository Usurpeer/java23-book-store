package ru.teamscore.java23.books.model.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import ru.teamscore.java23.books.model.enums.BookStatus;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor(staticName = "load") // все поля

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

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "book_genres",
            schema = "catalog", // я потратил 1000 часов, чтобы понять, что без этого не работает
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private Set<Genre> genres = new HashSet<>(); // список жанров*/

    @JsonManagedReference
    @ManyToMany(fetch = FetchType.EAGER)
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

    @Override
    public String toString() {
        return "Название: " + title +
                "\nОписание: " + description +
                "\nЦена: " + price +
                "\nИздательство: " + publisher +
                "\nГод публикации: " + year +
                "\nЖанры: " + genresToString() +
                "\nАвторы: " + authorsToString();
    }
    public String genresToString() {
        StringBuilder sb = new StringBuilder();
        for (Genre genre : genres) {
            sb.append(genre.toString()).append(", ");
        }
        if (!sb.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length()); // Удаление последней запятой и пробела
        }
        return sb.toString().toLowerCase(); // Приведение к нижнему регистру
    }

    public String authorsToString() {
        StringBuilder sb = new StringBuilder();
        for (Author author : authors) {
            sb.append(author.toString()).append(", ");
        }
        if (!sb.isEmpty()) {
            sb.delete(sb.length() - 2, sb.length()); // Удаление последней запятой и пробела
        }
        return sb.toString().toLowerCase(); // Приведение к нижнему регистру
    }
}

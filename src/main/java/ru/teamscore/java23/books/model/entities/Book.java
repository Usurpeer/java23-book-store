package ru.teamscore.java23.books.model.entities;

import jakarta.persistence.*;
import lombok.*;
import ru.teamscore.java23.books.model.enums.BookStatus;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

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

    // @Getter(AccessLevel.NONE) // Разрешаю геттер, потому что у меня 3 статуса
    @Setter(AccessLevel.NONE) // Пропустить сеттер
    @Column(nullable = false, length = 15)
    private BookStatus status = BookStatus.CLOSED; // статус книги в продаже / нет

    @Column(columnDefinition = "decimal(10,2)", nullable = false)
    private BigDecimal price; // цена книги

    private String publisher; // издательство книги

    @Column(name = "image_name", nullable = false)
    private String imageName = "default_book.png";

    @Column(name = "year_publication")
    private int year; // год публикации книги

    @ManyToMany
    @JoinTable(
            name = "book_genres",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    @ToString.Exclude
    private Set<Genre> genres; // список жанров

    @ManyToMany
    @JoinTable(
            name = "book_authors",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "author_id")
    )
    @ToString.Exclude
    private Set<Author> authors; // список авторов книги

    public void open() {
        status = BookStatus.OPEN;
    }

    public void close() {
        status = BookStatus.CLOSED;
    }

    public void hide() {
        status = BookStatus.HIDDEN;
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

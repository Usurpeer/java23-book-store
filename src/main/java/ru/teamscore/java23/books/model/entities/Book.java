package ru.teamscore.java23.books.model.entities;

import lombok.*;
import ru.teamscore.java23.books.model.enums.BookStatus;

import java.math.BigDecimal;
import java.util.Set;

@Data // toString, equals, hashcode, get/set (при чем set не создается для final полей)
@RequiredArgsConstructor // все final и NonNull поля
@AllArgsConstructor(staticName = "load") // все поля
@EqualsAndHashCode(of = "id") // чтобы учитывал только по id
public class Book {

    private final long id;

    private String title; // название книги

    @Setter(AccessLevel.NONE) // Пропустить сеттер
    private BookStatus status = BookStatus.CLOSED; // статус книги в продаже / нет

    @NonNull
    private BigDecimal price; // цена книги

    private String description; // описание книги

    private final String publisher; // издательство книги

    private final int year; // год публикации книги

    @NonNull
    private Set<Genre> genres; // список жанров
    @NonNull
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
}

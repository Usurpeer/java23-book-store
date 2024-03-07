package ru.teamscore.java23.books.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor

@ToString
@Entity
@Table(name = "genre", schema = "catalog")
@NoArgsConstructor
@NamedQuery(name = "genresCount", query = "SELECT count(*) from Genre")
@NamedQuery(name = "genreById", query = "from Genre as g where g.id = :id")
public class Genre {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id; // id жанра


    private String title; // название жанра

    @ManyToMany(mappedBy = "genres")
    @ToString.Exclude
    private Set<Book> books;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre genre)) return false;
        return Objects.equals(id, genre.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

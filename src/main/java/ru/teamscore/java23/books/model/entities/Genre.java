package ru.teamscore.java23.books.model.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor

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

    @JsonIgnore
   // @JsonBackReference
    @ManyToMany(mappedBy = "genres")
    private Set<Book> books = new HashSet<>();

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

    @Override
    public String toString() {
        return Objects.requireNonNullElse(title, "");
    }
}

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
@Table(name = "author", schema = "catalog")
@NoArgsConstructor
@NamedQuery(name = "authorsCount", query = "SELECT count(*) from Author")
@NamedQuery(name = "authorById", query = "from Author as a where a.id = :id")
public class Author {

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName; // имя
    @Column(name = "last_name", nullable = false, length = 64)
    private String lastName; // фамилия

    @Column(name = "middle_name", length = 64)
    private String middleName; // отчество

    private String pseudonym; // псевдоним

    @ManyToMany(mappedBy = "authors")
    @ToString.Exclude
    private Set<Book> books;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Author author)) return false;
        return Objects.equals(id, author.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

package ru.teamscore.java23.books.model.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor

@ToString
@Entity
@Table(name = "customer", schema = "orders")
@NoArgsConstructor
@NamedQuery(name = "customersCount", query = "SELECT count(*) from Customer")
@NamedQuery(name = "customerById", query = "from Customer as a where a.id = :id")
public class Customer {
    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NonNull
    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName; // имя

    @NonNull
    @Column(name = "last_name", nullable = false, length = 64)
    private String lastName; // фамилия

    @Column(name = "middle_name", length = 64)
    private String middleName; // отчество

    private String login;

    @ToString.Exclude
    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private Set<Order> orders = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return Objects.equals(id, customer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

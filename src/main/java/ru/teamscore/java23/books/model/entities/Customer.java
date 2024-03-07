package ru.teamscore.java23.books.model.entities;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor

@ToString
@Entity
@Table(name = "customer", schema = "orders")
@NoArgsConstructor
public class Customer {
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

    private String email; // почта
}

package ru.teamscore.java23.books.model.entities;

import lombok.*;

@Data // toString, equals, hashcode, get/set (при чем set не создается для final полей)
@AllArgsConstructor
@RequiredArgsConstructor
public class Author {
    private final long id;

    @NonNull
    private String firstName; // имя
    @NonNull
    private String lastName; // фамилия

    private String middleName; // отчество
    private String pseudonym; // псевдоним

}

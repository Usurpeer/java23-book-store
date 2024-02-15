package ru.teamscore.java23.books.model.entities;

import lombok.*;

@Data // toString, equals, hashcode, get/set (при чем set не создается для final полей)
@AllArgsConstructor
@Builder // для того чтобы сделать отчество и псевдоним необязательным полем
public class Author {

    private final long id;

    private String firstName; // имя

    private String lastName; // фамилия

    @Builder.Default // пометка, что необязательное поле
    private String middleName = ""; // отчество
    @Builder.Default // пометка, что необязательное поле
    private String pseudonym = ""; // псевдоним

}

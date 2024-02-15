package ru.teamscore.java23.books.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data // toString, equals, hashcode, get/set (при чем set не создается для final полей)
@AllArgsConstructor
@Builder // для того чтобы сделать отчество и почту необязательным полем
public class Customer {
    private final long id;

    private String firstName; // имя

    private String lastName; // фамилия

    @Builder.Default // необязательное поле
    private String middleName = ""; // отчество
    @Builder.Default // необязательное поле
    private String email = ""; // почта
}

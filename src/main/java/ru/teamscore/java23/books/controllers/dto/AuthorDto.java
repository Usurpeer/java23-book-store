package ru.teamscore.java23.books.controllers.dto;

import lombok.Data;

@Data
public class AuthorDto {
    private long id;
    private String firstName; // имя
    private String lastName; // фамилия
    private String middleName; // отчество
    private String pseudonym; // псевдоним
}

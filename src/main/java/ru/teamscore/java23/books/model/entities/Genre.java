package ru.teamscore.java23.books.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@Data // toString, equals, hashcode, get/set (при чем set не создается для final полей)
@EqualsAndHashCode(of = "id") // только по полю id
@AllArgsConstructor
public class Genre {
    private final long id; // id жанра
    private String title; // название жанра
}

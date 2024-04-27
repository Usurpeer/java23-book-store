package ru.teamscore.java23.books.controllers.dto.adminDtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Genre;
import ru.teamscore.java23.books.model.enums.BookStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class BooksDto {
    private long id;

    private String title; // название книги

    private BookStatus status;

    private Double price; // цена книги

    private String publisher; // издательство книги

    private int year; // год публикации книги

    private String genres;

    private String authors;

    private String imageName = "default_book.png";
    public boolean isAvailable() {
        return status.getAvailable().equals("Доступно");
    }
    public BooksDto(long id, String title, BookStatus status, BigDecimal price, String publisher, int year,
                    Set<Genre> genres, Set<Author> authors, String imageName) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.price = price.doubleValue();
        this.publisher = publisher;
        this.year = year;
        this.genres = genresToString(genres);
        this.authors = authorsToString(authors);
        this.imageName = imageName;
    }

    public static String authorsToString(Set<Author> authors) {
        StringBuilder resStr = new StringBuilder();

        List<String> authorNames = authorsToStringList(authors);

        final int maxAuthorsToShow = 3;
        List<String> truncatedAuthorNames = authorNames.subList(0, Math.min(maxAuthorsToShow, authorNames.size()));
        resStr.append(String.join(", ", truncatedAuthorNames));

        if (authorNames.size() > maxAuthorsToShow) {
            resStr.append(" и другие");
        }

        return resStr.toString();
    }

    private static List<String> authorsToStringList(Set<Author> authors) {
        return authors.stream()
                .map((BooksDto::formatAuthorName))
                .toList();
    }

    private static String formatAuthorName(Author author) {
        StringBuilder fullName = new StringBuilder();

        if (author.getLastName() != null) {
            fullName.append(author.getLastName());

            if (author.getFirstName() != null || author.getMiddleName() != null) {
                fullName.append(" ");
            }
        }

        if (author.getFirstName() != null) {
            fullName.append(Character.toUpperCase(author.getFirstName().charAt(0))).append(".");
        }

        if (author.getMiddleName() != null) {
            fullName.append(Character.toUpperCase(author.getMiddleName().charAt(0))).append(".");
        }

        return fullName.toString().trim();
    }

    public static String genresToString(Set<Genre> genres) {
        StringBuilder resStr = new StringBuilder();

        List<String> genreTitles = genresToStringList(genres);

        final int maxGenresToShow = 3;

        String firstGenre = genreTitles.get(0);

        List<String> truncatedGenres = genreTitles.subList(1, Math.min(maxGenresToShow, genreTitles.size()));

        int otherGenresCount = genreTitles.size() - 1;

        final int maxLength = 20;

        if (firstGenre.length() > maxLength) {
            resStr.append(firstGenre).append(" и другие");
        } else {
            resStr.append(firstGenre);
            if (!truncatedGenres.isEmpty()) {
                resStr.append(", ").append(String.join(", ", truncatedGenres));
            }
        }

        if (otherGenresCount > 0 && firstGenre.length() <= maxLength) {
            resStr.append(" и ещё ").append(otherGenresCount);
        }

        return resStr.toString();
    }

    private static List<String> genresToStringList(Set<Genre> genres) {
        return genres.stream()
                .map(Genre::getTitle)
                .toList();
    }
}


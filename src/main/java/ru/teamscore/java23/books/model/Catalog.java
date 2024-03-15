package ru.teamscore.java23.books.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;
import ru.teamscore.java23.books.model.entities.Order;
import ru.teamscore.java23.books.model.enums.CatalogSortOption;

import java.util.*;


@RequiredArgsConstructor
public class Catalog {

    private final EntityManager entityManager;
    @Getter
    private final AuthorManager authorManager = new AuthorManager();
    @Getter
    private final GenreManager genreManager = new GenreManager();

    public long getBooksCount() {
        return entityManager
                .createNamedQuery("booksCount", Long.class)
                .getSingleResult();
    }

    public Optional<Book> getBook(long id) {
        try {
            return Optional.of(entityManager
                    .createNamedQuery("bookById", Book.class)
                    .setParameter("id", id)
                    .getSingleResult());
            // Optional.ofNullable(entityManager.find(Book.class, id));
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    public Optional<Book> getBook(@NonNull Book book) {
        if (entityManager.contains(book)) {
            return Optional.of(book);
        }
        return getBook(book.getId());
    }

    public int addBook(@NonNull Book book) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(book);
            entityManager.getTransaction().commit();

            return 1;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return -1;
        }
    }

    public int updateBook(@NonNull Book book) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(book);
            entityManager.getTransaction().commit();

            return 1;
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            return -1;
        }
    }

    public Set<Author> getAuthorsBook(@NonNull Book book) {
        Optional<@NonNull Book> bookOptional;
        if (entityManager.contains(book)) {
            bookOptional = Optional.of(book);
        } else {
            bookOptional = getBook(book.getId());
        }
        if (bookOptional.isEmpty()) {
            return new HashSet<>();
        }
        return bookOptional.get().getAuthors();
    }

    public Set<Author> getAuthorsBook(long id) {
        Optional<@NonNull Book> bookOptional;
        bookOptional = getBook(id);

        if (bookOptional.isEmpty()) {
            return new HashSet<>();
        }
        return bookOptional.get().getAuthors();
    }

    // страница с нуля считается
    public Collection<Book> getSorted(CatalogSortOption option, boolean desc, int page, int pageSize) {
        var query = entityManager.getCriteriaBuilder().createQuery(Book.class);
        Root<Book> root = query.from(Book.class);

        var sortBy = root.get(option.getColumnName());
        var order = desc
                ? entityManager.getCriteriaBuilder().desc(sortBy)
                : entityManager.getCriteriaBuilder().asc(sortBy);
        query.orderBy(order, entityManager.getCriteriaBuilder().asc(root.get("id")));

        return entityManager
                .createQuery(query)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }

    /*public Set<Genre> getGenresBook(@NonNull Book book) {
        Optional<@NonNull Book> bookOptional;
        if (entityManager.contains(book)) {
            bookOptional = Optional.of(book);
        } else {
            bookOptional = getBook(book.getId());
        }
        if (bookOptional.isEmpty()) {
            return new HashSet<>();
        }
        return bookOptional.get().getGenres();
    }

    public Set<Genre> getGenresBook(long id) {
        Optional<@NonNull Book> bookOptional;
        bookOptional = getBook(id);

        if (bookOptional.isEmpty()) {
            return new HashSet<>();
        }
        return bookOptional.get().getGenres();
    }*/

    public class AuthorManager {
        public long getAuthorsCount() {
            return entityManager
                    .createNamedQuery("authorsCount", Long.class)
                    .getSingleResult();
        }

        public Optional<Author> getAuthor(long id) {
            try {
                return Optional.of(entityManager
                        .createNamedQuery("authorById", Author.class)
                        .setParameter("id", id)
                        .getSingleResult());
                // Optional.of(entityManager.find(Author.class, id));
            } catch (NoResultException e) {
                return Optional.empty();
            }
        }

        public Optional<Author> getAuthor(@NonNull Author author) {
            if (entityManager.contains(author)) {
                return Optional.of(author);
            }
            return getAuthor(author.getId());
        }

        public int addAuthor(@NonNull Author author) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(author);
                entityManager.getTransaction().commit();

                return 1;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                return -1;
            }
        }

        public int updateAuthor(@NonNull Author author) {
            try {
                entityManager.getTransaction().begin();
                entityManager.merge(author);
                entityManager.getTransaction().commit();

                return 1;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                return -1;
            }
        }

       /* public Set<Book> getBooksAuthor(@NonNull Author author) {
            Optional<@NonNull Author> authorOptional;
            if (entityManager.contains(author)) {
                authorOptional = Optional.of(author);
            } else {
                authorOptional = getAuthor(author.getId());
            }
            if (authorOptional.isEmpty()) {
                return new HashSet<>();
            }
            return authorOptional.get().getBooks();
        }

        public Set<Book> getBooksAuthor(long id) {
            Optional<@NonNull Author> authorOptional;
            authorOptional = getAuthor(id);

            if (authorOptional.isEmpty()) {
                return new HashSet<>();
            }
            return authorOptional.get().getBooks();
        }*/
    }

    public class GenreManager {
        public long getGenresCount() {
            return entityManager
                    .createNamedQuery("genresCount", Long.class)
                    .getSingleResult();
        }

        public Optional<Genre> getGenre(long id) {
            try {
                return Optional.of(entityManager
                        .createNamedQuery("genreById", Genre.class)
                        .setParameter("id", id)
                        .getSingleResult());
                // Optional.ofNullable(entityManager.find(Genre.class, id));
            } catch (NoResultException e) {
                return Optional.empty();
            }
        }

        public Optional<Genre> getGenre(@NonNull Genre genre) {
            if (entityManager.contains(genre)) {
                return Optional.of(genre);
            }
            return getGenre(genre.getId());
        }

        public int addGenre(@NonNull Genre genre) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(genre);
                entityManager.getTransaction().commit();

                return 1;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                return -1;
            }

        }

        public int updateGenre(@NonNull Genre genre) {
            try {
                entityManager.getTransaction().begin();
                entityManager.merge(genre);
                entityManager.getTransaction().commit();

                return 1;
            } catch (Exception e) {
                if (entityManager.getTransaction().isActive()) {
                    entityManager.getTransaction().rollback();
                }
                return -1;
            }
        }

        /*public Set<Book> getBooksGenre(@NonNull Genre genre) {
            Optional<@NonNull Genre> genreOptional;
            if (entityManager.contains(genre)) {
                genreOptional = Optional.of(genre);
            } else {
                genreOptional = getGenre(genre.getId());
            }
            if (genreOptional.isEmpty()) {
                return new HashSet<>();
            }
            return genreOptional.get().getBooks();
        }

        public Set<Book> getBooksGenre(long id) {
            Optional<@NonNull Genre> genreOptional;
            genreOptional = getGenre(id);

            if (genreOptional.isEmpty()) {
                return new HashSet<>();
            }
            return genreOptional.get().getBooks();
        }*/
    }
}

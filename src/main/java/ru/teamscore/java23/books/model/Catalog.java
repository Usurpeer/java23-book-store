package ru.teamscore.java23.books.model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;
import ru.teamscore.java23.books.model.enums.BookStatus;
import ru.teamscore.java23.books.model.enums.CatalogSortOption;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
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

    public List<Book> getOpenBooks() {
        return entityManager
                .createQuery("SELECT book FROM Book book WHERE status='OPEN'", Book.class)
                .getResultList();
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

    public List<String> getAllPublishers() {
        return entityManager
                //.createQuery("SELECT DISTINCT book.publisher FROM Book book", String.class)
                .createQuery("SELECT DISTINCT book.publisher FROM Book book ORDER BY book.publisher", String.class)
                .getResultList();
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
    public List<Book> getSorted(CatalogSortOption option, boolean asc, String search, int page, int pageSize) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> query = criteriaBuilder.createQuery(Book.class);

        Root<Book> root = query.from(Book.class);

        root.fetch("authors", JoinType.LEFT);
        root.fetch("genres", JoinType.LEFT);

        // добавил фильтрацию по статусу книги
        Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), BookStatus.OPEN);
        query.where(statusPredicate);

        var sortBy = root.get(option.getColumnName());
        var order = asc
                ? entityManager.getCriteriaBuilder().asc(sortBy)
                : entityManager.getCriteriaBuilder().desc(sortBy);
        query.orderBy(order, entityManager.getCriteriaBuilder().asc(root.get("id")));

        return entityManager
                .createQuery(query)
                .setFirstResult(page * pageSize)
                .setMaxResults(pageSize)
                .getResultList();
    }


    public class AuthorManager {
        public long getAuthorsCount() {
            return entityManager
                    .createNamedQuery("authorsCount", Long.class)
                    .getSingleResult();
        }

        public List<Author> getAllAuthors() {
            return entityManager
                    .createQuery("from Author a order by lastName, firstName, middleName, pseudonym", Author.class)
                    .getResultList();
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
    }

    public class GenreManager {
        public long getGenresCount() {
            return entityManager
                    .createNamedQuery("genresCount", Long.class)
                    .getSingleResult();
        }

        public List<Genre> getAllGenres() {
            return entityManager
                    .createQuery("from Genre order by title", Genre.class)
                    .getResultList();
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
    }
}

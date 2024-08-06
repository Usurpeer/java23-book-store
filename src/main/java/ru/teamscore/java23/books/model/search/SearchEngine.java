package ru.teamscore.java23.books.model.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.store.ByteBuffersDirectory;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;
import ru.teamscore.java23.books.model.search.dto.BookInSearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class SearchEngine {
    private static final String BOOK_TITLE_FIELD = "bookTitle";
    private static final String BOOK_DESCRIPTION_FIELD = "bookDescription";
    private static final String BOOK_AUTHORS_FIELD = "bookAuthors";
    private static final String BOOK_GENRES_FIELD = "bookGenres";
    private static final String BOOK_PUBLISHER_FIELD = "bookPublisher";
    private static final ByteBuffersDirectory index = new ByteBuffersDirectory();

    public static List<BookInSearchView> searchByAll(String search, List<Book> books) {
        List<BookInSearchView> booksInSearch = new ArrayList<>();

        try {
            searching(books, search, booksInSearch);
            normalizeRelevanceScores(booksInSearch);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return booksInSearch.stream().filter(bookInSearchView -> bookInSearchView.getRelevanceScore() > 0.6).toList();
    }

    private static void normalizeRelevanceScores(List<BookInSearchView> booksInSearch) {
        // Находим минимальное и максимальное значение relevance
        double minScore = Double.MAX_VALUE;
        double maxScore = Double.MIN_VALUE;
        for (BookInSearchView bookInView : booksInSearch) {
            double score = bookInView.getRelevanceScore();
            if (score < minScore) {
                minScore = score;
            }
            if (score > maxScore) {
                maxScore = score;
            }
        }

        // Нормализуем значения relevanceScore
        double range = maxScore - minScore;
        if (range == 0) {
            // В случае, если все значения равны, просто устанавливаем для всех 0.5
            for (BookInSearchView bookInView : booksInSearch) {
                bookInView.setRelevanceScore(0.5);
            }
        } else {
            for (BookInSearchView bookInView : booksInSearch) {
                double score = bookInView.getRelevanceScore();
                double normalizedScore = (score - minScore) / range;
                bookInView.setRelevanceScore(normalizedScore);
            }
        }
    }


    /*
    Мера сходства (также известная как функция релевантности) в контексте поисковых систем определяет,
    насколько документ соответствует запросу пользователя. В данном контексте, где используется Apache
    Lucene для поиска книг, "мера сходства" означает алгоритм или модель, которая используется для оценки
    релевантности документа (в данном случае, книги) запросу пользователя.

    В коде, предоставленном вами, используется BM25 (Okapi BM25), который является одним из наиболее
    широко используемых алгоритмов оценки релевантности в поисковых системах. Он учитывает не только
    наличие ключевых слов в документе и их расположение, но также учитывает длину документа и частоту
    встречаемости слова в коллекции документов.

    Мера сходства, такая как BM25, не является нейронной сетью. Это статистический алгоритм, основанный на
    вероятностной модели, который использует математические и статистические методы для оценки релевантности документов.

    BM25 (Okapi BM25) - это формула, разработанная поисковыми инженерами для оценки релевантности документов
    в контексте информационного поиска. Он учитывает различные факторы, такие как частота встречаемости слова
    в документе и коллекции, длина документа и другие статистические характеристики.
    */
    private static void searching(List<Book> books, String search, List<BookInSearchView> booksInSearch) throws IOException {
        // Создание писателя индекса для записи книг в индекс
        IndexWriter writer = createIndexWriter();
        for (Book book : books) {
            // Добавление каждой книги в индекс
            addBookToIndex(writer, book);
        }
        writer.close(); // Закрытие писателя индекса

        // Создание читателя индекса для поиска книг
        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        // Установка меры сходства (BM25) для поисковика
        Similarity similarity = new BM25Similarity();
        searcher.setSimilarity(similarity);

        // Создание пула потоков для параллельного выполнения поиска
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<Future<BookInSearchView>> futures = new ArrayList<>();

        // Для каждой книги в списке книг
        for (Book book : books) {
            // Подача задачи на выполнение в пул потоков
            Future<BookInSearchView> future = executor.submit(() -> {
                // Вычисление релевантности книги по поисковому запросу
                double relevanceScore = calculateRelevance(searcher, search.toLowerCase(), book.getId());
                // Создание объекта представления книги с ее релевантностью
                return new BookInSearchView(book.getId(), relevanceScore);
            });
            // Добавление будущего результата в список будущих результатов
            futures.add(future);
        }

        executor.shutdown(); // Остановка пула потоков после выполнения всех задач

        // Для каждого будущего результата в списке будущих результатов
        for (Future<BookInSearchView> future : futures) {
            try {
                // Добавление результата поиска книги в список книг в поиске
                booksInSearch.add(future.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

        reader.close(); // Закрытие читателя индекса после завершения поиска
    }

    private static IndexWriter createIndexWriter() throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return new IndexWriter(index, config);
    }

    private static void addBookToIndex(IndexWriter writer, Book book) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("bookId", Long.toString(book.getId()), Field.Store.YES));

        doc.add(new TextField(BOOK_TITLE_FIELD, book.getTitle(), Field.Store.YES));
        doc.add(new TextField(BOOK_DESCRIPTION_FIELD, book.getDescription(), Field.Store.YES));

        // Добавляем каждого автора в отдельный терм
        var authors = book.getAuthors();
        for (Author author : authors) {
            doc.add(new TextField(BOOK_AUTHORS_FIELD, author.toString().toLowerCase(), Field.Store.YES));
        }

        // Добавляем каждый жанр в отдельный терм
        var genres = book.getGenres();
        for (Genre genre : genres) {
            doc.add(new TextField(BOOK_GENRES_FIELD, genre.toString().toLowerCase(), Field.Store.YES));
        }

        doc.add(new TextField(BOOK_PUBLISHER_FIELD, book.getPublisher(), Field.Store.YES));

        writer.addDocument(doc);
    }

    private static double calculateRelevance(IndexSearcher searcher, String search, long bookId) throws IOException {
        double relevanceScoreFullPhrase = calculateRelevanceFullPhrase(searcher, search, bookId);
        double relevanceScoreTokens = calculateRelevanceTokens(searcher, search, bookId);

        // Веса для каждого метода поиска
        double weightFullPhrase = 0.65; // Больший вес для поиска по фразе
        double weightTokens = 0.35; // Меньший вес для поиска по токенам

        // Усреднение результатов с учетом весов
        return (relevanceScoreFullPhrase * weightFullPhrase + relevanceScoreTokens * weightTokens);
    }

    private static double calculateRelevanceFullPhrase(IndexSearcher searcher, String search, long bookId) throws IOException {
        Query idQuery = new TermQuery(new Term("bookId", Long.toString(bookId)));

        // Разбиваем поисковый запрос на токены по пробелам
        String[] tokens = search.split("\\s+");

        // Создаем запрос MultiPhraseQuery для каждого поля с учетом всех токенов
        MultiPhraseQuery.Builder titleBuilder = new MultiPhraseQuery.Builder();
        for (String token : tokens) {
            titleBuilder.add(new Term(BOOK_TITLE_FIELD, token));
        }
        MultiPhraseQuery titleMultiPhraseQuery = titleBuilder.build();

        // Повторяем то же самое для остальных полей

        // Комбинируем запросы с помощью boolean query
        BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
        booleanQueryBuilder.add(titleMultiPhraseQuery, BooleanClause.Occur.SHOULD);
        // Добавляем остальные запросы
        booleanQueryBuilder.add(idQuery, BooleanClause.Occur.MUST);

        // Выполняем поиск и получаем результаты
        TopDocs results = searcher.search(booleanQueryBuilder.build(), 1);

        // Возвращаем релевантность первого найденного документа
        if (results.totalHits.value > 0) {
            return results.scoreDocs[0].score;
        }
        return 0.0;
    }


    private static double calculateRelevanceTokens(IndexSearcher searcher, String search, long bookId) throws IOException {
        try {
            Query idQuery = new TermQuery(new Term("bookId", Long.toString(bookId)));

            // Создаем парсеры запросов для каждого поля с разными весами
            Query titleQuery = createFuzzyQuery(BOOK_TITLE_FIELD, search, 1);
            titleQuery = new BoostQuery(titleQuery, 2.0f); // Увеличиваем вес поля заголовка

            Query descriptionQuery = createFuzzyQuery(BOOK_DESCRIPTION_FIELD, search, 1);
            descriptionQuery = new BoostQuery(descriptionQuery, 1.0f); // Увеличиваем вес поля описания

            Query authorsQuery = createFuzzyQuery(BOOK_AUTHORS_FIELD, search, 1);
            authorsQuery = new BoostQuery(authorsQuery, 2.0f); // Увеличиваем вес поля авторов

            Query genresQuery = createFuzzyQuery(BOOK_GENRES_FIELD, search, 1);
            genresQuery = new BoostQuery(genresQuery, 2.0f); // Увеличиваем вес поля жанров

            Query publisherQuery = createFuzzyQuery(BOOK_PUBLISHER_FIELD, search, 1);
            publisherQuery = new BoostQuery(publisherQuery, 2.0f); // Увеличиваем вес поля издательства

            // Комбинируем запросы с помощью boolean query
            BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
            booleanQueryBuilder.add(titleQuery, BooleanClause.Occur.SHOULD);
            booleanQueryBuilder.add(descriptionQuery, BooleanClause.Occur.SHOULD);
            booleanQueryBuilder.add(authorsQuery, BooleanClause.Occur.SHOULD);
            booleanQueryBuilder.add(genresQuery, BooleanClause.Occur.SHOULD);
            booleanQueryBuilder.add(publisherQuery, BooleanClause.Occur.SHOULD);
            booleanQueryBuilder.add(idQuery, BooleanClause.Occur.MUST);

            TopDocs results = searcher.search(booleanQueryBuilder.build(), 1);
            if (results.totalHits.value > 0) {
                return results.scoreDocs[0].score;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        return 0.0;
    }

    // Метод для создания запроса с расплывчатым поиском
    private static Query createFuzzyQuery(String field, String search, int maxEdits) throws ParseException {
        return new FuzzyQuery(new Term(field, search), maxEdits);
    }
}
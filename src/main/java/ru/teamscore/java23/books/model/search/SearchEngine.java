package ru.teamscore.java23.books.model.search;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return booksInSearch;
    }

    private static void searching(List<Book> books, String search, List<BookInSearchView> booksInSearch) throws IOException {
        IndexWriter writer = createIndexWriter();
        for (Book book : books) {
            addBookToIndex(writer, book);
        }
        writer.close();

        IndexReader reader = DirectoryReader.open(index);
        IndexSearcher searcher = new IndexSearcher(reader);
        Similarity similarity = new BM25Similarity();
        searcher.setSimilarity(similarity);

        for (Book book : books) {
            double relevanceScore = calculateRelevance(searcher, search.toLowerCase(), book.getId());
            booksInSearch.add(new BookInSearchView(
                    book.getId(),
                    relevanceScore)
            );
        }

        reader.close();
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

    public static double calculateRelevance(IndexSearcher searcher, String search, long bookId) throws IOException {
        double relevanceScoreFullPhrase = calculateRelevanceFullPhrase(searcher, search, bookId);
        double relevanceScoreTokens = calculateRelevanceTokens(searcher, search, bookId);

        // Веса для каждого метода поиска
        double weightFullPhrase = 0.8; // Больший вес для поиска по фразе
        double weightTokens = 0.2; // Меньший вес для поиска по токенам

        // Усреднение результатов с учетом весов
        return (relevanceScoreFullPhrase * weightFullPhrase + relevanceScoreTokens * weightTokens);
    }

    private static double calculateRelevanceFullPhrase(IndexSearcher searcher, String search, long bookId) throws IOException {
        try {
            Query idQuery = new TermQuery(new Term("bookId", Long.toString(bookId)));

            // Создаем парсеры запросов для каждого поля с разными весами
            QueryParser titleParser = new QueryParser(BOOK_TITLE_FIELD, new StandardAnalyzer());
            Query titleQuery = titleParser.parse("\"" + search + "\"");
            titleQuery = new BoostQuery(titleQuery, 10.0f); // Увеличиваем вес поля заголовка

            QueryParser descriptionParser = new QueryParser(BOOK_DESCRIPTION_FIELD, new StandardAnalyzer());
            Query descriptionQuery = descriptionParser.parse("\"" + search + "\"");
            descriptionQuery = new BoostQuery(descriptionQuery, 2.0f); // Увеличиваем вес поля описания

            QueryParser authorsParser = new QueryParser(BOOK_AUTHORS_FIELD, new StandardAnalyzer());
            Query authorsQuery = authorsParser.parse("\"" + search + "\"");
            authorsQuery = new BoostQuery(authorsQuery, 10.0f); // Увеличиваем вес поля авторов

            QueryParser genresParser = new QueryParser(BOOK_GENRES_FIELD, new StandardAnalyzer());
            Query genresQuery = genresParser.parse("\"" + search + "\"");
            genresQuery = new BoostQuery(genresQuery, 10.0f); // Увеличиваем вес поля жанров

            QueryParser publisherParser = new QueryParser(BOOK_PUBLISHER_FIELD, new StandardAnalyzer());
            Query publisherQuery = publisherParser.parse("\"" + search + "\"");
            publisherQuery = new BoostQuery(publisherQuery, 10.0f); // Увеличиваем вес поля издательства

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

    private static double calculateRelevanceTokens(IndexSearcher searcher, String search, long bookId) throws IOException {
        try {
            Query idQuery = new TermQuery(new Term("bookId", Long.toString(bookId)));

            // Создаем парсеры запросов для каждого поля с разными весами
            QueryParser titleParser = new QueryParser(BOOK_TITLE_FIELD, new StandardAnalyzer());
            Query titleQuery = titleParser.parse(search);
            titleQuery = new BoostQuery(titleQuery, 10.0f); // Увеличиваем вес поля заголовка

            QueryParser descriptionParser = new QueryParser(BOOK_DESCRIPTION_FIELD, new StandardAnalyzer());
            Query descriptionQuery = descriptionParser.parse(search);
            descriptionQuery = new BoostQuery(descriptionQuery, 2.0f); // Увеличиваем вес поля описания

            QueryParser authorsParser = new QueryParser(BOOK_AUTHORS_FIELD, new StandardAnalyzer());
            Query authorsQuery = authorsParser.parse(search);
            authorsQuery = new BoostQuery(authorsQuery, 10.0f); // Увеличиваем вес поля авторов

            QueryParser genresParser = new QueryParser(BOOK_GENRES_FIELD, new StandardAnalyzer());
            Query genresQuery = genresParser.parse(search);
            genresQuery = new BoostQuery(genresQuery, 10.0f); // Увеличиваем вес поля жанров

            QueryParser publisherParser = new QueryParser(BOOK_PUBLISHER_FIELD, new StandardAnalyzer());
            Query publisherQuery = publisherParser.parse(search);
            publisherQuery = new BoostQuery(publisherQuery, 10.0f); // Увеличиваем вес поля издательства

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
}



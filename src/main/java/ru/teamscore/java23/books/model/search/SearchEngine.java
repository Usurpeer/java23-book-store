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
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.search.dto.BookInSearchView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SearchEngine {
    private static final String BOOK_TEXT_FIELD = "bookText"; // Поле для хранения текста книги в индексе
    private static final ByteBuffersDirectory index = new ByteBuffersDirectory(); // Создаем индекс для всех книг

    public static List<BookInSearchView> searchByAll(String search, List<Book> books) {
        List<BookInSearchView> booksInSearch = new ArrayList<>();

        try {
            IndexWriter writer = createIndexWriter();
            for (Book book : books) {
                addBookToIndex(writer, book.getId(), book.toString().toLowerCase());
            }
            writer.close();

            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            Similarity similarity = new BM25Similarity();
            searcher.setSimilarity(similarity);

            QueryParser parser = new QueryParser(BOOK_TEXT_FIELD, new StandardAnalyzer());
            org.apache.lucene.search.Query query = parser.parse(search);
            for (Book book : books) {
                double relevanceScore = calculateRelevance(searcher, search, book.getId());
                booksInSearch.add(new BookInSearchView(book.getId(), book.toString().toLowerCase(), relevanceScore));
            }

            reader.close();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        return booksInSearch;
    }

    private static IndexWriter createIndexWriter() throws IOException {
        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        return new IndexWriter(index, config);
    }

    private static void addBookToIndex(IndexWriter writer, Long bookId, String bookText) throws IOException {
        Document doc = new Document();
        doc.add(new StringField("bookId", Long.toString(bookId), Field.Store.YES)); // Уникальный идентификатор книги
        doc.add(new TextField(BOOK_TEXT_FIELD, bookText, Field.Store.YES));
        writer.addDocument(doc);
    }

    private static double calculateRelevance(IndexSearcher searcher, String search, long bookId) throws IOException {
        try {
            QueryParser parser = new QueryParser(BOOK_TEXT_FIELD, new StandardAnalyzer());
            org.apache.lucene.search.Query query = parser.parse("\"" + search + "\"");

            Query idQuery = new TermQuery(new Term("bookId", Long.toString(bookId)));
            BooleanQuery.Builder booleanQueryBuilder = new BooleanQuery.Builder();
            booleanQueryBuilder.add(query, BooleanClause.Occur.MUST);
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

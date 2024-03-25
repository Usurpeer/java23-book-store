package ru.teamscore.java23.books.model.search;

import org.apache.lucene.index.*;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.search.dto.BookInSearchView;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class SearchEngine {
    private static final String BOOK_TEXT_FIELD = "";

    public static List<BookInSearchView> searchByAll(String search, List<Book> books) {
        List<BookInSearchView> booksInSearch = new ArrayList<>();
        for (Book book : books) {
            booksInSearch.add(new BookInSearchView(book.getId(), book.toString()));
        }

        for (BookInSearchView book : booksInSearch) {
            book.setRelevanceScore(calculateRelevance(search, book.getBookToString()));
        }

        return booksInSearch;
    }

    private static double calculateRelevance(String search, String bookText) {
        Directory index = new RAMDirectory();
        Similarity similarity = new BM25Similarity();

        StandardAnalyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setSimilarity(similarity);
        IndexWriter writer = null;
        try {
            writer = new IndexWriter(index, config);

            Document doc = new Document();
            doc.add(new StringField(BOOK_TEXT_FIELD, bookText, Field.Store.YES));
            writer.addDocument(doc);
            writer.close();

            IndexReader reader = DirectoryReader.open(index);
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(similarity);

            QueryParser parser = new QueryParser(BOOK_TEXT_FIELD, analyzer);
            org.apache.lucene.search.Query query = null;
            try {
                query = parser.parse(search);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            TopDocs results = searcher.search(query, 10);

            double relevanceScore = 0.0;
            if (results.totalHits.value > 0) {
                relevanceScore = results.scoreDocs[0].score;
            }

            reader.close();
            return relevanceScore;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

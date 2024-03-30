package ru.teamscore.java23.books.model.search;

import ru.teamscore.java23.books.model.search.dto.BookInSearchView;

import java.util.List;

public class LimiterSearchResult {

    public static List<BookInSearchView> filterResultValues(List<BookInSearchView> booksInSearch) {
        double diversity = calculateDiversity(booksInSearch);
        double normalizedMaxRelevance = normalizeMaxRelevance(booksInSearch);
        double weightedMaxRelevance = weightedMaxRelevance(normalizedMaxRelevance);
        double threshold = adaptThreshold(weightedMaxRelevance, diversity);
        var a = booksInSearch.stream()
                .filter(bookInSearchView -> bookInSearchView.getRelevanceScore() >= threshold &&
                        bookInSearchView.getRelevanceScore() >= 2.44)
                .toList();
        return a;
    }

    private static double calculateDiversity(List<BookInSearchView> booksInSearch) {
        // Вычисляем разнообразие результатов, например, посчитаем дисперсию релевантности
        return calculateDispersion(booksInSearch);
    }

    private static double normalizeMaxRelevance(List<BookInSearchView> booksInSearch) {
        // Нормализуем максимальное значение релевантности к отрезку [0, 1]
        double maxRelevance = booksInSearch.stream()
                .mapToDouble(BookInSearchView::getRelevanceScore)
                .max()
                .orElse(0.0);
        double minRelevance = booksInSearch.stream()
                .mapToDouble(BookInSearchView::getRelevanceScore)
                .min()
                .orElse(0.0);
        return (maxRelevance - minRelevance) > 0 ? (maxRelevance - minRelevance) : 0;
    }

    private static double weightedMaxRelevance(double normalizedMaxRelevance) {
        // Применяем вес к нормализованному максимальному значению релевантности
        return normalizedMaxRelevance / 5.0; // Делим на 5
    }

    private static double adaptThreshold(double weightedMaxRelevance, double diversity) {
        // Адаптируем пороговое значение на основе взвешенного максимального значения релевантности и разнообразия результатов
        return weightedMaxRelevance / (diversity + 1);
    }

    private static double calculateDispersion(List<BookInSearchView> booksInSearch) {
        // Проверяем, что список не пустой и содержит хотя бы два элемента
        if (booksInSearch == null || booksInSearch.size() < 2) {
            throw new IllegalArgumentException("List must contain at least two elements");
        }

        // Вычисляем среднее значение релевантности
        double sumOfSquares = getSumOfSquares(booksInSearch);

        // Вычисляем дисперсию
        return sumOfSquares / (booksInSearch.size() - 1);
    }

    private static double getSumOfSquares(List<BookInSearchView> booksInSearch) {
        double sum = 0;
        for (BookInSearchView book : booksInSearch) {
            sum += book.getRelevanceScore();
        }
        double average = sum / booksInSearch.size();

        // Вычисляем сумму квадратов отклонений от среднего
        double sumOfSquares = 0;
        for (BookInSearchView book : booksInSearch) {
            double deviation = book.getRelevanceScore() - average;
            sumOfSquares += deviation * deviation;
        }
        return sumOfSquares;
    }
}
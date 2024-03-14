package ru.teamscore.java23.books.model.search;

import java.util.*;

public class Searcher {
    // Метод для вычисления расстояния Левенштейна между двумя строками
    private static int levenshteinDistance(String s1, String s2) {
        int[][] dp = new int[s1.length() + 1][s2.length() + 1];

        for (int i = 0; i <= s1.length(); i++) {
            for (int j = 0; j <= s2.length(); j++) {
                if (i == 0)
                    dp[i][j] = j;
                else if (j == 0)
                    dp[i][j] = i;
                else
                    dp[i][j] = s1.charAt(i - 1) == s2.charAt(j - 1) ? dp[i - 1][j - 1] :
                            1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
            }
        }
        return dp[s1.length()][s2.length()];
    }

    // Метод для выполнения поиска и сортировки результатов по релевантности
    public static HashMap<Integer, String> searchAndSort(String query, HashMap<Integer, String> hashMap) {
        HashMap<Integer, Integer> relevanceMap = new HashMap<>(); // Карта для хранения релевантности
        ArrayList<Map.Entry<Integer, String>> entries = new ArrayList<>(hashMap.entrySet());

        // Преобразуем запрос и строки в нижний регистр для регистронезависимого сравнения
        query = query.toLowerCase();
        for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            entry.setValue(entry.getValue().toLowerCase());
        }

        // Вычисляем релевантность для каждой строки
        for (Map.Entry<Integer, String> entry : entries) {
            int relevance = levenshteinDistance(query, entry.getValue());
            relevanceMap.put(entry.getKey(), relevance);
        }

        // Сортируем результаты по релевантности
        entries.sort(Comparator.comparingInt(o -> relevanceMap.get(o.getKey())));

        // Формируем отсортированный HashMap
        HashMap<Integer, String> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, String> entry : entries) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        return sortedMap;
    }

    // Метод для добавления новой строки в поисковый индекс
    public static void addToIndex(int id, String string, HashMap<Integer, String> hashMap) {
        hashMap.put(id, string.toLowerCase()); // Преобразуем в нижний регистр для регистронезависимого сравнения
    }

    // Метод для удаления строки из поискового индекса
    public static void removeFromIndex(int id, HashMap<Integer, String> hashMap) {
        hashMap.remove(id);
    }
}

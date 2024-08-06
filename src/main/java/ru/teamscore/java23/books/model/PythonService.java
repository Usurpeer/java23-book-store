package ru.teamscore.java23.books.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.teamscore.java23.books.model.search.dto.BookToJson;

import java.util.List;
import java.util.Map;

@Service
public class PythonService {
    private final RestTemplate restTemplate;

    @Autowired
    public PythonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Map<Long, Double> sendToPyth1(String search, List<BookToJson> books) {
        String url = "http://localhost:8081/api/pyth-1-search/" + search;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Отправляем POST-запрос с данными книг в теле запроса
        HttpEntity<List<BookToJson>> requestEntity = new HttpEntity<>(books, headers);

        // Отправляем POST-запрос и ожидаем ответ в виде списка книг
        ResponseEntity<Map<Long, Double>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<Long, Double>>() {
                });

        // Извлекаем список книг из ответа
        Map<Long, Double> foundBooks = responseEntity.getBody();

        return foundBooks;
    }

    public Map<Long, Double> sendToPyth2(String search, List<BookToJson> books) {
        String url = "http://localhost:8081/api/pyth-2-search/" + search;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Отправляем POST-запрос с данными книг в теле запроса
        HttpEntity<List<BookToJson>> requestEntity = new HttpEntity<>(books, headers);

        // Отправляем POST-запрос и ожидаем ответ в виде списка книг
        ResponseEntity<Map<Long, Double>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<Long, Double>>() {
                });

        // Извлекаем список книг из ответа
        Map<Long, Double> foundBooks = responseEntity.getBody();

        return foundBooks;
    }
}

package ru.teamscore.java23.books.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.teamscore.java23.books.model.entities.Book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RestToPythonService {
    private final RestTemplate restTemplate;

    @Autowired
    public RestToPythonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Book> sendToPyth1(String search, List<Book> books){
        String url = "http://localhost:8081/api/pyth-1-search/" + search;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Отправляем POST-запрос с данными книг в теле запроса
        HttpEntity<List<Book>> requestEntity = new HttpEntity<>(books, headers);

        // Отправляем POST-запрос и ожидаем ответ в виде списка книг
        ResponseEntity<List<Book>> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<List<Book>>() {});

        // Извлекаем список книг из ответа
        List<Book> foundBooks = responseEntity.getBody();

        return foundBooks;
    }
   /* public String sendToPythTest(String search){
        String url = "http://localhost:8081/api/pyth-1-search/" + search;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // Создаем пустое тело запроса, так как нам не нужны данные книг
        HttpEntity<?> requestEntity = new HttpEntity<>(headers);

        // Отправляем POST-запрос и ожидаем ответа в виде строки
        ResponseEntity<String> responseEntity = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class);

        // Извлекаем строку из ответа
        String response = responseEntity.getBody();

        return response;
    }*/
}

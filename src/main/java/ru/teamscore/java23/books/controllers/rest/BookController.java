package ru.teamscore.java23.books.controllers.rest;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.teamscore.java23.books.controllers.dto.book.BookDto;
import ru.teamscore.java23.books.model.Catalog;
import ru.teamscore.java23.books.model.entities.Book;

import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/api/book")
public class BookController {
    @Autowired
    private final Catalog catalog;
    @Autowired
    private final ModelMapper modelMapper;

    @GetMapping
    public ResponseEntity<BookDto> getBookById(@RequestParam Long id) {
        Optional<Book> book = catalog.getBook(id);
        if (book.isPresent()) {
            BookDto bookPageDto = modelMapper.map(book.get(), BookDto.class);
            return ResponseEntity.ok(bookPageDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

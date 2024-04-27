package ru.teamscore.java23.books.controllers;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.teamscore.java23.books.controllers.dto.adminDtos.BooksDto;
import ru.teamscore.java23.books.controllers.dto.catalog.CatalogRequestDto;
import ru.teamscore.java23.books.model.Catalog;
import ru.teamscore.java23.books.model.entities.Author;
import ru.teamscore.java23.books.model.entities.Book;
import ru.teamscore.java23.books.model.entities.Genre;
import ru.teamscore.java23.books.model.enums.BookStatus;
import ru.teamscore.java23.books.model.enums.CatalogSortOption;
import ru.teamscore.java23.books.model.search.SearchManager;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/adminpanel")
public class AdminBooksController {
    private Catalog catalog;
    private ModelMapper modelMapper;

    @Autowired
    public AdminBooksController(Catalog catalog, ModelMapper modelMapper) {
        this.catalog = catalog;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public String get(Model model,
                      @RequestParam(required = false) String sorting,
                      @RequestParam(required = false) String search,
                      @RequestParam(required = false) Integer page,
                      @RequestParam(required = false) Integer pageSize
    ) {
        if (pageSize == null || pageSize <= 0) {
            pageSize = 6;
        }


        CatalogSortOption sortOption = (sorting == null)
                ? CatalogSortOption.TITLE
                : CatalogSortOption.valueOf(sorting
                .toUpperCase()
                .replace("ASC", "")
                .replace("DESC", "")
        );
        var books = catalog.getBooks();
        boolean asc = sorting != null && sorting.toLowerCase().endsWith("asc");
        SearchManager searchManager = new SearchManager(sortOption, asc, search, page, pageSize, books);

        var sortedBooks = searchManager.getBooks();

        long pagesCount = searchManager.getBooksInSearchQuantity() / pageSize +
                (searchManager.getBooksInSearchQuantity() % pageSize > 0 ? 1 : 0);
        if (page == null || page <= 0) {
            page = 1;
        } else if (page > pagesCount) {
            page = (int) pagesCount;
        }

        var booksForAdmin = mappingToRequest(sortedBooks);
        model.addAttribute("books", booksForAdmin);
        model.addAttribute("pagesCount", pagesCount);
        model.addAttribute("currentPage", page);
        return "/adminpanel/index";
    }

    private List<BooksDto> mappingToRequest(List<Book> books) {
        return books.stream()
                .map(book -> new BooksDto(book.getId(), book.getTitle(), book.getStatus(), book.getPrice(),
                        book.getPublisher(), book.getYear(),
                        book.getGenres(), book.getAuthors(), book.getImageName()))
                .toList();
    }
}

package sumdu.edu.ua.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;

@Controller
@RequestMapping("/books")
public class BooksController {

    private final CatalogRepositoryPort bookRepo;

    public BooksController(CatalogRepositoryPort bookRepo) {
        this.bookRepo = bookRepo;
    }

    // Відображення списку книг
    @GetMapping
    public String getAllBooks(Model model) {
        var books = bookRepo.search(null, new PageRequest(0, 20)).getItems();
        model.addAttribute("books", books);
        return "books"; // Шукає templates/books.html
    }

    // Покази форми додавання нової книги
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("bookForm", new Book());
        return "book-add"; // Шукає templates/book-add.html
    }

    // Обробка форми додавання книги
    @PostMapping
    public String addBook(@ModelAttribute("bookForm") Book book) {
        bookRepo.add(book.getTitle(), book.getAuthor(), book.getPubYear());
        return "redirect:/books";

    }

}
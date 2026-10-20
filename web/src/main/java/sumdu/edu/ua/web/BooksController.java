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
    private final MailService mailService; // Додали сервіс

    public BooksController(CatalogRepositoryPort bookRepo, MailService mailService) {
        this.bookRepo = bookRepo;
        this.mailService = mailService; // Ініціалізуємо
    }

    @GetMapping
    public String getAllBooks(Model model) {
        var books = bookRepo.search(null, new PageRequest(0, 20)).getItems();
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("bookForm", new Book());
        return "book-add";
    }

    @PostMapping
    public String addBook(@ModelAttribute("bookForm") Book book) {
        // Зберігаємо книгу в базу
        bookRepo.add(book.getTitle(), book.getAuthor(), book.getPubYear());

        // Відправляємо лист
        mailService.sendNewBookEmail(book);

        return "redirect:/books";
    }
}
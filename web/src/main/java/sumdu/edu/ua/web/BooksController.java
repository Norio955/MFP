package sumdu.edu.ua.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.Page;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.core.port.CommentRepositoryPort;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/books")
public class BooksController {

    private static final Logger log = LoggerFactory.getLogger(BooksController.class);
    private final CatalogRepositoryPort bookRepo;
    private final CommentRepositoryPort commentRepo;

    // Spring автоматично ін'єктує залежності через конструктор
    public BooksController(CatalogRepositoryPort bookRepo, CommentRepositoryPort commentRepo) {
        this.bookRepo = bookRepo;
        this.commentRepo = commentRepo;
    }

    // Отримання списку книг (заміна BooksServlet та GET BooksApiServlet)
    @GetMapping
    public ResponseEntity<Page<Book>> getBooks(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        try {
            Page<Book> result = bookRepo.search(q, new PageRequest(page, size));
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            log.error("DB error while GET /books", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Деталі однієї книги з коментарями (заміна BookDetailsServlet)
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookDetails(@PathVariable("id") long id) {
        try {
            Book book = bookRepo.findById(id);
            if (book == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
            }
            var commentsPage = commentRepo.list(id, null, null, new PageRequest(0, 50));

            // Формуємо спільну JSON-відповідь
            Map<String, Object> response = new HashMap<>();
            response.put("book", book);
            response.put("comments", commentsPage.getItems());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Error loading book details for id: " + id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Додавання нової книги (заміна POST BooksApiServlet)
    @PostMapping
    public ResponseEntity<?> addBook(@RequestBody Book book) {
        if (book.getTitle() == null || book.getTitle().isBlank() || book.getAuthor() == null || book.getAuthor().isBlank()) {
            return ResponseEntity.badRequest().body("title & author required");
        }
        if (book.getPubYear() <= 0) {
            return ResponseEntity.badRequest().body("invalid pubYear");
        }
        try {
            Book saved = bookRepo.add(book.getTitle().trim(), book.getAuthor().trim(), book.getPubYear());
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            log.error("DB error while POST /books", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB error");
        }
    }
}
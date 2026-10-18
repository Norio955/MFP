package sumdu.edu.ua.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.core.service.CommentService;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    private static final Logger log = LoggerFactory.getLogger(CommentsController.class);
    private final CommentRepositoryPort commentRepo;
    private final CommentService commentService;

    public CommentsController(CommentRepositoryPort commentRepo) {
        this.commentRepo = commentRepo;
        this.commentService = new CommentService(commentRepo);
    }

    // Отримання коментарів до книги
    @GetMapping
    public ResponseEntity<?> getComments(@RequestParam long bookId) {
        try {
            var commentsPage = commentRepo.list(bookId, null, null, new PageRequest(0, 10));
            return ResponseEntity.ok(commentsPage);
        } catch (Exception e) {
            log.error("DB error while GET /comments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB error");
        }
    }

    // Створення нового коментаря
    @PostMapping
    public ResponseEntity<?> addComment(@RequestBody CommentRequest req) {
        if (req.author() == null || req.author().isBlank() || req.text() == null || req.text().isBlank()) {
            return ResponseEntity.badRequest().body("author & text required");
        }
        if (req.author().length() > 64 || req.text().length() > 1000) {
            return ResponseEntity.badRequest().body("too long");
        }
        try {
            commentRepo.add(req.bookId(), req.author().trim(), req.text().trim());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            log.error("DB error while POST /comments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DB error");
        }
    }

    // Видалення коментаря
    @DeleteMapping
    public ResponseEntity<?> deleteComment(@RequestParam long bookId, @RequestParam long commentId) {
        try {
            commentService.deleteComment(bookId, commentId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            log.error("Error while DELETE /comments", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Server error");
        }
    }
}
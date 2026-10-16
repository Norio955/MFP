package sumdu.edu.ua.web;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CommentRepositoryPort;

import java.util.Map;

public class CommentsController {

    private static final Logger log = LoggerFactory.getLogger(CommentsController.class);
    private final CommentRepositoryPort commentRepo;

    public CommentsController(CommentRepositoryPort commentRepo) {
        this.commentRepo = commentRepo;
    }

    public void registerRoutes(Javalin app) {
        // Отримання коментарів до книги
        app.get("/api/books/{id}/comments", this::getComments);
        // Додавання коментаря
        app.post("/api/comments", this::createComment);

        // Видалення коментаря за його ID та ID книги
        app.delete("/api/books/{bookId}/comments/{commentId}", this::deleteComment);
    }

    private void getComments(Context ctx) {
        long bookId = Long.parseLong(ctx.pathParam("id"));
        var comments = commentRepo.list(bookId, null, null, new PageRequest(0, 20)).getItems();
        ctx.json(comments);
    }

    private void createComment(Context ctx) {
        try {
            var body = ctx.bodyAsClass(CommentRequestDto.class);

            if (body.author() == null || body.author().isBlank() ||
                    body.text() == null || body.text().isBlank()) {
                ctx.status(400).json(Map.of("error", "Author and text are required"));
                return;
            }

            commentRepo.add(body.bookId(), body.author().trim(), body.text().trim());
            ctx.status(201).json(Map.of("status", "Created"));
        } catch (Exception e) {
            log.error("Error creating comment", e);
            ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }

    // Обробник для DELETE-запиту
    private void deleteComment(Context ctx) {
        try {
            long bookId = Long.parseLong(ctx.pathParam("bookId"));
            long commentId = Long.parseLong(ctx.pathParam("commentId"));

            commentRepo.delete(bookId, commentId);

            // Повертаємо статус 204 No Content (успішно видалено, без тіла відповіді)
            ctx.status(204);
        } catch (Exception e) {
            log.error("Error deleting comment", e);
            ctx.status(500).json(Map.of("error", "Internal server error"));
        }
    }

    public record CommentRequestDto(long bookId, String author, String text) {}
}
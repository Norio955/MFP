package sumdu.edu.ua.core.service;

import sumdu.edu.ua.core.domain.Comment;
import sumdu.edu.ua.core.port.CommentRepositoryPort;

import java.time.Duration;
import java.time.Instant;

public class CommentService {
    private final CommentRepositoryPort commentRepository;

    public CommentService(CommentRepositoryPort commentRepository) {
        this.commentRepository = commentRepository;
    }

    public void deleteComment(long bookId, long commentId) {
        // Шукаємо коментар за ідентифікатором
        Comment comment = commentRepository.findCommentById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Коментар не знайдено з ID: " + commentId));

        // Бізнес-правило: коментар можна видалити лише протягом 24 годин
        Instant now = Instant.now();
        Duration duration = Duration.between(comment.getCreatedAt(), now);

        if (duration.toHours() > 24) {
            throw new IllegalStateException("Коментар можна видалити лише протягом 24 годин з моменту створення.");
        }

        // Видаляємо коментар через порт репозиторію
        commentRepository.delete(bookId, commentId);
    }
}
package sumdu.edu.ua.core.port;

import sumdu.edu.ua.core.domain.Comment;
import sumdu.edu.ua.core.domain.Page;
import sumdu.edu.ua.core.domain.PageRequest;

import java.time.Instant;
import java.util.Optional;

public interface CommentRepositoryPort {
    void add(long bookId, String author, String text);
    Page<Comment> list(long bookId, String author, Instant since, PageRequest request);

    // Додаємо методи, потрібні для сервісу перевірки 24 годин
    Optional<Comment> findCommentById(long commentId);
    void delete(long bookId, long commentId);
}
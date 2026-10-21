package sumdu.edu.ua.persistence.adapter;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.Comment;
import sumdu.edu.ua.core.domain.User;
import sumdu.edu.ua.core.domain.Page;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.persistence.repo.BookJpaRepository;
import sumdu.edu.ua.persistence.repo.CommentJpaRepository;
import sumdu.edu.ua.persistence.repo.UserJpaRepository;

import java.time.Instant;
import java.util.Optional;

@Component
@Transactional
public class JpaCommentAdapter implements CommentRepositoryPort {

    private final CommentJpaRepository commentRepo;
    private final BookJpaRepository bookRepo;
    private final UserJpaRepository userRepo;

    public JpaCommentAdapter(CommentJpaRepository commentRepo, BookJpaRepository bookRepo, UserJpaRepository userRepo) {
        this.commentRepo = commentRepo;
        this.bookRepo = bookRepo;
        this.userRepo = userRepo;
    }

    @Override
    public void add(long bookId, String author, String text) {
        Book book = bookRepo.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        // Знаходимо користувача, або створюємо нового
        User user = userRepo.findByUsername(author)
                .orElseGet(() -> userRepo.save(new User(author, "default", "ROLE_USER")));

        Comment comment = new Comment(book, user, text);
        commentRepo.save(comment);
    }

    @Override
    public Page<Comment> list(long bookId, String author, Instant since, sumdu.edu.ua.core.domain.PageRequest request) {
        // Дістаємо коментарі для конкретної книги, відсортовані за часом
        var comments = commentRepo.findByBookIdOrderByCreatedAtDesc(bookId);
        return new Page<>(comments, request, comments.size());
    }

    @Override
    public void delete(long bookId, long commentId) {
        commentRepo.findById(commentId).ifPresent(c -> {
            if (c.getBook().getId() == bookId) {
                commentRepo.delete(c);
            }
        });
    }

    @Override
    public Optional<Comment> findCommentById(long id) {
        return commentRepo.findById(id);
    }

    @Override
    public java.util.List<Comment> findByUserId(Long userId) {
        return commentRepo.findByUserIdOrderByCreatedAtDesc(userId);
    }
}
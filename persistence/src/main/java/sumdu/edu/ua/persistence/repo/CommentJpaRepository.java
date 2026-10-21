package sumdu.edu.ua.persistence.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sumdu.edu.ua.core.domain.Comment;
import java.util.List;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByUserIdOrderByCreatedAtDesc(Long userId);
    List<Comment> findByBookIdOrderByCreatedAtDesc(Long bookId);
}
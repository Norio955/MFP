package sumdu.edu.ua.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sumdu.edu.ua.config.Beans;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.core.service.CommentService;

import java.io.IOException;

@MultipartConfig
public class CommentsServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(CommentsServlet.class);

    private final CommentRepositoryPort commentRepository = Beans.getCommentRepo(); // Используем Beans фабрику
    private final CommentService commentService = new CommentService(commentRepository);
    private final ObjectMapper om = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            String bookIdParam = req.getParameter("bookId");
            if (bookIdParam == null || bookIdParam.isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "bookId required");
                return;
            }
            long bookId = Long.parseLong(bookIdParam);

            var commentsPage = commentRepository.list(bookId, null, null, new PageRequest(0, 10));
            om.writeValue(resp.getWriter(), commentsPage);
        } catch (Exception e) {
            log.error("DB error while GET /comments", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB error");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");

        // Перевіряємо, чи це запит на видалення з JSP-форми через POST
        String action = req.getParameter("action");
        if ("delete".equals(action)) {
            String bookIdParam = req.getParameter("bookId");
            String commentIdParam = req.getParameter("commentId");

            if (bookIdParam == null || commentIdParam == null) {
                log.warn("Validation failed: missing bookId or commentId for deletion");
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "bookId & commentId required");
                return;
            }

            try {
                long bookId = Long.parseLong(bookIdParam);
                long commentId = Long.parseLong(commentIdParam);

                // Викликаємо сервіс з бізнес-правилом 24 годин
                commentService.deleteComment(bookId, commentId);

                // INFO лог при успішному видаленні з ключовими полями
                log.info("Successfully deleted comment with id: {} for bookId: {}", commentId, bookId);

                // Після успішного видалення оновлюємо сторінку книги
                resp.sendRedirect(req.getContextPath() + "/book?id=" + bookId);
                return;

            } catch (IllegalArgumentException e) {
                log.warn("Comment not found: {}", e.getMessage());
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
                return;
            } catch (IllegalStateException e) {
                log.warn("Deletion restriction violated (24h rule): {}", e.getMessage());
                resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage()); // 409 Conflict
                return;
            } catch (Exception e) {
                log.error("Error while deleting comment", e);
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
                return;
            }
        }

        // --- Стандартна логіка додавання коментаря ---
        String bookIdParam = req.getParameter("bookId");
        String author = req.getParameter("author");
        String text   = req.getParameter("text");

        if (bookIdParam == null || author == null || author.isBlank() || text == null || text.isBlank()) {
            log.warn("Validation failed: missing bookId, author or text for comment creation");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "bookId, author & text required");
            return;
        }
        if (author.length() > 64 || text.length() > 1000) {
            log.warn("Validation failed: author or text exceeds maximum length");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "too long");
            return;
        }

        try {
            long bookId = Long.parseLong(bookIdParam);
            String trimmedAuthor = author.trim();
            commentRepository.add(bookId, trimmedAuthor, text.trim());

            // INFO лог при успішному створенні з ключовими полями
            log.info("Successfully created comment for bookId: {}, author: '{}'", bookId, trimmedAuthor);

            // Після додавання також повертаємо користувача на сторінку книги
            resp.sendRedirect(req.getContextPath() + "/book?id=" + bookId);
        } catch (Exception e) {
            log.error("DB error while POST /comments", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB error");
        }
    }

    // Підтримка прямого методу DELETE за потреби
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String bookIdParam = req.getParameter("bookId");
        String commentIdParam = req.getParameter("commentId");

        if (bookIdParam == null || commentIdParam == null) {
            log.warn("Validation failed: missing bookId or commentId in doDelete");
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "bookId & commentId required");
            return;
        }

        try {
            long bookId = Long.parseLong(bookIdParam);
            long commentId = Long.parseLong(commentIdParam);

            // Викликаємо сервіс, який перевіряє правило 24 годин
            commentService.deleteComment(bookId, commentId);
            log.info("Successfully deleted comment via DELETE method with id: {} for bookId: {}", commentId, bookId);
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (IllegalArgumentException e) {
            log.warn("Comment not found: {}", e.getMessage());
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (IllegalStateException e) {
            log.warn("Deletion restriction violated (24h rule): {}", e.getMessage());
            resp.sendError(HttpServletResponse.SC_CONFLICT, e.getMessage()); // 409 Conflict
        } catch (Exception e) {
            log.error("Error while DELETE /comments", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }
}
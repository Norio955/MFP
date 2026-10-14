package sumdu.edu.ua.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.persistence.jdbc.JdbcCommentRepository;

import java.io.IOException;

@MultipartConfig
public class CommentsServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(CommentsServlet.class);

    // Використовуємо порт замість прямого DAO для дотримання чистої архітектури
    private final CommentRepositoryPort commentRepository = new JdbcCommentRepository();
    private final ObjectMapper om = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json;charset=UTF-8");
        try {
            // Отримуємо ID книги з параметрів запиту
            String bookIdParam = req.getParameter("bookId");
            if (bookIdParam == null || bookIdParam.isBlank()) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "bookId required");
                return;
            }
            long bookId = Long.parseLong(bookIdParam);

            // Викликаємо метод репозиторія згідно з новими вимогами інтерфейсу (передаємо bookId та пагінацію)
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

        // Зчитуємо дані з форми або запиту (включно з bookId)
        String bookIdParam = req.getParameter("bookId");
        String author = req.getParameter("author");
        String text   = req.getParameter("text");

        // Валідація вхідних даних
        if (bookIdParam == null || author == null || author.isBlank() || text == null || text.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "bookId, author & text required");
            return;
        }
        if (author.length() > 64 || text.length() > 1000) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "too long");
            return;
        }

        try {
            long bookId = Long.parseLong(bookIdParam);
            // Зберігаємо коментар через репозиторій, передаючи всі три необхідні параметри
            commentRepository.add(bookId, author.trim(), text.trim());
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (Exception e) {
            log.error("DB error while POST /comments", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "DB error");
        }
    }
}
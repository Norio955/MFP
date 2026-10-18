package sumdu.edu.ua.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.core.port.CommentRepositoryPort;

import java.io.IOException;

public class BookDetailsServlet extends HttpServlet {

    private final CatalogRepositoryPort bookRepo;
    private final CommentRepositoryPort commentRepo;

    // Конструктор для Spring DI (отримує репозиторії автоматично з контейнера)
    public BookDetailsServlet(CatalogRepositoryPort bookRepo, CommentRepositoryPort commentRepo) {
        this.bookRepo = bookRepo;
        this.commentRepo = commentRepo;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required");
            return;
        }

        try {
            long bookId = Long.parseLong(idParam);
            Book book = bookRepo.findById(bookId);

            if (book == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                return;
            }

            // Отримуємо список коментарів для цієї книги
            var commentsPage = commentRepo.list(bookId, null, null, new PageRequest(0, 50));

            req.setAttribute("book", book);
            req.setAttribute("comments", commentsPage.getItems());

            req.getRequestDispatcher("/WEB-INF/views/book-comments.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid Book ID format");
        } catch (Exception e) {
            throw new ServletException("Error loading book details", e);
        }
    }
}
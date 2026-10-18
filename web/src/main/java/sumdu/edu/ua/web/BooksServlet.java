package sumdu.edu.ua.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sumdu.edu.ua.core.domain.Book;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;

import java.io.IOException;
import java.util.List;

@WebServlet("/books/*")
public class BooksServlet extends HttpServlet {

    private static final Logger log = LoggerFactory.getLogger(BooksServlet.class);
    private final CatalogRepositoryPort bookRepo;

    public BooksServlet(CatalogRepositoryPort bookRepo) {
        this.bookRepo = bookRepo;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            PageRequest pageRequest = new PageRequest(0, 20);
            List<Book> books = bookRepo.search(null, pageRequest).getItems();

            req.setAttribute("books", books);
            req.getRequestDispatcher("/WEB-INF/views/books.jsp").forward(req, resp);
        } catch (Exception e) {
            log.error("Error in BooksServlet doGet", e); // Виведе повний стек помилки в консоль
            throw new ServletException(e);
        }
    }
}
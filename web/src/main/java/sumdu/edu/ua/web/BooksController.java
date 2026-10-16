package sumdu.edu.ua.web;

import io.javalin.Javalin;
import io.javalin.http.Context;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.core.domain.PageRequest;

public class BooksController {

    private final CatalogRepositoryPort bookRepo;

    public BooksController(CatalogRepositoryPort bookRepo) {
        this.bookRepo = bookRepo;
    }

    public void registerRoutes(Javalin app) {
        // Тільки GET маршрути для книг
        app.get("/api/books", this::getBooks);
        app.get("/api/books/{id}", this::getBook);
    }

    private void getBooks(Context ctx) {
        var books = bookRepo.search("", new PageRequest(0, 20)).getItems();
        ctx.json(books);
    }

    private void getBook(Context ctx) {
        long id = Long.parseLong(ctx.pathParam("id"));
        var book = bookRepo.findById(id);
        ctx.json(book);
    }
}
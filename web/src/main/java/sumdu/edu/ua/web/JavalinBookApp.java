package sumdu.edu.ua.web;

import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sumdu.edu.ua.config.Beans;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.persistence.jdbc.DbInit;

import java.util.Map;

public class JavalinBookApp {

    private static final Logger log = LoggerFactory.getLogger(JavalinBookApp.class);

    public static void main(String[] args) {

        // 1. Ініціалізація бази даних та створення таблиць через schema.sql
        DbInit.init();

        // 2. Створення застосунку Javalin з підтримкою статичних файлів (Крок 7)
        var app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);
        });

        // 3. Middleware: логування всіх вхідних запитів (Крок 5)
        app.before(ctx -> {
            log.info("HTTP Request: {} {}", ctx.method(), ctx.path());
        });

        // 4. Отримання репозиторіїв та реєстрація контролерів (Крок 3)
        CatalogRepositoryPort bookRepo = Beans.getBookRepo();
        new BooksController(bookRepo).registerRoutes(app);

        CommentRepositoryPort commentRepo = Beans.getCommentRepo();
        new CommentsController(commentRepo).registerRoutes(app);

        // 5. Централізована обробка винятків (Крок 6)
        app.exception(Exception.class, (e, ctx) -> {
            log.error("Unhandled exception occurred", e);
            ctx.status(500).json(Map.of("error", "Internal server error: " + e.getMessage()));
        });

        // 6. Запуск сервера на порту 8080
        app.start(8080);
        log.info("Javalin app started on http://localhost:8080");
    }
}
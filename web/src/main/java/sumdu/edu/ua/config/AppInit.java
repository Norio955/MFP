package sumdu.edu.ua.config;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import sumdu.edu.ua.persistence.jdbc.DbInit;

@WebListener
public class AppInit implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        DbInit.init(); // Викликає створення таблиць із schema.sql
        System.out.println("\nApp started at: http://localhost:8080/books\n");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {}
}
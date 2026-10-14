package sumdu.edu.ua.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sumdu.edu.ua.persistence.jdbc.DbInit;

@WebListener
public class AppInit implements ServletContextListener {

    private static final Logger log = LoggerFactory.getLogger(AppInit.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            log.info("Initializing database...");
            DbInit.init(); // Викликаємо ініціалізацію БД з модуля persistence
            log.info("Database initialized successfully.");
        } catch (Exception e) {
            log.error("DB init failed", e);
            throw new RuntimeException("DB init failed", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }
}
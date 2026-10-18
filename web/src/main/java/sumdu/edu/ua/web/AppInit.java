package sumdu.edu.ua.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import sumdu.edu.ua.persistence.jdbc.DbInit;

@SpringBootApplication(scanBasePackages = "sumdu.edu.ua")
public class AppInit {
    public static void main(String[] args) {
        // Принудительно переводим на чистые сервлеты и отключаем Spring MVC
        System.setProperty("spring.main.web-application-type", "servlet");
        System.setProperty("spring.autoconfigure.exclude",
                "org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration," +
                        "org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration");

        DbInit.init();
        SpringApplication.run(AppInit.class, args);
        System.out.println("Started at http://localhost:8080/books");
    }
}
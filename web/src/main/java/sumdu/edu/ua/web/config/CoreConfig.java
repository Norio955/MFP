package sumdu.edu.ua.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import sumdu.edu.ua.core.port.MailPort;
import sumdu.edu.ua.core.port.UserRepositoryPort;
import sumdu.edu.ua.core.service.UserService;

@Configuration
public class CoreConfig {
    @Bean
    public UserService userService(UserRepositoryPort userRepositoryPort, MailPort mailPort, PasswordEncoder passwordEncoder) {
        return new UserService(userRepositoryPort, mailPort, passwordEncoder);
    }
}
package sumdu.edu.ua.core.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import sumdu.edu.ua.core.domain.User;
import sumdu.edu.ua.core.port.MailPort;
import sumdu.edu.ua.core.port.UserRepositoryPort;

import java.util.Optional;
import java.util.UUID;

public class UserService {

    private final UserRepositoryPort userRepositoryPort;
    private final MailPort mailPort;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryPort userRepositoryPort, MailPort mailPort, PasswordEncoder passwordEncoder) {
        this.userRepositoryPort = userRepositoryPort;
        this.mailPort = mailPort;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(String email, String rawPassword, String nickname) {
        if (userRepositoryPort.findByEmail(email).isPresent()) {
            throw new RuntimeException("Користувач з таким email вже існує");
        }

        User u = new User();
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(rawPassword));
        u.setRole("ROLE_REGISTERED");
        u.setVerified(false);
        u.setNickname(nickname);

        String token = UUID.randomUUID().toString();
        u.setVerificationToken(token);

        userRepositoryPort.save(u);
        mailPort.sendVerificationEmail(u.getEmail(), token);
    }

    public boolean verifyAccount(String token) {
        Optional<User> opt = userRepositoryPort.findByVerificationToken(token);
        if (opt.isEmpty()) return false;

        User u = opt.get();
        u.setVerified(true);
        u.setRole("ROLE_USER");
        u.setVerificationToken(null);

        userRepositoryPort.save(u);
        return true;
    }

    public User findByEmailOrThrow(String email) {
        return userRepositoryPort.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}
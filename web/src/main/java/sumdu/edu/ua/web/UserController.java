package sumdu.edu.ua.web;

import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sumdu.edu.ua.core.domain.User;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.core.port.UserRepositoryPort;

@Controller
public class UserController {

    private final UserRepositoryPort userPort;
    private final CommentRepositoryPort commentPort;

    public UserController(UserRepositoryPort userPort, CommentRepositoryPort commentPort) {
        this.userPort = userPort;
        this.commentPort = commentPort;
    }

    @GetMapping("/users/{id}/comments")
    @Transactional(readOnly = true)
    public String userComments(@PathVariable("id") Long id, Model model) {
        User user = userPort.findById(id).orElseThrow(() -> new RuntimeException("Користувача не знайдено"));
        var comments = commentPort.findByUserId(id);

        // Використовуємо nickname, а якщо він відсутній — email
        String displayName = (user.getNickname() != null && !user.getNickname().isBlank())
                ? user.getNickname()
                : user.getEmail();

        model.addAttribute("username", displayName);
        model.addAttribute("comments", comments);

        return "user-comments";
    }
}
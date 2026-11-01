package sumdu.edu.ua.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import sumdu.edu.ua.core.service.UserService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final UserService userService;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public GlobalControllerAdvice(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("currentUserNickname")
    public String currentUserNickname() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return userService.findByEmailOrThrow(auth.getName()).getNickname();
    }

    @ModelAttribute("currentTime")
    public String currentTime() {
        return LocalDateTime.now().format(timeFormatter);
    }

    @ModelAttribute("uri")
    public String uri(HttpServletRequest request) {
        return request.getRequestURI();
    }
}
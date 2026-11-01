package sumdu.edu.ua.web.controller;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.core.service.UserService;
import sumdu.edu.ua.web.dto.RegisterUserDto;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new RegisterUserDto());
        return "sign-up";
    }

    @PostMapping("/register")
    public String processRegister(@ModelAttribute("user") @Valid RegisterUserDto dto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) return "sign-up";

        try {
            userService.register(dto.getEmail(), dto.getPassword(), dto.getNickname());
            return "sign-up-success";
        } catch (RuntimeException ex) {
            model.addAttribute("error", ex.getMessage());
            return "sign-up";
        }
    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        boolean ok = userService.verifyAccount(token);
        return ok ? "confirm-success" : "confirm-error";
    }
}
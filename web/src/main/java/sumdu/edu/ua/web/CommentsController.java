package sumdu.edu.ua.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sumdu.edu.ua.core.domain.PageRequest;
import sumdu.edu.ua.core.port.CatalogRepositoryPort;
import sumdu.edu.ua.core.port.CommentRepositoryPort;
import sumdu.edu.ua.core.service.CommentService;

@Controller
@RequestMapping("/comments")
public class CommentsController {

    private final CommentRepositoryPort commentRepo;
    private final CatalogRepositoryPort bookRepo;
    private final CommentService commentService;

    public CommentsController(CommentRepositoryPort commentRepo, CatalogRepositoryPort bookRepo) {
        this.commentRepo = commentRepo;
        this.bookRepo = bookRepo;
        this.commentService = new CommentService(commentRepo);
    }

    @GetMapping
    public String list(@RequestParam long bookId, Model model) {
        var book = bookRepo.findById(bookId);
        if (book == null) return "redirect:/books";

        var comments = commentRepo.list(bookId, null, null, new PageRequest(0, 20)).getItems();

        model.addAttribute("book", book);
        model.addAttribute("comments", comments);
        return "book-comments"; // Шукає templates/book-comments.html
    }

    @PostMapping
    public String add(@RequestParam long bookId, @RequestParam String author, @RequestParam String text) {
        commentRepo.add(bookId, author.trim(), text.trim());
        return "redirect:/comments?bookId=" + bookId;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam long bookId, @RequestParam long commentId) {
        commentService.deleteComment(bookId, commentId);
        return "redirect:/comments?bookId=" + bookId;
    }
}
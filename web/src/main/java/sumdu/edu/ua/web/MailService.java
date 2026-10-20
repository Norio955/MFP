package sumdu.edu.ua.web;

import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import sumdu.edu.ua.core.domain.Book;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailService {

    private final JavaMailSender mailSender;
    private final EmailTemplateProcessor templateProcessor;

    public MailService(JavaMailSender mailSender, EmailTemplateProcessor templateProcessor) {
        this.mailSender = mailSender;
        this.templateProcessor = templateProcessor;
    }

    public void sendNewBookEmail(Book book) {
        Map<String, Object> model = new HashMap<>();
        model.put("title", book.getTitle());
        model.put("author", book.getAuthor());
        model.put("year", book.getPubYear());
        model.put("added", new java.util.Date());

        String html = templateProcessor.process("new_book.ftl", model);
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, "UTF-8");

            // ТУТ ВКАЖИ СВІЙ EMAIL ДЛЯ ОТРИМАННЯ ТЕСТОВИХ ЛИСТІВ
            helper.setTo("santerro955@gmail.com");
            helper.setSubject("Нова книга в каталозі: " + book.getTitle());
            helper.setText(html, true);
            helper.setFrom("bookapp@erlkonig.sumdu.edu.ua");

            mailSender.send(message);

        } catch (Exception e) {
            throw new RuntimeException("Email sending failed", e);
        }
    }
}
package sumdu.edu.ua.web.adapter;

import org.springframework.stereotype.Component;
import sumdu.edu.ua.core.port.MailPort;

@Component
public class SmtpMailAdapter implements MailPort {
    // В реальном проекте здесь используется JavaMailSender,
    // но для лабы мы сымитируем отправку, выводя ссылку в консоль
    @Override
    public void sendVerificationEmail(String toEmail, String token) {
        System.out.println("==================================================");
        System.out.println("EMAIL SENT TO: " + toEmail);
        System.out.println("ACTIVATION LINK: http://localhost:8080/confirm?token=" + token);
        System.out.println("==================================================");
    }
}
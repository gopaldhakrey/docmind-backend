package in.strikes.docmind_backend.service;

import lombok.RequiredArgsConstructor;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {

        String resetLink =
                frontendUrl + "/reset-password?token=" + resetToken;

        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("DocMind - Password Reset");
        message.setText(
                "Hello,\n\n" +
                        "We received a request to reset your DocMind password.\n\n" +
                        "Click the link below to reset your password:\n" +
                        resetLink + "\n\n" +
                        "This link will expire in 15 minutes.\n\n" +
                        "If you did not request a password reset, you can safely ignore this email.\n\n" +
                        "Regards,\n" +
                        "DocMind Team"
        );

        mailSender.send(message);
    }
}
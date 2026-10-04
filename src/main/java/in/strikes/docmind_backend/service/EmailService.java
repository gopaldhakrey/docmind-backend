package in.strikes.docmind_backend.service;

import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${app.email.resend-api-key}")
    private String resendApiKey;

    @Value("${app.email.from}")
    private String fromEmail;

    @Value("${app.frontend-url}")
    private String frontendUrl;

    public void sendPasswordResetEmail(String toEmail, String resetToken) {

        String resetLink =
                frontendUrl + "/reset-password?token=" + resetToken;

        String html = """
                <html>
                <body>
                    <h2>DocMind - Password Reset</h2>

                    <p>Hello,</p>

                    <p>
                        We received a request to reset your DocMind password.
                    </p>

                    <p>
                        Click the button below to reset your password:
                    </p>

                    <p>
                        <a href="%s"
                           style="
                               display:inline-block;
                               padding:10px 20px;
                               background:#000;
                               color:#fff;
                               text-decoration:none;
                               border-radius:6px;
                           ">
                            Reset Password
                        </a>
                    </p>

                    <p>
                        This link will expire in 15 minutes.
                    </p>

                    <p>
                        If you did not request a password reset,
                        you can safely ignore this email.
                    </p>

                    <p>
                        Regards,<br>
                        DocMind Team
                    </p>
                </body>
                </html>
                """.formatted(resetLink);

        try {
            Resend resend = new Resend(resendApiKey);

            CreateEmailOptions params = CreateEmailOptions.builder()
                    .from(fromEmail)
                    .to(toEmail)
                    .subject("DocMind - Password Reset")
                    .html(html)
                    .build();

            resend.emails().send(params);

        } catch (ResendException e) {
            throw new RuntimeException(
                    "Failed to send password reset email",
                    e
            );
        }
    }
}
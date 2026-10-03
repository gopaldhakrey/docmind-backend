package in.strikes.docmind_backend.service;

import in.strikes.docmind_backend.entity.PasswordResetToken;
import in.strikes.docmind_backend.entity.User;
import in.strikes.docmind_backend.repository.PasswordResetTokenRepository;
import in.strikes.docmind_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;


    @Transactional
    public String createResetToken(String email) {

        User user = userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            return null;
        }

        // Remove any previous reset tokens for this user
        passwordResetTokenRepository.deleteByUser(user);

        String token = generateSecureToken();

        PasswordResetToken resetToken = PasswordResetToken.builder()
                .tokenHash(hashToken(token))
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(15))
                .used(false)
                .build();

        passwordResetTokenRepository.save(resetToken);

        emailService.sendPasswordResetEmail(email, token);

        return token;
    }

    @Transactional
    public void resetPassword(String token, String newPassword) {

        String tokenHash = hashToken(token);

        PasswordResetToken resetToken =
                passwordResetTokenRepository.findByTokenHashAndUsedFalse(tokenHash)
                        .orElseThrow(() ->
                                new RuntimeException("Invalid or expired reset token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        User user = resetToken.getUser();

        user.setPassword(passwordEncoder.encode(newPassword));

        userRepository.save(user);

        resetToken.setUsed(true);
        passwordResetTokenRepository.save(resetToken);
    }

    private String generateSecureToken() {
        return UUID.randomUUID().toString()
                + UUID.randomUUID().toString();
    }
    private String hashToken(String token) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    token.getBytes(StandardCharsets.UTF_8)
            );

            StringBuilder hex = new StringBuilder();

            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }

            return hex.toString();

        } catch (Exception e) {
            throw new IllegalStateException("Unable to hash reset token", e);
        }
    }
}
package in.strikes.docmind_backend.repository;

import in.strikes.docmind_backend.entity.PasswordResetToken;
import in.strikes.docmind_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository
        extends JpaRepository<PasswordResetToken, Long> {

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    Optional<PasswordResetToken> findByTokenHashAndUsedFalse(String tokenHash);

    void deleteByUser(User user);
}
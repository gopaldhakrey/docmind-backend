package in.strikes.docmind_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordRequest {

    @NotBlank(message = "Reset token cannot be empty")
    private String token;

    @NotBlank(message = "New password cannot be empty")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;
}
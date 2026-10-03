package in.strikes.docmind_backend.dto.conversation;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RenameConversationRequest {

    @NotBlank(message = "Conversation title cannot be empty")
    @Size(
            max = 100,
            message = "Conversation title cannot exceed 100 characters"
    )
    private String title;
}
package in.strikes.docmind_backend.dto.conversation;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ConversationSummaryDto {

    private String id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package in.strikes.docmind_backend.dto.conversation;

import in.strikes.docmind_backend.dto.CitationDto;
import in.strikes.docmind_backend.entity.MessageType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ChatMessageDto {
    private Long id;
    private MessageType messageType;
    private String content;
    private LocalDateTime createdAt;
    private List<CitationDto> citations;
}
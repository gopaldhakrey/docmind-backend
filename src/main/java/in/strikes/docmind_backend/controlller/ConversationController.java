package in.strikes.docmind_backend.controlller;

import in.strikes.docmind_backend.dto.conversation.ChatMessageDto;
import in.strikes.docmind_backend.dto.conversation.ConversationSummaryDto;
import in.strikes.docmind_backend.entity.ChatMessage;
import in.strikes.docmind_backend.entity.Conversation;
import in.strikes.docmind_backend.entity.User;
import in.strikes.docmind_backend.service.ConversationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import in.strikes.docmind_backend.dto.conversation.RenameConversationRequest;
import jakarta.validation.Valid;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.List;
import in.strikes.docmind_backend.dto.CitationDto;


@RestController
@RequestMapping("/api/v1/conversations")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;
    private final ObjectMapper objectMapper;

    @GetMapping
    public ResponseEntity<List<ConversationSummaryDto>> getConversations(
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        List<ConversationSummaryDto> conversations =
                conversationService.getUserConversations(user)
                        .stream()
                        .map(this::toSummaryDto)
                        .toList();

        return ResponseEntity.ok(conversations);
    }

    @GetMapping("/{conversationId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessages(
            @PathVariable String conversationId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        List<ChatMessageDto> messages =
                conversationService
                        .getMessages(conversationId, user)
                        .stream()
                        .map(this::toMessageDto)
                        .toList();

        return ResponseEntity.ok(messages);
    }

    @DeleteMapping("/{conversationId}")
    public ResponseEntity<Void> deleteConversation(
            @PathVariable String conversationId,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        conversationService.deleteConversation(
                conversationId,
                user
        );

        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{conversationId}")
    public ResponseEntity<ConversationSummaryDto> renameConversation(
            @PathVariable String conversationId,
            @Valid @RequestBody RenameConversationRequest request,
            Authentication authentication
    ) {
        User user = (User) authentication.getPrincipal();

        Conversation conversation =
                conversationService.renameConversation(
                        conversationId,
                        request,
                        user
                );

        return ResponseEntity.ok(toSummaryDto(conversation));
    }

    private ConversationSummaryDto toSummaryDto(
            Conversation conversation
    ) {
        return ConversationSummaryDto.builder()
                .id(conversation.getId())
                .title(conversation.getTitle())
                .createdAt(conversation.getCreatedAt())
                .updatedAt(conversation.getUpdatedAt())
                .build();
    }

    private ChatMessageDto toMessageDto(ChatMessage message) {

        List<CitationDto> citations = List.of();

        if (message.getMetadata() != null
                && !message.getMetadata().isBlank()) {

            try {
                JsonNode metadata =
                        objectMapper.readTree(message.getMetadata());

                JsonNode citationsNode =
                        metadata.get("citations");

                if (citationsNode != null && !citationsNode.isNull()) {
                    citations = objectMapper.convertValue(
                            citationsNode,
                            new TypeReference<List<CitationDto>>() {}
                    );
                }

            } catch (JsonProcessingException e) {
                // Keep history loading even if old metadata is malformed.
                citations = List.of();
            }
        }

        return ChatMessageDto.builder()
                .id(message.getId())
                .messageType(message.getMessageType())
                .content(message.getContent())
                .createdAt(message.getCreatedAt())
                .citations(citations)
                .build();
    }
}
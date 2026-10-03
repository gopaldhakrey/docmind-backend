package in.strikes.docmind_backend.service;

import in.strikes.docmind_backend.entity.ChatMessage;
import in.strikes.docmind_backend.entity.Conversation;
import in.strikes.docmind_backend.entity.User;
import in.strikes.docmind_backend.repository.ChatMessageRepository;
import in.strikes.docmind_backend.repository.ConversationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import in.strikes.docmind_backend.dto.conversation.RenameConversationRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConversationService {

    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional(readOnly = true)
    public List<Conversation> getUserConversations(User user) {
        return conversationRepository.findByUserOrderByUpdatedAtDesc(user);
    }

    @Transactional(readOnly = true)
    public Conversation getConversation(String conversationId, User user) {
        return conversationRepository
                .findByIdAndUser(conversationId, user)
                .orElseThrow(() ->
                        new IllegalArgumentException("Conversation not found"));
    }

    @Transactional(readOnly = true)
    public List<ChatMessage> getMessages(
            String conversationId,
            User user
    ) {
        // First verify ownership.
        getConversation(conversationId, user);

        return chatMessageRepository
                .findByConversation_IdOrderByCreatedAtAsc(conversationId);
    }

    @Transactional
    public void deleteConversation(
            String conversationId,
            User user
    ) {
        Conversation conversation =
                getConversation(conversationId, user);

        conversationRepository.delete(conversation);
    }
    @Transactional
    public Conversation renameConversation(
            String conversationId,
            RenameConversationRequest request,
            User user
    ) {
        Conversation conversation = getConversation(
                conversationId,
                user
        );

        conversation.setTitle(request.getTitle().trim());
        conversation.setUpdatedAt(java.time.LocalDateTime.now());

        return conversationRepository.save(conversation);
    }
}
package in.strikes.docmind_backend.service;

import in.strikes.docmind_backend.dto.AdminDashboardDto;
import in.strikes.docmind_backend.repository.ChatMessageRepository;
import in.strikes.docmind_backend.repository.ConversationRepository;
import in.strikes.docmind_backend.repository.DocumentMetadataRepo;
import in.strikes.docmind_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDashboardService {

    private final UserRepository userRepository;
    private final DocumentMetadataRepo documentMetadataRepo;
    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;

    public AdminDashboardDto getDashboardStats() {

        return AdminDashboardDto.builder()
                .totalUsers(userRepository.count())
                .totalDocuments(documentMetadataRepo.count())
                .totalConversations(conversationRepository.count())
                .totalMessages(chatMessageRepository.count())
                .build();
    }
}
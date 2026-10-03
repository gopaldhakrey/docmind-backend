package in.strikes.docmind_backend.service;

import in.strikes.docmind_backend.dto.AdminUserDto;
import in.strikes.docmind_backend.entity.User;
import in.strikes.docmind_backend.repository.ConversationRepository;
import in.strikes.docmind_backend.repository.DocumentMetadataRepo;
import in.strikes.docmind_backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import in.strikes.docmind_backend.entity.Role;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminUserService {

    private final UserRepository userRepository;
    private final DocumentMetadataRepo documentMetadataRepo;
    private final ConversationRepository conversationRepository;
    private final DocumentMetadataService documentMetadataService;

    public List<AdminUserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public void deleteUser(Long userId, Long adminUserId) {

        // Prevent admin from deleting their own account
        if (userId.equals(adminUserId)) {
            throw new IllegalArgumentException(
                    "You cannot delete your own admin account"
            );
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        // Delete user's documents and their vector data
        documentMetadataRepo.findByUser(user)
                .forEach(document ->
                        documentMetadataService.deleteDocument(document.getId())
                );

        // Delete user's conversations.
        // Chat messages are removed through Conversation cascade/orphanRemoval.
        conversationRepository.findByUserOrderByUpdatedAtDesc(user)
                .forEach(conversation ->
                        conversationRepository.delete(conversation)
                );

        // Finally delete the user
        userRepository.delete(user);
    }

    public AdminUserDto updateUserRole(Long userId, Role role) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new IllegalArgumentException("User not found")
                );

        user.setRole(role);

        User updatedUser = userRepository.save(user);

        return toDto(updatedUser);
    }

    private AdminUserDto toDto(User user) {
        return AdminUserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
package in.strikes.docmind_backend.service;

import in.strikes.docmind_backend.dto.AdminDocumentDto;
import in.strikes.docmind_backend.entity.DocumentMetadata;
import in.strikes.docmind_backend.repository.DocumentMetadataRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminDocumentService {

    private final DocumentMetadataRepo documentMetadataRepo;
    private final DocumentMetadataService documentMetadataService;

    public List<AdminDocumentDto> getAllDocuments() {

        return documentMetadataRepo.findAll()
                .stream()
                .map(this::toDto)
                .toList();
    }
    public void deleteDocument(UUID documentId) {
        documentMetadataService.deleteDocument(documentId);
    }

    private AdminDocumentDto toDto(DocumentMetadata document) {

        return AdminDocumentDto.builder()
                .id(document.getId())
                .fileName(document.getFilename())
                .status(document.getStatus())
                .userId(document.getUser().getId())
                .username(document.getUser().getUsername())
                .createdAt(document.getCreatedAt())
                .build();
    }
}
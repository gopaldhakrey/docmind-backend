package in.strikes.docmind_backend.dto;

import in.strikes.docmind_backend.entity.DocumentStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AdminDocumentDto {

    private UUID id;
    private String fileName;
    private DocumentStatus status;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
}